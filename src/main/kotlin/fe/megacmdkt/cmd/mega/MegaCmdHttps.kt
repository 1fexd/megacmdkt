package fe.megacmdkt.cmd.mega


import fe.megacmdkt.cmd.base.AbstractMegaCmdCallerWithParams
import fe.megacmdkt.exception.MegaIOException
import fe.megacmdkt.exception.MegaInvalidResponseException
import java.io.IOException

/**
 * Shows if HTTPS is used for transfers. Use [.MegaCmdHttps] to
 * enable or disable HTTPS for transfers or [MegaCmdHttps] constructor
 * just to query the current state. Use [.call] to see the result.
 */
class MegaCmdHttps(private val enable: Boolean? = null) : AbstractMegaCmdCallerWithParams<Boolean?>("https") {

    private fun parseResponseToHttpsEnabled(response: String): Boolean? {
        if (response.endsWith("HTTPS")) {
            return true
        }
        return if (response.endsWith("HTTP")) {
            false
        } else null
    }

    override fun call(): Boolean {
        return try {
            execCmdWithSingleOutputOrNull()?.let {
                parseResponseToHttpsEnabled(it)
            } ?: throw MegaInvalidResponseException("Invalid HTTP state in the response")
        } catch (ex: IOException) {
            throw MegaIOException()
        }
    }

    override fun cmdParams() = buildList {
        add(if (enable == true) "on" else "off")
    }
}
