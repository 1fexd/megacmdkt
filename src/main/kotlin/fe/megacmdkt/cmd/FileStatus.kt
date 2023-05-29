package fe.megacmdkt.cmd

import fe.megacmdkt.exception.MegaInvalidResponseException

/**
 * Defines the status of the remote file/folder in MEGA
 *
 * @see FileInfo
 */
class FileStatus(
    val type: Type,
    val isExported: Boolean,
    val isExportedPermanent: Boolean,
    val isExportedTemporal: Boolean,
    val sharingStatus: SharingStatus
) {

    enum class Type(var value: Char) {
        DIRECTORY('d'),
        FILE('-'),
        ROOT('r'),
        INBOX('i'),
        RUBBISH('b'),
        UNSUPORTED('x');

        companion object {
            @Throws(IllegalArgumentException::class)
            fun valueOf(c: Char): Type {
                for (t in values()) {
                    if (t.value == c) {
                        return t
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }

    enum class SharingStatus(var value: Char) {
        SHARED('s'),
        IN_SHARE('I'),
        NOT_SHARED('-');

        companion object {
            @Throws(IllegalArgumentException::class)
            fun valueOf(c: Char): SharingStatus {
                for (t in values()) {
                    if (t.value == c) {
                        return t
                    }
                }
                throw IllegalArgumentException()
            }
        }
    }

    companion object {
        fun valueOf(statusStr: String): FileStatus {
            if (statusStr.length != 4) {
                throw MegaInvalidResponseException(
                    "'%s' dont contains valid flags for a file"
                )
            }
            val type = parseType(statusStr[0])
            val isExported = isExported(statusStr[1])
            val isExportedPermanent = isExportedPermanent(statusStr[2])
            val isExportedTemporal = isExportedTemporal(statusStr[2])
            val sharingStatus = parseSharingStatus(statusStr[3])

            return FileStatus(
                type, isExported, isExportedPermanent,
                isExportedTemporal, sharingStatus
            )
        }

        private fun parseType(c1st: Char): Type {
            return try {
                Type.valueOf(c1st)
            } catch (ex: IllegalArgumentException) {
                throw MegaInvalidResponseException(
                    "'%c' is not a valid file type", c1st
                )
            }
        }

        private fun isExported(c2nd: Char): Boolean {
            return c2nd == 'e'
        }

        private fun isExportedPermanent(c3rd: Char): Boolean {
            return c3rd == 'p'
        }

        private fun isExportedTemporal(c3rd: Char): Boolean {
            return c3rd == 't'
        }

        private fun parseSharingStatus(c4th: Char): SharingStatus {
            return try {
                SharingStatus.valueOf(c4th)
            } catch (ex: IllegalArgumentException) {
                throw MegaInvalidResponseException(
                    "'%c' is not a valid sharing status", c4th
                )
            }
        }
    }
}
