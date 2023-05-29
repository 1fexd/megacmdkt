package fe.megacmdkt.cmd


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.exception.MegaInvalidResponseException
import java.time.LocalDateTime

/**
 * Defines a remote file/Folder in MEGA
 *
 * @see FileStatus
 */
class FileInfo(
    val name: String,
    val date: LocalDateTime,
    val size: Long?,
    val version: Int?,
    status: FileStatus
) {

    val isFile = status.type == FileStatus.Type.FILE
    val isDirectory = status.type == FileStatus.Type.DIRECTORY

    companion object {
        private val FILE_INFO_REGEX = Regex("^(\\S{4})\\s+(\\S+)\\s+(-|\\d+)\\s(\\S+)\\s(\\S+)\\s(.+)$")

        @JvmStatic
        fun parseInfo(fileInfoStr: String): FileInfo {
            val matchResult = FILE_INFO_REGEX.find(fileInfoStr)
            try {
                if (matchResult != null) {
                    return parseFileInfo(matchResult)
                }
            } catch (ex: Exception) {
                val megaEx = MegaInvalidResponseException(
                    "Error while parsing file info from %s", fileInfoStr
                )
                megaEx.addSuppressed(ex)
                throw megaEx
            }
            throw MegaInvalidResponseException(fileInfoStr)
        }

        @JvmStatic
        fun isValid(fileInfoStr: String): Boolean {
            return try {
                FILE_INFO_REGEX.matches(fileInfoStr)
            } catch (ex: Exception) {
                false
            }
        }

        private fun parseFileInfo(matchResult: MatchResult): FileInfo {
            val filename = parseFileName(matchResult)
            val date = parseDate(matchResult)
            val sizeInBytes = parseSizeInBytes(matchResult)
            val version = parseVersion(matchResult)
            val fileStatus = parseFileType(matchResult)
            return FileInfo(filename, date, sizeInBytes, version, fileStatus)
        }

        private fun parseFileName(matchResult: MatchResult): String {
            return matchResult.groups[6]!!.value
        }

        private fun parseDate(matchResult: MatchResult): LocalDateTime {
            return MegaUtils.parseFileDate("${matchResult.groups[4]!!.value} ${matchResult.groups[5]!!.value}")
        }

        private fun parseSizeInBytes(matchResult: MatchResult): Long? {
            return runCatching { matchResult.groups[3]?.value?.toLongOrNull() }.getOrNull()
        }

        private fun parseVersion(matchResult: MatchResult): Int? {
            return runCatching { matchResult.groups[2]?.value?.toIntOrNull() }.getOrNull()
        }

        private fun parseFileType(matcher: MatchResult): FileStatus {
            return FileStatus.valueOf(matcher.groups[1]!!.value)
        }
    }
}
