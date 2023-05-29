package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdPathHandler

class MegaCmdGet(
    var remotePath: String,
    private var localPath: String? = null
) : AbstractMegaCmdPathHandler<MegaCmdGet>("get") {

    fun setRemotePath(remotePath: String): MegaCmdGet {
        this.remotePath = remotePath
        return this
    }

    fun setLocalPath(localPath: String): MegaCmdGet {
        this.localPath = localPath
        return this
    }

    fun useCurrentFolder(): MegaCmdGet {
        localPath = null
        return this
    }

    override fun cmdFileParams() = buildList {
        add(remotePath)
        if (localPath != null) {
            add(localPath!!)
        }
    }
}
