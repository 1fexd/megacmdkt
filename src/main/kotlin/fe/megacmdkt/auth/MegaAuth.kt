package fe.megacmdkt.auth

import fe.megacmdkt.Mega
import fe.megacmdkt.MegaSession
import fe.megacmdkt.exception.MegaException
import fe.megacmdkt.exception.MegaLoginException

/**
 * Abstraction of authentication mechanisms, used to provide a strategy
 * of creating a MEGA session.
 */
abstract class MegaAuth {
    @Throws(MegaLoginException::class)
    abstract fun login(): MegaSession?

    companion object {
        fun createFromEnvVariables(): MegaAuthCredentials {
            val username = System.getenv(Mega.USERNAME_ENV_VAR) ?: throw MegaException.nonExistingEnvVariable(
                Mega.USERNAME_ENV_VAR
            )

            val password = System.getenv(Mega.PASSWORD_ENV_VAR) ?: throw MegaException.nonExistingEnvVariable(
                Mega.PASSWORD_ENV_VAR
            )

            return MegaAuthCredentials(username, password)
        }
    }
}
