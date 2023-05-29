package fe.megacmdkt.cmd.mega


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.cmd.base.AbstractMegaCmdCaller
import fe.megacmdkt.exception.MegaIOException
import fe.megacmdkt.exception.MegaLoginRequiredException
import java.io.IOException

class MegaCmdWhoAmI : AbstractMegaCmdCaller<String>("whoami") {
    override fun call(): String {
        return try {
            execCmdWithSingleOutputOrNull()?.let { parseUsername(it) } ?: throw MegaLoginRequiredException()
        } catch (e: IOException) {
            throw MegaIOException()
        }
    }

    companion object {
        fun parseUsername(response: String) = MegaUtils.parseMegaValue(response)
    }
}
