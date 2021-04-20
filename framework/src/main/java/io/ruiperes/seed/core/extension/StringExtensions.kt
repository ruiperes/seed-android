package io.ruiperes.seed.core.extension

import android.util.Patterns
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.math.BigInteger
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun String.Companion.empty() = ""

fun String?.or(orString: String): String = this ?: orString

fun String?.isValidColor(): Boolean {
    if (this == null)
        return false
    val hexPattern = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$"
    val pattern = Pattern.compile(hexPattern)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}


fun String.maskNumber(mask: String): String {
    try {
        var index = 0
        val masked = StringBuilder()
        for (element in mask) {
            val c = element
            if (c == '#') {
                masked.append(this[index])
                index++
            } else if (c == 'x' || c == 'X') {
                masked.append(c)
                index++
            } else {
                masked.append(c)
            }
        }
        return masked.toString()
    } catch (e: Exception) {
        return this
    }
}

fun String?.isValidUrl(): Boolean {
    if (this == null)
        return false
    return "(?i)^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]-*)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$".toRegex()
        .matches(this)
}

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.toDate(): Date? {
    return try {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        format.isLenient = false
        format.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.toDateTime(): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        format.isLenient = false
        format.parse(this)
    } catch (e : Exception) {
        null
    }
}

fun String.MRZtoDate(): Date? {
    return try {
        val format = SimpleDateFormat("yyMMdd", Locale.ENGLISH)
        format.isLenient = false
        format.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.parseToBigDecimal(): BigDecimal? {
    var formatted = this

    if (!Regex("([-+])?([,.0-9])*\\d").matches(formatted))
        return null

    formatted = if (formatted.contains(".")) {
        if (formatted.count { c -> c == '.' } > 1) {
            formatted.replace(".", "")
        } else {
            formatted
        }
    } else {
        formatted
    }

    formatted = if (formatted.contains(",")) {
        if (formatted.count { c -> c == ',' } > 1) {
            formatted.replace(",", "")
        } else {
            formatted
        }
    } else {
        formatted
    }

    val last = formatted.lastIndexOfAny(listOf(",", "."))

    try {

        if (last != -1) {
            formatted = if (formatted[last] == ',') {
                formatted.replace(".", "")
            } else {
                formatted.replace(",", "")
            }
        }


        val locale = if (last != -1) {
            if (formatted[formatted.lastIndexOfAny(listOf(",", "."))] == ',') {
                Locale.GERMANY
            } else {
                Locale.US
            }
        } else {
            Locale.GERMANY
        }

        val df = NumberFormat.getInstance(locale) as DecimalFormat
        df.currency = Currency.getInstance("EUR")
        df.maximumFractionDigits = 2
        df.minimumFractionDigits = 2
        df.roundingMode = RoundingMode.DOWN
        df.isParseBigDecimal = true
        val r = df.parseObject(formatted) as BigDecimal
        return r
    } catch (e: ParseException) {
        e.printStackTrace()
        return null
    }
}

fun BigInteger.toFormattedString(): String? {
    val formatted = NumberFormat.getInstance(Locale.UK)
    formatted.currency = Currency.getInstance("EUR")
    formatted.maximumFractionDigits = 0
    formatted.minimumFractionDigits = 0
    formatted.roundingMode = RoundingMode.DOWN
    return try {
        formatted.format(this)
    } catch (e: Exception) {
        null
    }
}

fun BigDecimal.toFormattedString(): String? {
    val formatted = NumberFormat.getInstance(Locale.UK) as DecimalFormat
    formatted.currency = Currency.getInstance("EUR")
    formatted.maximumFractionDigits = 2
    formatted.minimumFractionDigits = 2
    formatted.roundingMode = RoundingMode.DOWN
    formatted.isParseBigDecimal = true
    return try {
        formatted.format(this)
    } catch (e: Exception) {
        null
    }
}

fun String.toBigIntegerFromString(): BigInteger? {
    val formatted = NumberFormat.getInstance(Locale.UK)
    formatted.currency = Currency.getInstance("EUR")
    formatted.maximumFractionDigits = 0
    formatted.minimumFractionDigits = 0
    formatted.roundingMode = RoundingMode.DOWN
    return try {
        (formatted.parseObject(this) as Long).toBigInteger()
    } catch (e: Exception) {
        null
    }
}

fun String.toBigDecimalFromString(): BigDecimal? {
    val formatted = NumberFormat.getInstance(Locale.UK) as DecimalFormat
    formatted.currency = Currency.getInstance("EUR")
    formatted.maximumFractionDigits = 2
    formatted.minimumFractionDigits = 2
    formatted.roundingMode = RoundingMode.DOWN
    formatted.isParseBigDecimal = true
    return try {
        formatted.parseObject(this) as BigDecimal
    } catch (e: Exception) {
        null
    }
}
