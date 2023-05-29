package fe.megacmdkt.cmd.base

import fe.megacmdkt.MegaUtils
import java.util.concurrent.Callable

abstract class AbstractMegaCmdCaller<T>(cmd: String) : AbstractMegaCmd(cmd), Callable<T> {
    open fun execCmdWithOutput() = MegaUtils.handleCmdWithOutput(cmd)
    open fun execCmdWithSingleOutputOrNull() = MegaUtils.execCmdWithSingleOutput(cmd)
}

abstract class AbstractMegaCmdCallerWithParams<T>(
    cmd: String
) : AbstractMegaCmdCaller<T>(cmd), AbstractMegaCmdWithParams {
    override fun execCmdWithOutput() = execCmdWithOutput(cmd)
    override fun execCmdWithSingleOutputOrNull() = execCmdWithSingleOutputOrNull(cmd)
}
