package fe.megacmdkt.auth

import fe.megacmdkt.MegaSession
import fe.megacmdkt.cmd.mega.MegaCmdLogin
import fe.megacmdkt.exception.MegaException
import fe.megacmdkt.exception.MegaLoginException

/**
 * Logs into MEGA with an email/username and password combination
 */
class MegaAuthCredentials(private val username: String, private val password: String) : MegaAuth() {
    override fun login(): MegaSession? {
        return try {
            MegaCmdLogin(username, password).run()
            MegaSession(this)
        } catch (ex: MegaException) {
            throw ex
        } catch (err: Throwable) {
            throw MegaLoginException("Invalid username or password", err)
        }
    }
}
