package io.ruiperes.seed.core.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.isWeekend(): Boolean {
    val cal = Calendar.getInstance().apply {
        time = this@isWeekend
    }

    return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
}

fun Date.isFriday(): Boolean {
    val cal = Calendar.getInstance().apply {
        time = this@isFriday
    }

    return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
}

fun Date.addDays(days: Int): Date {
    val cal = Calendar.getInstance().apply {
        time = this@addDays
    }

    cal.add(Calendar.DATE, days)
    return cal.time
}

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Date.randomFromDateToDate(endDate: Date): Date {
    return Date(kotlin.random.Random.nextLong(this.time, endDate.time))
}

fun Long.toDate(): Date = Date(this)
fun Date.toLong(): Long = this.time

fun Long.toDateWithHour(hour: Int): Date {
    val cal = this.toDate().toCalendar()
    cal.set(Calendar.HOUR_OF_DAY, hour)
    return cal.time
}

fun Date.age(): Int {

    val dob = this.toCalendar()
    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}

fun Long?.or(defaultValue: Long = 0): Long = this ?: defaultValue

fun String?.parseDate(): Date? {
    return try {
        val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        dateParser.parse(this!!)
    } catch (e: Exception) {
        null
    }

}

fun String?.parseDateWithMilliseconds(): Date? {
    return try {
        val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        dateParser.parse(this!!)
    } catch (e: Exception) {
        null
    }
}

fun Date?.toSimpleString(): String? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        return format.format(this!!)
    } catch (e: Exception) {
        null
    }
}

fun Date?.toSimpleDateString(): String {
    if (this == null)
        return ""
    return try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        format.format(this!!)
    } catch (e: Exception) {
        throw e
    }
}

fun Date?.toString(): String = this.toSimpleDateString()

fun lastMonday(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun lastMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun lastYear(): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_YEAR, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun thisWeekLastDay(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun thisMonthLastDay(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun thisYearLastDay(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val date = calendar.time.toSimpleString()
    return date!!
}

fun String.add(field: Int, amount: Int) : String {
    val date = this.toDateTime()!!
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    calendar.add(field, amount)
    val newDate = calendar.time.toSimpleString()
    return newDate!!
}

fun String.lastDayOfMonth() : String {
    val date = this.toDateTime()!!
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    val newDate = calendar.time.toSimpleString()
    return newDate!!
}

fun String.isFuture() : Boolean {
    val date = this.toDateTime()!!
    return date > Calendar.getInstance().time
}
