package fe.megacmdkt.cmd.base

import fe.megacmdkt.MegaUtils

interface AbstractMegaCmdWithParams {
    fun executableCommand(cmd: String) = buildList {
        addAll(MegaUtils.toCmdInstruction(cmd))
        addAll(cmdParams())
    }

    fun cmdParams(): List<String>

    fun execCmdWithOutput(cmd: String) = MegaUtils.handleCmdWithOutput(executableCommand(cmd))
    fun execCmdWithSingleOutputOrNull(cmd: String) = MegaUtils.execCmdWithSingleOutput(executableCommand(cmd))
}
