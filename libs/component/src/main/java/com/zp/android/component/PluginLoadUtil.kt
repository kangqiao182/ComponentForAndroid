package com.zp.android.component

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*

/**
 * Created by zhaopan on 2018/9/4.
 */
object PluginLoadUtil {
    const val TAG = "PluginLoadUtil"

    fun getExternalFile(context: Context, fileName: String): File?{
        try {
            val dir = context.getExternalFilesDir(null)
            val file = File(dir, fileName)
            return file
        } catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        Log.e(TAG, "isExternalStorageWritable: $state")
        return Environment.MEDIA_MOUNTED.equals(state)
    }

    fun saveAsFileFromAssets(context: Context, assetPath: String, outPutName: String): File? {
        if (!isExternalStorageWritable()) {
            throw RuntimeException("Permission denied!!! Can not write to external storage.")
        }

        var inputStream: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            val assetManager = context.applicationContext.assets
            inputStream = assetManager.open(assetPath)

            val dir = context.getExternalFilesDir(null)
            Log.e(TAG, "save as dir: ${dir.absolutePath} and is exists=${dir.exists()}")
            if (!dir.exists()) dir.mkdir()
            val destFile = File(dir, outPutName)
            if (destFile.exists()) destFile.delete()
            destFile.createNewFile()
            fos = FileOutputStream(destFile)

            var buffer = ByteArray(1024)
            var count = inputStream.read(buffer)
            while ( count != -1) {
                fos.write(buffer, 0, count)
                count = inputStream.read(buffer)
            }

            fos.flush()
            inputStream.close()
            fos.close()
            return destFile
        } catch (e: IOException){
            e.printStackTrace()
            return null
        } finally {
            try {
                inputStream?.close()
                fos?.close()
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
        return null
    }

    fun delFileRecurision(file: File){
        if(file.isFile){
            file.delete()
            return
        }
        if(file.isDirectory){
            val childFiles = file.listFiles()
            if(childFiles == null || childFiles.size == 0){
                file.delete()
                return
            }
            for (file in childFiles){
                delFileRecurision(file)
            }
            file.delete()
        }
    }


}
