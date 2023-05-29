package fe.megacmdkt.cmd

import fe.megacmdkt.exception.MegaException

@org.junit.jupiter.api.DisplayName("MEGAcmd Common Commands")
class MegaCmdCommonsTest : AbstractRemoteTests() {
    @org.junit.jupiter.api.DisplayName("Session should exist")
    @org.junit.jupiter.api.Test
    fun sessionShouldExist() {
        org.junit.jupiter.api.Assertions.assertNotNull(sessionMega.sessionID())
    }

    @org.junit.jupiter.api.DisplayName("Retrieve username (WhoAmI)")
    @org.junit.jupiter.api.Test
    fun shouldReturnUsername() {
        org.junit.jupiter.api.Assertions.assertNotNull(sessionMega.whoAmI())
    }

    @org.junit.jupiter.api.DisplayName("When change password to empty string then fail")
    @org.junit.jupiter.api.Test
    fun emptyPasswordWhenChangePasswordThenFail() {
        val currentPassword = System.getenv(fe.megacmdkt.Mega.PASSWORD_ENV_VAR)
        org.junit.jupiter.api.Assertions.assertTimeout(
            java.time.Duration.ofMinutes(30)
        ) {
            org.junit.jupiter.api.Assertions.assertThrows(
                MegaException::class.java,
                org.junit.jupiter.api.function.Executable { sessionMega.changePassword(currentPassword, "  ") })
        }
    }
}
