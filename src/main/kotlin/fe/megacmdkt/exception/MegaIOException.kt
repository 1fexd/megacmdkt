package fe.megacmdkt.exception

class MegaIOException @JvmOverloads constructor(message: String = "Error while connecting to Mega services") :
    MegaException(message) {
    constructor(message: String, vararg args: Any?) : this(String.format(message, *args))
}
