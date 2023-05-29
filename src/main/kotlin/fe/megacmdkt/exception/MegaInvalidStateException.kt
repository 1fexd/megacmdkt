package fe.megacmdkt.exception

class MegaInvalidStateException : MegaException {
    constructor() : super("Invalid state")
    constructor(message: String) : super(message)
}
