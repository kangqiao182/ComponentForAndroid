package com.zp.android.common

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import okhttp3.OkHttpClient
import java.io.InputStream


/**
 * Created by zhaopan on 2018/10/20.
 * https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
 */

@GlideModule
//@Excludes(OkHttpLibraryGlideModule::class) // initialize OkHttp manually
class MyAppGlideModule : AppGlideModule() {

    /*override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val okHttpClient = OkHttpClient()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))

    }*/

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // Glide default Bitmap Format is set to RGB_565 since it
        // consumed just 50% memory footprint compared to ARGB_8888.
        // Increase memory usage for quality with:

        builder.setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_ARGB_8888))
            .setDiskCache(
                InternalCacheDiskCacheFactory(
                    context, diskCacheFolderName(context), diskCacheSizeBytes()
                )
            )
            .setMemoryCache(LruResourceCache(memoryCacheSizeBytes().toLong()))
    }

    /**
     * Implementations should return `false` after they and their dependencies have migrated
     * to Glide's annotation processor.
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     * set the memory cache size, unit is the [Byte].
     */
    private fun memoryCacheSizeBytes(): Int {
        return 1024 * 1024 * 20 // 20 MB
    }

    /**
     * set the disk cache size, unit is the [Byte].
     */
    private fun diskCacheSizeBytes(): Long {
        return 1024 * 1024 * 512 // 512 MB
    }

    /**
     * set the disk cache folder's name.
     */
    private fun diskCacheFolderName(context: Context): String {
        return "android-zp"
    }
}