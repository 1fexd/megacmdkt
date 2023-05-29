package fe.megacmdkt.cmd

import fe.megacmdkt.MegaUtils
import fe.megacmdkt.exception.MegaInvalidResponseException

class ImportInfo(var remotePath: String) {

    companion object {
        @JvmStatic
        fun parseImportInfo(importInfoStr: String): ImportInfo {
            try {
                if (importInfoStr.contains("Imported folder complete:")) {
                    return ImportInfo(MegaUtils.parseMegaValue(importInfoStr)!!)
                }
            } catch (e: Exception) {
                throw MegaInvalidResponseException(e.message)
            }

            throw MegaInvalidResponseException(importInfoStr)
        }
    }
}
