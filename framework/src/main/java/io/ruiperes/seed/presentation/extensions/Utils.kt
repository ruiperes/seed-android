package io.ruiperes.seed.presentation.extensions

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*
import java.util.concurrent.ThreadLocalRandom

val EditText.string
    get() = this.text.toString()

/**
 * Extension function on any list that will return a list of unique random picks
 * from the list. If the specified number of elements you want is larger than the
 * number of elements in the list it returns null
 */
fun <E> List<E>.getRandomElements(numberOfElements: Int): List<E>? {
    if (numberOfElements > this.size) {
        return null
    }
    return this.shuffled().take(numberOfElements)
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Date.randomFromDateToDate(endDate: Date): Date {
    return Date(ThreadLocalRandom.current().nextLong(this.time, endDate.time))
}

fun ViewGroup.inflate(layoutResource: Int): View {
    return LayoutInflater.from(this.context).inflate(layoutResource, this, false)
}

fun String.toDrawableResource(context: Context): Int {
    return context.resources.getIdentifier(this, "drawable", context.packageName)
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

//fun AppCompatActivity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
//    this.applicationContext.toast(message, length)
//}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    this.context?.toast(message, length)
}

fun String.toHTML(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return SpannedString(this)
    }
}
