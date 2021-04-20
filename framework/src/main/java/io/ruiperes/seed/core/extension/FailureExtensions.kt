package io.ruiperes.seed.core.extension

import io.ruiperes.seed.core.exceptions.Failure
import io.ruiperes.seed.core.funcional.Either

fun Failure.toLeft() = Either.Left(this)
