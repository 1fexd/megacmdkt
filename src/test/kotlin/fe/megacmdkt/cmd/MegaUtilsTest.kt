package fe.megacmdkt.cmd


import fe.megacmdkt.MegaUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeParseException
import java.util.*

@DisplayName("MegaUtils")
class MegaUtilsTest {
    @DisplayName("MegaUtils#parseFileDate well formed datetime like 04May2018 17:54:11")
    @Test
    fun parseFileDateWithTimeShouldBeOk() {
        //Given
        val dateStr = "04May2018 17:54:11"

        //When
        val date: LocalDateTime = MegaUtils.parseFileDate(dateStr)

        //Then
        Assertions.assertNotNull(date)
        Assertions.assertEquals(4, date.dayOfMonth)
        Assertions.assertEquals(Month.MAY, date.month)
        Assertions.assertEquals(2018, date.year)
        Assertions.assertEquals(17, date.hour)
        Assertions.assertEquals(54, date.minute)
        Assertions.assertEquals(11, date.second)
    }

    @DisplayName("MegaUtils#parseFileDate malformed datetime like 29Jun2021")
    @Test
    fun parseFileDateWithoutTimeShouldFail() {
        Assertions.assertThrows<DateTimeParseException>(
            DateTimeParseException::class.java,
            Executable { MegaUtils.parseFileDate("29Jun2021") })
    }

    @get:Test
    @get:DisplayName("MegaUtils#isEmail")
    val isEmailShouldBeOk: Unit
        get() {
            Assertions.assertTrue(MegaUtils.isEmail("user@doma.in"))
            Assertions.assertTrue(MegaUtils.isEmail("eliecerhdz@gmail.com"))
            Assertions.assertTrue(MegaUtils.isEmail("a@b.xx"))
        }

    @get:Test
    @get:DisplayName("MegaUtils#isEmailShouldFail")
    val isEmailShouldFail: Unit
        get() {
            Assertions.assertFalse(MegaUtils.isEmail("/user@doma.in"))
            Assertions.assertFalse(MegaUtils.isEmail("eliecerhdz*@gmail.com"))
            Assertions.assertFalse(MegaUtils.isEmail("a@b.toolong"))
        }

    @get:Test
    @get:DisplayName("MegaUtils#isDirectoryShouldBeOk")
    val isDirectoryShouldBeOk: Unit
        get() {
            Assertions.assertTrue(MegaUtils.isDirectory("/path"))
            Assertions.assertTrue(MegaUtils.isDirectory("/path/subpath"))
            Assertions.assertTrue(MegaUtils.isDirectory("subpath"))
        }

    @get:Test
    @get:DisplayName("MegaUtils#isDirectoryShouldFail")
    val isDirectoryShouldFail: Unit
        get() {
            Assertions.assertFalse(MegaUtils.isDirectory("user@doma.in"))
            //TODO Improve
        }

    @DisplayName("MegaUtils#collectValidCmdOutput should not accept output with banner")
    @Test
    fun collectValidCmdOutputShouldNotAcceptOutputWithStoreBanner() {
        val OUTPUT_WITH_RUNNING_OUT_OF_STORAGE_BANNER =
            """-------------------------------------------------------------------------------
|                   You are running out of available storage.                   |
|        You can change your account plan to increase your quota limit.         |
|                   See "help --upgrade" for further details.                 |
--------------------------------------------------------------------------------
MEGAcmd version: 1.3.0.0: code 1030000
MEGA SDK version: 3.7.0
"""
        val inputScanner = Scanner(OUTPUT_WITH_RUNNING_OUT_OF_STORAGE_BANNER)
            .useDelimiter("\n")
        val result: List<String> = MegaUtils.collectValidCmdOutput(inputScanner)
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("MEGAcmd version: 1.3.0.0: code 1030000", result[0])
        Assertions.assertEquals("MEGA SDK version: 3.7.0", result[1])
    }

    @DisplayName("MegaUtils#collectValidCmdOutput should not trim alike content when banner is over")
    @Test
    fun collectValidCmdOutputShouldNotTrimAlikeContentWhenBannerIsOver() {
        val CONFUSING_BANNER = """-------------------------------------------------------------------------------
|                   You are running out of available storage.                   |
|        You can change your account plan to increase your quota limit.         |
|                   See "help --upgrade" for further details.                 |
--------------------------------------------------------------------------------
These are the results
|----------------------------|
|asd | vvvvv | zzzzz   | tttt|
|----------------------------|
"""
        val inputScanner = Scanner(CONFUSING_BANNER)
            .useDelimiter("\n")
        val result: List<String> = MegaUtils.collectValidCmdOutput(inputScanner)
        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals("These are the results", result[0])
        Assertions.assertEquals("|----------------------------|", result[1])
        Assertions.assertEquals("|asd | vvvvv | zzzzz   | tttt|", result[2])
        Assertions.assertEquals("|----------------------------|", result[3])
    }
}
