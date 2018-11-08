package com.zp.android.component.event

/**
 * Created by zhaopan on 2018/8/22.
 * 组件间公共事件, 方便数据传递.
 */

open class ComponentEvent

object LoginSuccessEvent: ComponentEvent()

object RegisterSuccessEvent: ComponentEvent()
//class RegisterSuccessEvent(var username: String = ""): ComponentEvent()

object LogoutSuccessEvent: ComponentEvent()