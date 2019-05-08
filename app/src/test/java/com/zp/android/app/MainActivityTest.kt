package com.zp.android.app

import android.app.Activity
import android.content.Context
import android.os.Build
import com.zp.android.app.ui.MainActivity
import com.zp.android.base.BaseActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.lang.Exception


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = AppTest::class)
class MainActivityTest {

    lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.applicationContext
    }

    @Test
    fun testMainActivity() {
        try {
            val mainActivity: Activity = Robolectric.setupActivity<MainActivity>(MainActivity::class.java)
        } catch (e: Exception) {

        }
    }
}
