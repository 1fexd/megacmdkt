package fe.megacmdkt.cmd.mega


import fe.megacmdkt.cmd.ImportInfo
import fe.megacmdkt.cmd.base.AbstractMegaCmdCallerWithParams
import fe.megacmdkt.exception.*
import java.io.IOException
import java.util.*

class MegaCmdImport(private val exportedLink: String) : AbstractMegaCmdCallerWithParams<ImportInfo>("import") {
    private var remotePath: String? = null
    private var password: String? = null

    fun setRemotePath(remotePath: String): MegaCmdImport {
        this.remotePath = remotePath
        return this
    }

    fun setPassword(password: String): MegaCmdImport {
        this.password = password
        return this
    }

    override fun cmdParams() = buildList {
        add(exportedLink)
        if (password != null) {
            add("--password=$password")
        }
        if (remotePath != null) {
            add(remotePath!!)
        } else {
            add("/")
        }
    }

    @Throws(Exception::class)
    override fun call(): ImportInfo {
        return try {
           execCmdWithSingleOutputOrNull()
                ?.let { ImportInfo.parseImportInfo(it) }
                ?: throw MegaInvalidResponseException("Invalid response while exporting '%s'", remotePath)
        } catch (e: IOException) {
            throw MegaIOException("Error while exporting $remotePath")
        }
    }
}
