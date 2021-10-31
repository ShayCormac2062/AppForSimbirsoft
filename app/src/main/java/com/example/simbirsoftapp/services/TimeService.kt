package com.example.simbirsoftapp.services

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.applandeo.materialcalendarview.EventDay
import java.util.*

class TimeService {

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMonthFromTime(day: EventDay): String {
        val time = convertToTimeMonthDay(day.calendar.timeInMillis)
        val dayOfMonth = "${time[3]}${time[4]}"
        val month = when("${time[0]}${time[1]}") {
            "01" -> "января"
            "02" -> "февраля"
            "03" -> "марта"
            "04" -> "апреля"
            "05" -> "мая"
            "06" -> "июня"
            "07" -> "июля"
            "08" -> "августа"
            "09" -> "сентября"
            "10" -> "октября"
            "11" -> "ноября"
            "12" -> "декабря"
            else -> ""
        }
        return "$dayOfMonth $month"
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun convertToTimeMonthDay(time: Long?): String {
        time?.let {
            val date = Date(it)
            val format = SimpleDateFormat("MM:dd")
            return format.format(date)
        }
        return ""
    }

    fun getDayTime(eventDay: EventDay?): Long {
        eventDay?.let {
            return it.calendar.timeInMillis
        }
        return 0
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun convertToTime(time: Long?): String {
        time?.let {
            val date = Date(it)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return format.format(date)
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    fun convertToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun convertToTimeHourMinute(time: Long?): String {
        time?.let {
            val date = Date(it)
            val format = SimpleDateFormat("HH:mm")
            return format.format(date)
        }
        return ""
    }
}