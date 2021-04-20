package io.ruiperes.seed.presentation.extensions

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T> RecyclerView.Adapter<*>.autoNotify(oldList: List<T>, newList: List<T>, compare: (T, T) -> Boolean) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    })
    diff.dispatchUpdatesTo(this)
}

fun <K, T> RecyclerView.Adapter<*>.autoNotify(oldMap: Map<K, List<T>>, newMap: Map<K, List<T>>, compare: (T, T) -> Boolean) {

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compare(oldMap.flatMap { it.value }[oldItemPosition], newMap.flatMap { it.value }[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMap.flatMap { it.value }[oldItemPosition] ==  newMap.flatMap { it.value }[newItemPosition]
        }

        override fun getOldListSize() =  oldMap.flatMap { it.value }.size
        override fun getNewListSize() =  newMap.flatMap { it.value }.size
    })
    diff.dispatchUpdatesTo(this)
}
