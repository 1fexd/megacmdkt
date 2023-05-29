package fe.megacmdkt

import fe.megacmdkt.MegaUtils.execCmd
import fe.megacmdkt.exception.MegaUnexpectedFailureException

object MegaServer {
    fun start() {
        try {
            execCmd("help")
        } catch (e: Exception) {
            throw MegaUnexpectedFailureException()
        }
    }

    fun stop() = Mega.quit()
}
