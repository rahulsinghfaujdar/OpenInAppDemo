package com.openinapp.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.highsoft.highcharts.common.hichartsclasses.HINavigation
import com.highsoft.highcharts.core.HIChartView
import com.openinapp.datamodel.ResDashboard
import java.io.IOException
import java.util.Calendar

object CommonUtil {

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hour in 0 until 12 -> "Good morning"
            hour in 12 until 16 -> "Good afternoon"
            else -> "Good evening"
        }
    }

    fun createRoundedCornerDrawableWithOutline(
        context: Context,
        cornerRadius: Float,
        backgroundColor: Int,
        outlineColor: Int,
        outlineWidth: Int
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = cornerRadius
        drawable.setColor(ContextCompat.getColor(context, backgroundColor))
        drawable.setStroke(outlineWidth, ContextCompat.getColor(context, outlineColor))
        return drawable
    }

    fun loadDataFromJsonAsset(context: Context,jsonName:String):ResDashboard{
        var jsonString: String = ""
        try {
            jsonString = context.assets.open("$jsonName.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        //val listCountryType = object : TypeToken<List<ResDashboard>>() {}.type
        val listCountryType = object : TypeToken<ResDashboard>() {}.type
        return Gson().fromJson(jsonString,listCountryType)
    }
}