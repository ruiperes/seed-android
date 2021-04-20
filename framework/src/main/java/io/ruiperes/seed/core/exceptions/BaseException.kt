package io.ruiperes.seed.core.exceptions

/**
 * Exceptions should only be used until Use Cases
 */
sealed class BaseException(val code: ExceptionCode) : Exception() {
    object IoRemoteException : BaseException(ExceptionCode.Network)
    object IoLocalException : BaseException(ExceptionCode.Local)
    object IoMemoryException : BaseException(ExceptionCode.Local)

    object ThreadProviderNotFoundException : BaseException(ExceptionCode.ThreadProvider)

    open class FeatureException(code: ExceptionCode) : BaseException(code)
}
