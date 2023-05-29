package fe.megacmdkt.cmd.mega


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams

class MegaCmdCopy(
    private val sourceRemotePath: String,
    private val remoteTarget: String
) : AbstractMegaCmdRunnerWithParams("cp") {

    override fun cmdParams() = listOf(sourceRemotePath, remoteTarget)

    val isRemoteTargetAUser = MegaUtils.isEmail(remoteTarget)
    val isRemoteTargetADirectory = MegaUtils.isDirectory(remoteTarget)
}
