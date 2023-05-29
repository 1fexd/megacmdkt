package fe.megacmdkt.auth

import fe.megacmdkt.MegaSession
import fe.megacmdkt.exception.MegaLoginException
import java.util.*

/**
 * Logs into MEGA using a session ID
 */
class MegaAuthSessionID(val sessionID: String) : MegaAuth() {

    @Throws(MegaLoginException::class)
    override fun login(): MegaSession? {
        return try {
            MegaSession(this)
        } catch (err: Throwable) {
            throw MegaLoginException("There is no started session", err)
        }
    }

}
