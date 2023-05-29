package fe.megacmdkt.exception

class MegaUnexpectedFailureException : MegaException {
    constructor(code: Int) : super("Unexpected failure with code %s", code)
    constructor() : super("Unexpected failure catched by MEGA")
}
