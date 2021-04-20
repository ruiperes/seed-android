package io.ruiperes.seed.core.extension

import androidx.annotation.NonNull
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.log10

fun Int.length() = when(this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}
fun Int.between(startValue: Int, endValue: Int)  = this in startValue until endValue

fun Int.isEven()  = this % 2 == 0

fun Int.isOdd()  = this % 2 == 1

fun Double.formatWithDecimal(): String {

    val rounded = this

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    val decimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol = ""
    decimalFormatSymbols.decimalSeparator = '.'
    decimalFormatSymbols.patternSeparator = ','

    currencyFormat.decimalFormatSymbols = decimalFormatSymbols
    currencyFormat.maximumFractionDigits = 2
    currencyFormat.roundingMode = RoundingMode.DOWN

    val value = currencyFormat.format(rounded)
    return value.replace("\\s+".toRegex(), "")
}


fun Double.formatWithoutDecimal(): String {

    val rounded = this

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK)
    val decimalFormatSymbols = (currencyFormat as DecimalFormat).decimalFormatSymbols
    decimalFormatSymbols.currencySymbol = ""
    decimalFormatSymbols.patternSeparator = ','

    currencyFormat.decimalFormatSymbols = decimalFormatSymbols
    currencyFormat.maximumFractionDigits = 0
    currencyFormat.roundingMode = RoundingMode.DOWN

    val value = currencyFormat.format(rounded)
    return value.replace("\\s+".toRegex(), "")
}

fun removeNonNumeric(@NonNull numberString: String) : String {
    var numbers : String = ""
    for (i in numberString){
        if (i.isDigit())
            numbers += i
    }
    return numbers
}

fun getNewCursorPosition(digitCountToRightOfCursor : Int, numberString : String) : Int{
    var position = 0
    var c = digitCountToRightOfCursor
    for (i in numberString.reversed()) {
        if (c == 0)
            break

        if (i.isDigit())
            c --
        position ++


    }
    return numberString.length - position
}

fun getNumberOfDigits(@NonNull text : String) : Int{
    var count = 0
    for (i in text)
        if (i.isDigit())
            count++
    return count
}
