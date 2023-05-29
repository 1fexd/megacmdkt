package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdPut

class MegaCmdPutSingle(
    private val localFile: String,
    var remotePath: String? = null
) : AbstractMegaCmdPut<MegaCmdPutSingle>() {
    override fun cmdFileParams() = listOfNotNull(localFile, remotePath)

    fun setRemotePath(remotePath: String): MegaCmdPutSingle {
        this.remotePath = remotePath
        return this
    }
}
