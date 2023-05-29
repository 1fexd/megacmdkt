package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams

class MegaCmdChangePassword(
    private val oldPassword: String,
    private val newPassword: String
) : AbstractMegaCmdRunnerWithParams("passwd") {
    private var authCode: String? = null

    override fun cmdParams() = buildList {
        add(oldPassword)
        add("-f")
        if (authCode != null) {
            add("--auth-code=$authCode")
        }

        add(newPassword)
    }

    fun setAuthCode(authCode: String): MegaCmdChangePassword {
        this.authCode = authCode
        return this
    }

    fun removeAuthCode(): MegaCmdChangePassword {
        authCode = null
        return this
    }
}
