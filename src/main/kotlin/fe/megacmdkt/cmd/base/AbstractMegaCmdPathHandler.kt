@file:Suppress("UNCHECKED_CAST")

package fe.megacmdkt.cmd.base

abstract class AbstractMegaCmdPathHandler<T : AbstractMegaCmdPathHandler<T>>(cmd: String) :
    AbstractMegaCmdRunnerWithParams(cmd) {
    private var isRemotePathCreatedIfNotPresent = false
    private var isUploadQueued = false
    private var isQuotaWarningIgnored = true

    override fun cmdParams() = buildList {
        if (isRemotePathCreatedIfNotPresent) {
            add("-c")
        }
        if (isUploadQueued) {
            add("-q")
        }
        if (isQuotaWarningIgnored) {
            add("--ignore-quota-warn")
        }
        addAll(cmdFileParams())
    }

    fun createRemotePathIfNotPresent(): T {
        isRemotePathCreatedIfNotPresent = true
        return this as T
    }

    fun skipIfRemotePathNotPresent(): T {
        isRemotePathCreatedIfNotPresent = false
        return this as T
    }

    fun queueUpload(): T {
        isUploadQueued = true
        return this as T
    }

    fun waitToUpload(): T {
        isUploadQueued = false
        return this as T
    }

    fun ignoreQuotaSurpassingWarning(): T {
        isQuotaWarningIgnored = true
        return this as T
    }

    fun warnQuotaSurpassing(): T {
        isQuotaWarningIgnored = false
        return this as T
    }

    protected abstract fun cmdFileParams(): List<String>
}
