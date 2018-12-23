package com.zp.android.common

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.*
import java.security.cert.CertificateException
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal

/**
 * Created by zhaopan on 2018/5/30.
 */

object CipherStorageFactory {
    /**
     * Create a new instance of the [CipherStorage] based on the
     * current api level, on API 22 and bellow it will use the [CipherStorageAndroidKeystore]
     * and on api 23 and above it will use the [CipherStorageSharedPreferencesKeystore]
     *
     * @param context used for api 22 and bellow to access the keystore and
     * access the Android Shared preferences, on api 23 and above
     * it's only used for Android Shared Preferences access
     * @return a new [CipherStorage] based on the current api level
     */
    fun newInstance(context: Context): CipherStorage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            CipherStorageAndroidKeystore(context)
        else
            CipherStorageSharedPreferencesKeystore(context)
    }
}

interface CipherStorage {

    fun encrypt(alias: String, value: String)

    fun decrypt(alias: String): String?

    fun containsAlias(alias: String): Boolean

    fun removeKey(alias: String)

    fun saveOrReplace(alias: String, value: String)
}


internal abstract class BaseCipherStorage(val context: Context) : CipherStorage {

    companion object {
        const val ANDROID_KEY_STORE = "AndroidKeyStore"

        val keyStoreAndLoad: KeyStore
            get() {
                try {
                    val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
                    keyStore.load(null)
                    return keyStore
                } catch (e: NoSuchAlgorithmException) {
                    throw KeyStoreAccessException("Could not access Keystore", e)
                } catch (e: CertificateException) {
                    throw KeyStoreAccessException("Could not access Keystore", e)
                } catch (e: KeyStoreException) {
                    throw KeyStoreAccessException("Could not access Keystore", e)
                } catch (e: IOException) {
                    throw KeyStoreAccessException("Could not access Keystore", e)
                }
            }
    }

    override fun containsAlias(alias: String): Boolean {
        try {
            return keyStoreAndLoad.containsAlias(alias) && CipherPreferencesStorage.containsAlias(context, alias)
        } catch (e: KeyStoreException) {
            throw KeyStoreAccessException("Failed to access Keystore", e)
        }
    }

    override fun removeKey(alias: String) {
        try {
            if (containsAlias(alias)) {
                keyStoreAndLoad.deleteEntry(alias)
                CipherPreferencesStorage.remove(context, alias)
            }
        } catch (e: KeyStoreException) {
            throw KeyStoreAccessException("Failed to access Keystore", e)
        }
    }

    override fun saveOrReplace(alias: String, value: String) {
        if (containsAlias(alias)) {
            removeKey(alias)
        }
        encrypt(alias, value)
    }
}

/**
 * M(23) 以上使用AES/CBC/PKCS7Padding
 */
@TargetApi(Build.VERSION_CODES.M)
internal class CipherStorageAndroidKeystore(context: Context) : BaseCipherStorage(context) {

    override fun encrypt(alias: String, value: String) {
        try {
            val generator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, BaseCipherStorage.ANDROID_KEY_STORE)
            generator.init(generateParameterSpec(alias))
            generator.generateKey()

            val key = keyStoreAndLoad.getKey(alias, null)
            val encryptedData = encryptString(key, value)
            CipherPreferencesStorage.saveKeyBytes(context, alias, encryptedData)
        } catch (e: NoSuchAlgorithmException) {
            throw CryptoFailedException("Could not encrypt data", e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw CryptoFailedException("Could not encrypt data", e)
        } catch (e: NoSuchProviderException) {
            throw CryptoFailedException("Could not encrypt data", e)
        } catch (e: UnrecoverableKeyException) {
            throw CryptoFailedException("Could not encrypt data", e)
        } catch (e: KeyStoreException) {
            throw CryptoFailedException("Could not access Keystore", e)
        } catch (e: KeyStoreAccessException) {
            throw CryptoFailedException("Could not access Keystore", e)
        }

    }

    override fun decrypt(alias: String): String? {
        try {
            val storedData = CipherPreferencesStorage.getKeyBytes(context, alias) ?: return null
            /* Well this should not happen if you do not have a stored byte data, but just in case */
            val key = keyStoreAndLoad.getKey(alias, null) ?: return null
            return decryptBytes(key, storedData)
        } catch (e: KeyStoreException) {
            return null
        } catch (e: UnrecoverableKeyException) {
            return null
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: KeyStoreAccessException) {
            return null
        }
    }

    companion object {
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val ENCRYPTION_TRANSFORMATION = ENCRYPTION_ALGORITHM + "/" + ENCRYPTION_BLOCK_MODE + "/" + ENCRYPTION_PADDING
        private const val ENCRYPTION_KEY_SIZE = 256
        private val DEFAULT_CHARSET = Charset.forName("UTF-8")

        private fun generateParameterSpec(alias: String): AlgorithmParameterSpec {
            return KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
                    .setBlockModes(ENCRYPTION_BLOCK_MODE)
                    .setEncryptionPaddings(ENCRYPTION_PADDING)
                    .setRandomizedEncryptionRequired(true)
                    .setKeySize(ENCRYPTION_KEY_SIZE)
                    .build()
        }

        @Throws(CryptoFailedException::class)
        private fun decryptBytes(key: Key, bytes: ByteArray): String {
            try {
                val cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION)
                val inputStream = ByteArrayInputStream(bytes)
                // read the initialization vector from the beginning of the stream
                val ivParams = readIvFromStream(inputStream)
                cipher.init(Cipher.DECRYPT_MODE, key, ivParams)
                // decrypt the bytes using a CipherInputStream
                val cipherInputStream = CipherInputStream(
                        inputStream, cipher)
                val output = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                while (true) {
                    val n = cipherInputStream.read(buffer, 0, buffer.size)
                    if (n <= 0) {
                        break
                    }
                    output.write(buffer, 0, n)
                }
                return String(output.toByteArray(), DEFAULT_CHARSET)
            } catch (e: IOException) {
                throw CryptoFailedException("Could not decrypt bytes", e)
            } catch (e: NoSuchAlgorithmException) {
                throw CryptoFailedException("Could not decrypt bytes", e)
            } catch (e: NoSuchPaddingException) {
                throw CryptoFailedException("Could not decrypt bytes", e)
            } catch (e: InvalidKeyException) {
                throw CryptoFailedException("Could not decrypt bytes", e)
            } catch (e: InvalidAlgorithmParameterException) {
                throw CryptoFailedException("Could not decrypt bytes", e)
            }

        }

        private fun readIvFromStream(inputStream: ByteArrayInputStream): IvParameterSpec {
            val iv = ByteArray(16)
            inputStream.read(iv, 0, iv.size)
            return IvParameterSpec(iv)
        }

        @Throws(CryptoFailedException::class)
        private fun encryptString(key: Key, value: String): ByteArray {
            try {
                val cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION)
                cipher.init(Cipher.ENCRYPT_MODE, key)
                val outputStream = ByteArrayOutputStream()
                // write initialization vector to the beginning of the stream
                val iv = cipher.iv
                outputStream.write(iv, 0, iv.size)
                // encrypt the value using a CipherOutputStream
                val cipherOutputStream = CipherOutputStream(outputStream, cipher)
                cipherOutputStream.write(value.toByteArray(DEFAULT_CHARSET))
                cipherOutputStream.close()
                return outputStream.toByteArray()
            } catch (e: IOException) {
                throw CryptoFailedException("Could not encrypt value", e)
            } catch (e: NoSuchAlgorithmException) {
                throw CryptoFailedException("Could not encrypt value", e)
            } catch (e: InvalidKeyException) {
                throw CryptoFailedException("Could not encrypt value", e)
            } catch (e: NoSuchPaddingException) {
                throw CryptoFailedException("Could not encrypt value", e)
            }
        }
    }
}

/**
 * LOLLIPOP_MR1(22) 以下版本使用RSA算法.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
internal class CipherStorageSharedPreferencesKeystore(context: Context) : BaseCipherStorage(context) {

    companion object {
        private const val KEY_ALGORITHM_RSA = "RSA"
        private const val KEY_ALGORITHM_AES = "AES"
        private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
        private const val ENCRYPTION_KEY_SIZE = 128
        private const val DURATION_YEAR_AMOUNT = 30
        private const val AES_TAG_PREFIX = "aes!"
        private val DEFAULT_CHARSET = Charset.forName("UTF-8")
        private val KEY_SERIAL_NUMBER = BigInteger.valueOf(1338)

        private fun makeAesTagForAlias(alias: String): String {
            return AES_TAG_PREFIX + alias
        }

        private fun cipherEncryption(transformation: String, mode: Int, key: Key,
                                     inputByteArray: ByteArray): ByteArray {
            try {
                val cipher = Cipher.getInstance(transformation)
                cipher.init(mode, key)
                return cipher.doFinal(inputByteArray)
            } catch (e: NoSuchPaddingException) {
                throw CryptoFailedException(String.format(Locale.US, "Unable to do cipher for transformation %s and mode %d", transformation, mode), e)
            } catch (e: NoSuchAlgorithmException) {
                throw CryptoFailedException(String.format(Locale.US, "Unable to do cipher for transformation %s and mode %d", transformation, mode), e)
            } catch (e: InvalidKeyException) {
                throw CryptoFailedException(String.format(Locale.US, "Unable to do cipher for transformation %s and mode %d", transformation, mode), e)
            } catch (e: BadPaddingException) {
                throw CryptoFailedException(String.format(Locale.US, "Unable to do cipher for transformation %s and mode %d", transformation, mode), e)
            } catch (e: IllegalBlockSizeException) {
                throw CryptoFailedException(String.format(Locale.US, "Unable to do cipher for transformation %s and mode %d", transformation, mode), e)
            }

        }
    }

    override fun encrypt(alias: String, value: String) {
        val entry = getKeyStoreEntry(true, alias)
                ?: throw CryptoFailedException("Unable to generate key for alias $alias")

        val key = entry as KeyStore.PrivateKeyEntry
        val encryptedData = encryptData(alias, value, key.certificate.publicKey)
        CipherPreferencesStorage.saveKeyBytes(context, alias, encryptedData)
    }


    override fun decrypt(alias: String): String? {
        val entry = getKeyStoreEntry(false, alias) ?: return null
        val key = entry as KeyStore.PrivateKeyEntry
        return decryptData(alias, key.privateKey)
    }

    override fun containsAlias(alias: String): Boolean {
        return super.containsAlias(alias) && CipherPreferencesStorage.containsAlias(context, makeAesTagForAlias(alias))
    }


    override fun removeKey(alias: String) {
        super.removeKey(alias)
        CipherPreferencesStorage.remove(context, makeAesTagForAlias(alias))
    }

    private fun decryptData(alias: String, privateKey: PrivateKey): String? {
        val encryptedData = CipherPreferencesStorage.getKeyBytes(context, alias)
        val secretData = CipherPreferencesStorage.getKeyBytes(context, makeAesTagForAlias(alias))
        if (encryptedData == null || secretData == null) {
            return null
        }

        val decryptedData = cipherEncryption(TRANSFORMATION, Cipher.PRIVATE_KEY, privateKey, secretData)
        val secretKey = SecretKeySpec(decryptedData, 0, decryptedData.size, KEY_ALGORITHM_AES)
        val finalData = cipherEncryption(KEY_ALGORITHM_AES, Cipher.DECRYPT_MODE, secretKey, encryptedData)
        return String(finalData, DEFAULT_CHARSET)
    }

    private fun generateKeyRsa(alias: String) {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, BaseCipherStorage.ANDROID_KEY_STORE)
            keyPairGenerator.initialize(getParameterSpec(alias))
            keyPairGenerator.generateKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        } catch (e: NoSuchProviderException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        }
    }

    private fun getParameterSpec(alias: String): AlgorithmParameterSpec {
        val start = GregorianCalendar()
        val end = GregorianCalendar()
        end.add(Calendar.YEAR, DURATION_YEAR_AMOUNT)

        return KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=$alias, O=Android Authority"))
                .setSerialNumber(KEY_SERIAL_NUMBER)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
    }

    private fun getKeyStoreEntry(shouldGenerateKey: Boolean, alias: String): KeyStore.Entry? {
        try {
            var entry = keyStoreAndLoad.getEntry(alias, null) as? KeyStore.Entry
            if (entry == null && shouldGenerateKey) {
                generateKeyRsa(alias)
                entry = keyStoreAndLoad.getEntry(alias, null)
            }
            return entry
        } catch (e: KeyStoreException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        } catch (e: NoSuchAlgorithmException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        } catch (e: UnrecoverableEntryException) {
            throw KeyStoreAccessException("Unable to access keystore", e)
        }
        return null
    }

    private fun encryptData(alias: String, value: String, publicKey: PublicKey): ByteArray {
        val secret = generateKeyAes(alias)
        val rsaEncrypted = cipherEncryption(TRANSFORMATION, Cipher.PUBLIC_KEY, publicKey, secret.encoded)
        CipherPreferencesStorage.saveKeyBytes(context,
                makeAesTagForAlias(alias), rsaEncrypted)
        return cipherEncryption(KEY_ALGORITHM_AES, Cipher.ENCRYPT_MODE, secret, value.toByteArray(DEFAULT_CHARSET))
    }

    private fun generateKeyAes(alias: String): SecretKey {
        try {
            val generator = KeyGenerator.getInstance(KEY_ALGORITHM_AES)
            generator.init(ENCRYPTION_KEY_SIZE)
            return generator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw CryptoFailedException("Unable to generate key for alias $alias", e)
        }
    }
}

/**
 * 密钥存储数据的地方.
 */
internal object CipherPreferencesStorage {
    private val SHARED_PREFERENCES_NAME = "com.android.zp.keystore_security_storage"

    private fun saveKeyString(context: Context, alias: String, value: String) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(alias, value)
                .apply()
    }

    fun remove(context: Context, alias: String) {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(alias)
                .apply()
    }

    fun containsAlias(context: Context, alias: String): Boolean {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .contains(alias)
    }

    private fun getKeyString(context: Context, alias: String): String? {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(alias, null)
    }

    fun getKeyBytes(context: Context, alias: String): ByteArray? {
        val value = getKeyString(context, alias)
        return value?.let { Base64.decode(value, Base64.DEFAULT) } ?: null
    }

    fun saveKeyBytes(context: Context, alias: String, value: ByteArray) {
        saveKeyString(context, alias, Base64.encodeToString(value, Base64.DEFAULT))
    }
}


class CryptoFailedException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}


class KeyStoreAccessException(message: String, cause: Throwable) : RuntimeException(message, cause)