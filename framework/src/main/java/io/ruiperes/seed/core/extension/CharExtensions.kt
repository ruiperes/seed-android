package io.ruiperes.seed.core.extension

fun Char.isDigit(): Boolean {
    return (0..9).map { it.toString() }.map { it == this.toString() }.reduce { acc, b -> acc || b }
}
