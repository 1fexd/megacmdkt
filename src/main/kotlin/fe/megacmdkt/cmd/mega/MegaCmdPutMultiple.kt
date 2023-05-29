package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdPut
import fe.megacmdkt.exception.MegaWrongArgumentsException

class MegaCmdPutMultiple(var remotePath: String, vararg localFile: String) : AbstractMegaCmdPut<MegaCmdPutMultiple>() {
    val localFiles = localFile.toMutableList()

    fun setRemotePath(remotePath: String?): MegaCmdPutMultiple {
        if (remotePath != null) {
            this.remotePath = remotePath
        }

        return this
    }

    private fun cmdLocalFilesParams(): List<String> {
        if (localFiles.isEmpty()) {
            throw MegaWrongArgumentsException(
                "There are not local files specified!"
            )
        }

        return localFiles.toList()
    }

    override fun cmdFileParams() = buildList {
        addAll(localFiles)
        add(remotePath)
    }

    fun addLocalFileToUpload(filename: String) {
        localFiles.add(filename)
    }
}
