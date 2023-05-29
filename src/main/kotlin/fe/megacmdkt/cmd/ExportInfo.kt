package fe.megacmdkt.cmd


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.exception.MegaInvalidResponseException
import java.time.LocalDate

/**
 * Handles the information of an exported resource in MEGA
 */
class ExportInfo(val remotePath: String, val publicLink: String) {
    var expireDate: LocalDate? = null

    fun setExpireDate(expireDate: LocalDate): ExportInfo {
        this.expireDate = expireDate
        return this
    }

    companion object {
        private val LIST_REGEX = Regex(
            "(?<remotePath>\\S+) \\(.+link: (?<publicLink>http[s]?://mega.nz/\\S*#.+)\\)"
        )

        private val EXPORT_REGEX = Regex(
            "\\s(?<remotePath>\\S+)(:)\\s(?<publicLink>http[s]?://mega.nz/\\S*#\\S*)( expires at (?<expireDate>.+))?"
        )

        @JvmStatic
        fun parseExportInfo(exportInfoStr: String): ExportInfo {
            val matchResult = EXPORT_REGEX.find(exportInfoStr)
            if (matchResult != null) {
                val remotePath = matchResult.groups["remotePath"]!!.value
                val publicLink = matchResult.groups["publicLink"]!!.value
                val result = ExportInfo(remotePath, publicLink)

                val expireDate = matchResult.groups["expireDate"]
                if (expireDate != null) {
                    result.setExpireDate(MegaUtils.parseBasicISODate(expireDate.value))
                }

                return result
            }
            throw MegaInvalidResponseException("Unexpected export info format : $exportInfoStr")
        }

        @JvmStatic
        fun parseExportListInfo(exportInfoLine: String): ExportInfo {
            try {
                val matchResult = LIST_REGEX.find(exportInfoLine)
                if (matchResult != null) {
                    return ExportInfo(
                        matchResult.groups["remotePath"]!!.value,
                        matchResult.groups["publicLink"]!!.value
                    )
                }
            } catch (ex: IllegalStateException) {
                //Don't let it go outside
            } catch (_: IndexOutOfBoundsException) {
            }

            throw MegaInvalidResponseException(exportInfoLine)
        }
    }
}
