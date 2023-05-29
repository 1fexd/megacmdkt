package fe.megacmdkt.cmd.mega


import fe.megacmdkt.cmd.FileInfo
import fe.megacmdkt.cmd.base.AbstractMegaCmdCallerWithParams
import fe.megacmdkt.exception.*
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class MegaCmdList(val remotePath: String = "") : AbstractMegaCmdCallerWithParams<List<FileInfo?>?>("ls") {
    override fun cmdParams() = listOf("-l", "--time-format=SHORT", remotePath)

    override fun call(): List<FileInfo> {
        //mega-cmd seems to print a line with the directory name before the usual "FLAGS VERS    SIZE            DATE       NAME"
        // line when the path contains a space/or is a shared incoming directory, or if ANY file contains a space (handled by FileInfo::isValid below)
        val skipNLines = if (remotePath.contains(" ") || INCOMING_SHARE_PATTERN.matcher(remotePath).matches()) 2 else 1
        return try {
            execCmdWithOutput()
                .asSequence().drop(skipNLines)
                .filter { FileInfo.isValid(it) }.map { FileInfo.parseInfo(it) }.toList()
        } catch (e: IOException) {
            throw MegaIOException("Error while listing $remotePath")
        }
    }

    fun filter(predicate: (FileInfo) -> Boolean): List<FileInfo> {
        return try {
            execCmdWithOutput()
                .asSequence().drop(1) // The first one is not valid
                .filter { FileInfo.isValid(it) }.map { FileInfo.parseInfo(it) }
                .filter(predicate).toList()
        } catch (e: IOException) {
            throw MegaIOException("Error while listing $remotePath")
        }
    }

    @Throws(MegaResourceNotFoundException::class)
    fun count(): Int {
        return try {
            execCmdWithOutput()
                .asSequence()
                .filter { FileInfo.isValid(it) }
                .count()
        } catch (e: IOException) {
            throw MegaIOException("Error while listing $remotePath")
        }
    }

    @Throws(MegaResourceNotFoundException::class)
    fun count(predicate: (FileInfo) -> Boolean): Int {
        return try {
            execCmdWithOutput()
                .asSequence()
                .filter { FileInfo.isValid(it) }.map { FileInfo.parseInfo(it) }
                .filter(predicate)
                .count()
        } catch (e: IOException) {
            throw MegaIOException("Error while listing $remotePath")
        }
    }

    fun exists(): Boolean {
        return try {
            execCmdWithOutput()
            true
        } catch (e: MegaException) {
            false
        } catch (e: IOException) {
            throw MegaIOException("Error while listing $remotePath")
        }
    }

    companion object {
        //my@email.com:dir1/dir2
        private val INCOMING_SHARE_PATTERN = Pattern.compile("^.*@.*:.*$")
    }
}
