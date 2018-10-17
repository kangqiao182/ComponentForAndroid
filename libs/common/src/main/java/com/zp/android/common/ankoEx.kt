package com.zp.android.common

import android.app.Activity
import android.content.Context
import android.view.View
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.backgroundColorResource

/**
 * Created by zhaopan on 2018/8/17.
 */

//fun Context.lineView(): View{
//    with(this){
//
//        view { backgroundColorResource = R.color.base_line }.lparams(matchParent, dimen(R.dimen.base_divider_line_height))
//    }
//}


fun <Owner> AnkoContext<Owner>.finishUI(){
    (ctx as? Activity)?.apply { finish() }
}

fun <T : Activity, Owner> AnkoComponent<Owner>.setContentView(activity: T, owner: Owner): View =
        createView(AnkoContextImpl(activity, owner, true))

/*
fun <T : SupportFragment, Owner> AnkoComponent<Owner>.setContentView(fragment: T, owner: Owner): View =
        createView(AnkoContextImpl(fragment.activity as Context, owner, true))

*/

