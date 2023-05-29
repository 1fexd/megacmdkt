package fe.megacmdkt.cmd.mega

import fe.megacmdkt.cmd.base.AbstractMegaCmdRunnerWithParams

class MegaCmdSignup(
    private val username: String,
    private val password: String,
    private var name: String? = null,
) : AbstractMegaCmdRunnerWithParams("signup") {

    override fun cmdParams() = buildList {
        add(username)
        add(password)
        if (name != null) {
            add("--name=$name")
        }
    }

    fun setName(name: String): MegaCmdSignup {
        this.name = name
        return this
    }
}
