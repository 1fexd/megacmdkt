package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams

class MegaCmdMove(
    private val sourceRemotePath: String,
    private val remoteTarget: String
) : AbstractMegaCmdRunnerWithParams("mv") {

    override fun cmdParams() = listOf(sourceRemotePath, remoteTarget)
}
