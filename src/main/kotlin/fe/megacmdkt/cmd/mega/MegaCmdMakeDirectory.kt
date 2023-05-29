package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams
import fe.megacmdkt.exception.MegaWrongArgumentsException

class MegaCmdMakeDirectory(val remotePath: String) : AbstractMegaCmdRunnerWithParams("mkdir") {
    private var isRecursively = false
    private var isErrorIgnoredIfExists = false

    override fun cmdParams() = buildList {
        if (isRecursively) {
            add("-p")
        }

        add(remotePath)
    }

    fun recursively(): MegaCmdMakeDirectory {
        isRecursively = true
        return this
    }

    fun notRecursively(): MegaCmdMakeDirectory {
        isRecursively = false
        return this
    }

    fun ignoreErrorIfExists(): MegaCmdMakeDirectory {
        isErrorIgnoredIfExists = true
        return this
    }

    fun throwErrorIfExists(): MegaCmdMakeDirectory {
        isErrorIgnoredIfExists = false
        return this
    }

    override fun run() {
        try {
            super.run()
        } catch (ex: MegaWrongArgumentsException) {
            if (!isErrorIgnoredIfExists) {
                throw ex
            }
        }
    }
}
