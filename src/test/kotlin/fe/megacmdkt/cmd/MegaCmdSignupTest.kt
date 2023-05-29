package fe.megacmdkt.cmd

import fe.megacmdkt.Mega
import fe.megacmdkt.exception.MegaWrongArgumentsException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS

@DisplayName("Sign up")
@Tag("cmd")
@Tag("signup")
class MegaCmdSignupTest {
    @DisplayName("Sign up without using a name")
    @Test
    fun shouldSignUpWithoutUsingName() {
        Mega.signup("noreply@gmail.com", "anypassword").run()
    }

    @DisplayName("Sign up using a name")
    @Test
    fun shouldSignUpUsingName() {
        Mega.signup("noreply@gmail.com", "anypassword")
            .setName("Test User")
            .run()
    }

    @DisplayName("When sign up using an empty name then throw MegaWrongArgumentsException")
    @Test
    fun givenEmptyUserWhenSignupThenThrowWrongArgumentsException() {
        assertThrows(
            MegaWrongArgumentsException::class.java
        ) { Mega.signup("", "anypassword").run() }
    }

    @DisplayName("When sign up using an empty password then throw MegaWrongArgumentsException")
    @EnabledOnOs(OS.WINDOWS)
    @Test
    fun givenEmptyPassword_whenSignupThenItShouldWork() {
        assertThrows(
            MegaWrongArgumentsException::class.java,
            { Mega.signup("noreply@gmail.com", "").run() },
            "It should not allow an empty password"
        )
    }
}
