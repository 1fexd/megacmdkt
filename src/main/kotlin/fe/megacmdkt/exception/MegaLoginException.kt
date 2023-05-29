package fe.megacmdkt.exception

class MegaLoginException : MegaException {
    constructor(errorMessage: String) : super(errorMessage)
    constructor(errorMessage: String, detailedError: Throwable?) : super(errorMessage, detailedError)
}
