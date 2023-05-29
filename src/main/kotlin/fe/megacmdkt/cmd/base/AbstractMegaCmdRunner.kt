package fe.megacmdkt.cmd.base


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.exception.MegaIOException
import java.io.IOException

abstract class AbstractMegaCmdRunner(cmd: String) : AbstractMegaCmd(cmd), Runnable {
    override fun run() {
        tryExecuteSysCmd()
    }

    protected open fun tryExecuteSysCmd() {
        try {
            executeSysCmd()
        } catch (e: IOException) {
            throw MegaIOException()
        } catch (e: InterruptedException) {
            throw MegaIOException("The execution of %s couldn't be finished", cmd)
        }
    }

    @Throws(Exception::class)
    protected open fun executeSysCmd() {
        val result = MegaUtils.execCmd(cmd)
        MegaUtils.handleResult(result)
    }
}

abstract class AbstractMegaCmdRunnerWithParams(cmd: String) : AbstractMegaCmdRunner(cmd), AbstractMegaCmdWithParams {
    fun execCmdWithOutput() = execCmdWithOutput(cmd)
    fun execCmdWithSingleOutputOrNull() = execCmdWithSingleOutputOrNull(cmd)
}

