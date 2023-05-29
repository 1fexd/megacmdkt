package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams

open class MegaCmdShare(val remotePath: String?, private val username: String?) : AbstractMegaCmdRunnerWithParams("share") {
    private var isShared = true
    private var accessLevel = AccessLevel.READ_ONLY

    override fun cmdParams() = buildList {
        add(if (isShared) "-a" else "-d")
        if (username != null) {
            add("--with=$username")
        }

        if (isShared) {
            add("--level=$accessLevel")
        }
        if (remotePath != null) {
            add(remotePath)
        }
    }

    fun startSharing(): MegaCmdShare {
        isShared = true
        return this
    }

    fun stopSharing(): MegaCmdShare {
        isShared = true
        return this
    }

    fun grantReadOnlyAccess(): MegaCmdShare {
        accessLevel = AccessLevel.READ_ONLY
        return this
    }

    fun grantReadAndWriteAccess(): MegaCmdShare {
        accessLevel = AccessLevel.READ_WRITE
        return this
    }

    fun grantFullAccess(): MegaCmdShare {
        accessLevel = AccessLevel.FULL
        return this
    }

    fun grantOwnerAccess(): MegaCmdShare {
        accessLevel = AccessLevel.OWNER
        return this
    }

    protected enum class AccessLevel(private val id: Char) {
        READ_ONLY('0'),
        READ_WRITE('1'),
        FULL('2'),
        OWNER('3');

        override fun toString(): String {
            return id.toString()
        }
    }
}
