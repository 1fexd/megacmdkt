package fe.megacmdkt.cmd.mega


import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams
import fe.megacmdkt.exception.MegaLoginException
import java.io.IOException

class MegaCmdLogin(vararg val cmdParams: String) : AbstractMegaCmdRunnerWithParams("login") {

    override fun tryExecuteSysCmd() {
        try {
            executeSysCmd()
        } catch (e: IOException) {
            throw MegaLoginException("Error while executing login command in Mega")
        } catch (e: InterruptedException) {
            throw MegaLoginException("The login was interrupted")
        }
    }

    override fun cmdParams() = cmdParams.toList()
}
