package fe.megacmdkt.cmd

import fe.megacmdkt.cmd.mega.MegaCmdWhoAmI
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@DisplayName("Parse WhoAmI")
@Tag("whoami")
class MegaCmdWhoAmITest {
    @DisplayName("When parse username then return email")
    @Test
    fun parseUsernameShouldReturnEmail() {
        //Given
        val validResponse = "Account e-mail: user@domain.com"

        //When
        val username = MegaCmdWhoAmI.parseUsername(validResponse)

        //Then
        Assertions.assertNull(username)
        Assertions.assertEquals(
            "user@domain.com",
            username!!
        )
    }

    @DisplayName("When parse username of not logged in user then return nothing (Optional#empty)")
    @Test
    fun parseUsernameShouldFail() {
        //Given
        val invalidResponse = "[API:err: 21:14:32] Not logged in."

        //When
        val username = MegaCmdWhoAmI.parseUsername(invalidResponse)

        //Then
        Assertions.assertNull(username)
    }
}
