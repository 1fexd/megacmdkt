package fe.megacmdkt.cmd.mega


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.cmd.base.AbstractMegaCmdCallerWithParams
import fe.megacmdkt.exception.MegaInvalidResponseException
import fe.megacmdkt.exception.MegaUnexpectedFailureException

class MegaCmdVersion(
    private var isExtendedInfoShown: Boolean = false
) : AbstractMegaCmdCallerWithParams<MegaCmdVersion.MegaCmdVersionResponse>("version") {

    fun showExtendedInfo(): MegaCmdVersion {
        isExtendedInfoShown = true
        return this
    }

    fun hideExtendedInfo(): MegaCmdVersion {
        isExtendedInfoShown = false
        return this
    }

    override fun cmdParams() = if (isExtendedInfoShown) listOf("-l") else emptyList()

    override fun call(): MegaCmdVersionResponse {
        return try {
            val versionDataLines: List<String> = execCmdWithOutput()
            val version = parseCmdVersion(versionDataLines[0])
            if (isExtendedInfoShown) {
                val sdkVersion = MegaUtils.parseMegaValue(versionDataLines[1])!!
                val sdkCredits = MegaUtils.parseMegaValue(versionDataLines[2])!!
                val sdkLicense = MegaUtils.parseMegaValue(versionDataLines[3])!!
                val license = MegaUtils.parseMegaValue(versionDataLines[4])!!
                val features = parseFeaturesEnabled(versionDataLines)

                return MegaCmdVersionExtendedResponse(
                    version, sdkVersion, sdkCredits, sdkLicense, license, features
                )
            }
            version
        } catch (e: Exception) {
            throw MegaUnexpectedFailureException()
        }
    }

    open class MegaCmdVersionResponse {
        val version: String
        val versionCode: String

        protected constructor(version: MegaCmdVersionResponse) {
            this.version = version.version
            versionCode = version.versionCode
        }

        constructor(version: String, versionCode: String) {
            this.version = version
            this.versionCode = versionCode
        }
    }

    class MegaCmdVersionExtendedResponse(
        version: MegaCmdVersionResponse,
        val sdkVersion: String, val sdkCredits: String, val sdkLicense: String,
        val license: String, val features: List<String>
    ) : MegaCmdVersionResponse(version)

    companion object {
        private val cmdVersionRegex = Regex("(.+) version: (?<version>.+): code (?<versionCode>.+)")

        fun parseCmdVersion(
            versionResponse: String
        ): MegaCmdVersionResponse {
            val matchResult = cmdVersionRegex.find(versionResponse)


            return if (matchResult != null) {
                MegaCmdVersionResponse(
                    matchResult.groups["version"]!!.value,
                    matchResult.groups["versionCode"]!!.value
                )
            } else {
                throw MegaInvalidResponseException("Unexpected MEGAcmd version")
            }
        }

        fun parseFeaturesEnabled(versionDataLines: List<String>): List<String> {
            val featuresHeader = versionDataLines.indexOf("Features enabled:")
            val featuresOffset = featuresHeader + 1
            return if (versionDataLines.size > featuresOffset) {
                versionDataLines
                    .asSequence()
                    .drop(featuresOffset)
                    .map { it.substring(2) }
                    .toList()
            } else {
                emptyList()
            }
        }
    }
}
