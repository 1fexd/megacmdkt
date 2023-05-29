package fe.megacmdkt

import fe.megacmdkt.auth.MegaAuth
import fe.megacmdkt.auth.MegaAuthSessionID
import fe.megacmdkt.cmd.mega.MegaCmdQuit
import fe.megacmdkt.cmd.mega.MegaCmdSession
import fe.megacmdkt.cmd.mega.MegaCmdSignup
import fe.megacmdkt.cmd.mega.MegaCmdVersion
import fe.megacmdkt.exception.MegaException

interface Mega {
    companion object {
        fun envVars(): Array<String> {
            return arrayOf("PATH=" + System.getenv("PATH"))
        }

        fun init(): MegaSession? {
            return try {
                MegaServer.start()
                currentSession()
            } catch (e: MegaException) {
                e.printStackTrace()
                login(MegaAuth.createFromEnvVariables())
            }
        }

        fun login(credentials: MegaAuth): MegaSession? {
            return credentials.login()
        }

        fun currentSession(): MegaSession? {
            val sessionID: String = MegaCmdSession().call()
            return login(MegaAuthSessionID(sessionID))
        }

        fun signup(username: String, password: String): MegaCmdSignup {
            return MegaCmdSignup(username, password)
        }

        fun quit() {
            MegaCmdQuit().run()
        }

        fun version(): MegaCmdVersion {
            return MegaCmdVersion()
        }

        const val USERNAME_ENV_VAR = "MEGA_EMAIL"
        const val PASSWORD_ENV_VAR = "MEGA_PWD"
        const val CMD_TTL_ENV_VAR = "MEGA_CMD_TTL"
    }
}
