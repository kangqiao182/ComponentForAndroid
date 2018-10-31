package com.zp.android.base.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Environment
import android.widget.ScrollView
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * Created by zhaopan on 2018/7/14.
 *
 */
object CaptureViewUtil {

    private fun createBitmap(w: Int, h: Int) = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    fun getViewBitmap(view: View): Bitmap {
        var w = 0
        var h = 0
        when (view) {
            is ScrollView -> {
                for (i in 0 until (view as ScrollView).childCount) {
                    h += (view as ScrollView).getChildAt(i).height
                }
                w = view.width
            }
            else -> {
                w = view.width
                h = view.height
            }
        }
        val bitmap = createBitmap(w, h)
        view.draw(Canvas(bitmap))
        return bitmap
    }

    @JvmOverloads
    fun saveImg(bitmap: Bitmap, fileName: String = "zp.png") {
        val dirFile = File(Environment.getExternalStorageDirectory(), "ZP")
        if (!dirFile.exists()) {
            dirFile.mkdir()
        }
        val file = File(dirFile, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        try {
            val fileOutputStream = FileOutputStream(file)
            //这个100表示压缩比,100说明不压缩,90说明压缩到原来的90%
            //注意:这是对于占用存储空间而言,不是说占用内存的大小
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
        //通知图库即使更新,否则不能看到图片
        //App.app.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())))
    }

    fun bitmap2ByteArray(bitmap: Bitmap): ByteArray{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        try {
            baos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return byteArray
    }

    fun ByteArray2Bitmap(byteArray: ByteArray): Bitmap?{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}