package com.zp.android.app

import android.app.Activity
import android.content.Context
import android.util.Log
import com.zp.android.home.MainActivity
import com.zp.android.home.moduleList
import com.zp.android.home.ui.HomeViewModel
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Created by zhaopan on 2019/5/8.
 */

@RunWith(RobolectricTestRunner::class)
@Config(application = AppTest::class)
class HomeModuleTest : KoinTest {

    lateinit var context: Context
    val viewModel: HomeViewModel by inject()

    @Before
    fun before() {
        context = RuntimeEnvironment.application.applicationContext
        stopKoin()
        startKoin {
            moduleList
        }
        //declareMock<HomeViewModel>()
    }

    @After
    fun after() {
        stopKoin()
    }


    @Test
    fun testMainActivity() {
        try {
            val mainActivity: Activity = Robolectric.setupActivity<MainActivity>(MainActivity::class.java)
        } catch (e: Exception) {

        }
    }

    @Test
    fun testViewModel() {
        try {
            viewModel.articleData.observeForever{
                it?.let {
                    Log.i("HomeViewModel", "observer: articleResponseBody${it}")
                }
            }

            viewModel.bannerList.observeForever{
                it?.let {
                    Log.i("HomeViewModel", "observer: articleResponseBody${it}")
                }
            }


            viewModel.getArticleData(1)
            viewModel.getBannerList()
        }catch (e: Exception) {

        }
    }
}
