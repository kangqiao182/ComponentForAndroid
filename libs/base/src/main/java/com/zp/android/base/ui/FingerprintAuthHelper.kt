package com.zp.android.base.ui

import android.app.Activity
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import android.view.View
import androidx.annotation.RequiresApi
import com.zp.android.base.BR
import com.zp.android.base.CtxUtil
import com.zp.android.base.CtxUtil.showToast
import com.zp.android.base.R
import com.zp.android.base.utils.SPUtil
import com.zp.android.base.widget.dialog.BaseDialogInfo
import com.zp.android.base.widget.dialog.CustomDialog
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

/**
 * Created by zhaopan on 2019/1/8.
 */

interface FingerprintAuthCallBack {
    fun onFingerprintAuth(result: Boolean, msg: String)
}


class FingerprintAuthHelper {
    private var fingerprintManager: FingerprintManagerCompat
    private var authenticationCallback: FingerprintManagerCompat.AuthenticationCallback? = null
    private var cancellationSignal: CancellationSignal? = null
    private var dialogInfo: BaseDialogInfo? = null

    init {
        fingerprintManager = FingerprintManagerCompat.from(CtxUtil.context)
    }

    fun isEnableFingerprint(): Boolean {
        //先判断系统1.是否支持指纹识别, 2.是否已注册指纹, 3. 用户是否开启指纹登录, 开启则弹出指纹识别对话框, 去验证登录.
        return SPUtil.getBoolean(SETTING_FINGERPRINT_ENABLE, false) && isSupportFingerprint()
    }

    fun isSupportFingerprint(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && fingerprintManager.isHardwareDetected()
                && fingerprintManager.hasEnrolledFingerprints()
    }

    fun hasFingerprintDevice(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && fingerprintManager.isHardwareDetected()
    }

    fun cancel() {
        dialogInfo?.binder?.dialog?.dismiss()
        cancellationSignal?.cancel()
    }

    private fun showFingerprintVerifyDialog(context: Activity, callback: FingerprintAuthCallBack? = null) {
        dialogInfo = BaseDialogInfo().apply {
            title = CtxUtil.getString(R.string.base_auth_fingerprint)
            content = CtxUtil.getString(R.string.base_auth_pls_verify_finger)
            error = ""
            cancelListener = View.OnClickListener {
                cancellationSignal?.cancel()
                callback?.onFingerprintAuth(false, "")
                binder.dialog?.dismiss()
            }
        }
        CustomDialog(context as Context, R.layout.base_dialog_fingerprint_verify).apply {
            bindVariable(BR.info, dialogInfo!!)
            //setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    private fun setDialogInfo(title: String, content: String, error: String = "") {
        dialogInfo?.let { info ->
            info.title = title
            info.content = content
            info.error = error
            info.reBindDialogInfo()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun authFingerprint(activity: Activity, callback: FingerprintAuthCallBack) {
        showFingerprintVerifyDialog(activity, callback)
        val dInfo = dialogInfo!!
        try {
            val cryptoObjectHelper = CryptoObjectHelper()
            if (cancellationSignal == null) cancellationSignal = CancellationSignal()
            if (authenticationCallback == null) authenticationCallback = object : FingerprintManagerCompat.AuthenticationCallback() {
                override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errMsgId, errString)
                    setDialogInfo(CtxUtil.getString(R.string.base_auth_fingerprint_failed), "", getFingerAuthErrorMsg(errMsgId))
                    if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                        callback.onFingerprintAuth(false, dInfo.error)
                    }
                }

                override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpMsgId, helpString)
                    setDialogInfo(CtxUtil.getString(R.string.base_auth_fingerprint_failed), "", getFingerAuthHelpMsg(helpMsgId))
                }

                override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    setDialogInfo(CtxUtil.getString(R.string.base_auth_fingerprint), CtxUtil.getString(R.string.base_auth_fingerprint_succeeded), "")
                    callback.onFingerprintAuth(true, dInfo.content)
                    dInfo.dismissDialog()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    setDialogInfo(CtxUtil.getString(R.string.base_auth_fingerprint_failed), "", CtxUtil.getString(R.string.base_auth_fingerprint_failed_tips))
                }
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0, cancellationSignal, authenticationCallback!!, null)
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(R.string.base_auth_fingerprint_init_failed)
        }
    }

    fun getFingerAuthErrorMsg(code: Int): String {
        return when (code) {
            FingerprintManager.FINGERPRINT_ERROR_CANCELED -> CtxUtil.getString(R.string.base_auth_fingerprint_error_canceled)
            FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE -> CtxUtil.getString(R.string.base_auth_fingerprint_error_hw_unavailable)
            FingerprintManager.FINGERPRINT_ERROR_LOCKOUT -> CtxUtil.getString(R.string.base_auth_fingerprint_error_lockout)
            FingerprintManager.FINGERPRINT_ERROR_NO_SPACE -> CtxUtil.getString(R.string.base_auth_fingerprint_error_no_space)
            FingerprintManager.FINGERPRINT_ERROR_TIMEOUT -> CtxUtil.getString(R.string.base_auth_fingerprint_error_timeout)
            FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS -> CtxUtil.getString(R.string.base_auth_fingerprint_error_unable_to_process)
            else -> ""
        }
    }

    fun getFingerAuthHelpMsg(code: Int): String {
        return when (code) {
            FingerprintManager.FINGERPRINT_ACQUIRED_GOOD -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_good)
            FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_imager_dirty)
            FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_insufficient)
            FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_partial)
            FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_too_fast)
            FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW -> CtxUtil.getString(R.string.base_auth_fingerprint_acquired_too_slow)
            else -> ""
        }
    }

    class CryptoObjectHelper @Throws(Exception::class) constructor() {
        val keyStore: KeyStore

        init {
            keyStore = KeyStore.getInstance(KEYSTORE_NAME)
            keyStore.load(null)
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Throws(Exception::class)
        fun buildCryptoObject(): FingerprintManagerCompat.CryptoObject {
            val cipher = createCipher(true)
            return FingerprintManagerCompat.CryptoObject(cipher)
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Throws(Exception::class)
        internal fun createCipher(retry: Boolean): Cipher {
            val key = GetKey()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            try {
                cipher.init(Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE, key)
            } catch (e: KeyPermanentlyInvalidatedException) {
                keyStore.deleteEntry(KEY_NAME)
                if (retry) {
                    createCipher(false)
                } else {
                    throw Exception("Could not create the cipher for fingerprint authentication.", e)
                }
            }
            return cipher
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Throws(Exception::class)
        internal fun GetKey(): Key {
            val secretKey: Key
            if (!keyStore.isKeyEntry(KEY_NAME)) {
                CreateKey()
            }
            secretKey = keyStore.getKey(KEY_NAME, null)
            return secretKey
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Throws(Exception::class)
        internal fun CreateKey() {
            val keyGen = KeyGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_NAME)
            val keyGenSpec = KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setUserAuthenticationRequired(true)
                .build()
            keyGen.init(keyGenSpec)
            keyGen.generateKey()
        }

        companion object {
            internal val KEY_NAME = "com.chainup.wallet.android.fingerprint_authentication_key"
            internal val KEYSTORE_NAME = "AndroidKeyStore"
            // Should be no need to change these values.
            internal val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
            internal val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
            internal val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
            internal val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$ENCRYPTION_PADDING"
        }
    }
}