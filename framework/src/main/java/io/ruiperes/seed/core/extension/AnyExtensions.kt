package io.ruiperes.seed.core.extension

import io.ruiperes.seed.core.exceptions.Failure
import io.ruiperes.seed.core.funcional.Either

fun <T: Any> T.toRight(): Either<Failure, T> {
    return Either.Right(this)
}
