package fe.megacmdkt.cmd

import fe.megacmdkt.cmd.mega.MegaCmdSession
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@DisplayName("Sessions")
@Tag("session")
class MegaCmdSessionTest {
    @DisplayName("When parse valid session id then return expected value")
    @Test
    fun parseSessionIDShouldBeOk() {
        //Given
        val validResponse =
            "Your (secret) session is: Ae9r6XXXqUZGhXEIUoy7C85XhPq9vOAr2Sc94axXXXX-T3JZZE9kOEt3dDjWGMscV2il65Zo-mFMEXXX"

        //When
        val id = MegaCmdSession.parseSessionID(validResponse)

        //Then
        Assertions.assertNotNull(id, "No id was found")
        Assertions.assertEquals(
            "Ae9r6XXXqUZGhXEIUoy7C85XhPq9vOAr2Sc94axXXXX-T3JZZE9kOEt3dDjWGMscV2il65Zo-mFMEXXX",
            id!!,
            "The found id has not the expected value"
        )
    }

    @DisplayName("When parse session id for a not logged in user then return nothing (Optional#empty)")
    @Test
    fun parseSessionIDShouldFail() {
        //Given
        val invalidResponse = "[API:err: 21:14:32] Not logged in."

        //When
        val noId = MegaCmdSession.parseSessionID(invalidResponse)

        //Then
        Assertions.assertNull(noId, "Unexpected session id")
    }
}
