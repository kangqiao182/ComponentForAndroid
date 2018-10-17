package com.zp.android.base.event

import com.zp.android.base.CtxUtil
import com.zp.android.base.R
import java.io.Serializable


/**
 * bus用到的订阅事件
 * Created by zhaopan on 2018/5/14.
 */

// 货币单位
enum class CurrencyUnit(name: String, val descId: Int){

    USD("USD",  R.string.base_currency_unit_usd),
    CNY("CNY", R.string.base_currency_unit_cny);

    val desc: String
        get() = CtxUtil.getString(descId)

    companion object {
        val default = USD
        fun valueOF(value: String): CurrencyUnit?{
            for (unit in  values()){
                if (unit.name.equals(value)) return unit
            }
            return null
        }

        fun isUSD(value: String):Boolean{
            return USD.name.equals(value)
        }

        //设定主货币单位的次货币单位.
        fun getSecondCurrency(unit: CurrencyUnit): CurrencyUnit{
            return when(unit){
                USD -> CNY
                CNY -> USD
            }
        }
    }

}
