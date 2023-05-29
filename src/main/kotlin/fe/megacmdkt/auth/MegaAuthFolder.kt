package fe.megacmdkt.auth


import fe.megacmdkt.MegaSession
import fe.megacmdkt.cmd.mega.MegaCmdLogin
import fe.megacmdkt.exception.MegaLoginException

/**
 * Authenticates the users into MEGA just for an exported or public folder
 */
class MegaAuthFolder(private val folderPath: String) : MegaAuth() {

    @Throws(MegaLoginException::class)
    override fun login(): MegaSession? {
        return try {
            MegaCmdLogin(folderPath).run()
            MegaSession(this)
        } catch (cause: Throwable) {
            throw MegaLoginException(
                "You could not access to $folderPath", cause
            )
        }
    }
}
