package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams
import fe.megacmdkt.exception.MegaException

class MegaCmdRemove(val remotePath: String) : AbstractMegaCmdRunnerWithParams("rm") {
    private var isRecursivelyDeleted = false
    private var isErrorIgnoredIfAbsent = false

    override fun cmdParams() = buildList {
        add("-f")
        if (isRecursivelyDeleted) {
            add("-r")
        }

        add(remotePath)
    }

    override fun run() {
        try {
            super.run()
        } catch (ex: MegaException) {
            if (!isErrorIgnoredIfAbsent) {
                throw ex
            }
        }
    }

    fun deleteRecursively(): MegaCmdRemove {
        isRecursivelyDeleted = true
        return this
    }

    fun deleteRecursivelyDisabled(): MegaCmdRemove {
        isRecursivelyDeleted = false
        return this
    }

    fun ignoreErrorIfNotPresent(): MegaCmdRemove {
        isErrorIgnoredIfAbsent = true
        return this
    }

    fun reportErrorIfNotPresent(): MegaCmdRemove {
        isErrorIgnoredIfAbsent = false
        return this
    }
}
