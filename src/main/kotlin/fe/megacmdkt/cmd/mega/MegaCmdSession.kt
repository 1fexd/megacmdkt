package fe.megacmdkt.cmd.mega


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.cmd.base.AbstractMegaCmdCaller
import fe.megacmdkt.exception.MegaIOException
import fe.megacmdkt.exception.MegaLoginRequiredException
import java.io.IOException

class MegaCmdSession : AbstractMegaCmdCaller<String>("session") {
    override fun call(): String {
        return try {
            execCmdWithSingleOutputOrNull()?.let { parseSessionID(it) } ?: throw MegaLoginRequiredException()
        } catch (e: IOException) {
            throw MegaIOException()
        }
    }

    companion object {
        fun parseSessionID(response: String) = MegaUtils.parseMegaValue(response)
    }
}
