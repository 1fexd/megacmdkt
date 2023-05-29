package fe.megacmdkt.exception

class MegaInvalidResponseException : MegaException {
    constructor(cmdName: String?) : super("$cmdName returned an invalid response")
    constructor(errorMessage: String, vararg args: Any?) : super(errorMessage, *args)
}
