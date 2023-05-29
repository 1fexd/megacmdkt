package fe.megacmdkt.exception

/**
 * Defines expected errors based on behaviours of failure defined by MEGA or the library itself.
 */
open class MegaException : RuntimeException {
    constructor(errorMessage: String) : super(errorMessage)
    constructor(errorMessage: String, vararg args: Any?) : super(String.format(errorMessage, *args))
    constructor(errorMessage: String, errCause: Throwable?) : super(errorMessage, errCause)

    companion object {
        fun nonExistingEnvVariable(envVarName: String): MegaException {
            return MegaException(
                "You must define the variable %s in your environment",
                envVarName
            )
        }
    }
}
