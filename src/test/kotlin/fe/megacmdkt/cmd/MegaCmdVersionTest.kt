package fe.megacmdkt.cmd

import fe.megacmdkt.cmd.mega.MegaCmdVersion
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@DisplayName("Parse MEGAcmd version")
@Tag("cmd")
@Tag("version")
class MegaCmdVersionTest {
    @DisplayName("Get valid version id")
    @Test
    fun shouldReturnValidVersion() {
        val megaCmdVersion: MegaCmdVersion.MegaCmdVersionResponse = fe.megacmdkt.Mega.version().call()
        Assertions.assertFalse(
            megaCmdVersion.version == null
                    || megaCmdVersion.version.isEmpty(),
            "No MEGAcmd version was found"
        )
    }

    @DisplayName("Parse MEGAcmd features from the version output")
    @Test
    fun shouldReturnExpectedFeaturesEnabled() {
        val versionResponse: List<String> = mutableListOf(
            "MEGAcmd version: 1.1.0: code 1010000",
            "MEGA SDK version: 3.3.5",
            "etc",
            "Features enabled:",
            "* SQLite",
            "* FreeImage",
            "* Feature3"
        )
        val features = MegaCmdVersion.parseFeaturesEnabled(versionResponse)
        Assertions.assertEquals("SQLite", features[0])
        Assertions.assertEquals("FreeImage", features[1])
        Assertions.assertEquals("Feature3", features[2])
    }

    @DisplayName("Parse extended version output")
    @Test
    fun shouldReturnExtendedVersion() {
        val version: MegaCmdVersion.MegaCmdVersionResponse = fe.megacmdkt.Mega.version().showExtendedInfo().call()
        Assertions.assertTrue(
            version is MegaCmdVersion.MegaCmdVersionExtendedResponse,
            "Is not an extended response"
        )
        val extendedVersion: MegaCmdVersion.MegaCmdVersionExtendedResponse =
            version as MegaCmdVersion.MegaCmdVersionExtendedResponse
        Assertions.assertFalse(
            extendedVersion.version == null
                    || extendedVersion.version.isEmpty(),
            "Invalid MEGAcmd version"
        )
        Assertions.assertTrue(
            extendedVersion.versionCode.matches("\\d{3,}".toRegex()),
            "version code had not 3 characters"
        )
        Assertions.assertTrue(
            extendedVersion.sdkVersion.matches(".{1,3}\\..{1,3}\\..{1,3}".toRegex()),
            "The SDK version had not the expected format"
        )
        Assertions.assertEquals(
            "https://github.com/meganz/sdk/blob/master/CREDITS.md",
            extendedVersion.sdkCredits
        )
        Assertions.assertEquals(
            "https://github.com/meganz/sdk/blob/master/LICENSE",
            extendedVersion.sdkLicense
        )
        Assertions.assertEquals(
            "https://github.com/meganz/megacmd/blob/master/LICENSE",
            extendedVersion.license
        )
        Assertions.assertNotEquals(
            0,
            extendedVersion.features.size,
            "MEGAcmd has no features enabled"
        )
        Assertions.assertFalse(
            extendedVersion.features.get(0).startsWith("*"),
            "MEGAcmd features starts with a prefix: *"
        )
    }
}
