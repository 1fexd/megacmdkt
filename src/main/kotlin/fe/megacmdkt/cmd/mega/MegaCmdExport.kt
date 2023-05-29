package fe.megacmdkt.cmd.mega


import fe.megacmdkt.cmd.ExportInfo
import fe.megacmdkt.cmd.TimeDelay
import fe.megacmdkt.cmd.base.AbstractMegaCmdCallerWithParams
import fe.megacmdkt.exception.MegaIOException
import fe.megacmdkt.exception.MegaInvalidResponseException
import java.io.IOException
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

open class MegaCmdExport(val remotePath: String?) : AbstractMegaCmdCallerWithParams<ExportInfo?>("export") {
    private var password: String? = null

    private var isExportDeleted = false
    private var listOnly = false

    var expirationTimeDelay: TimeDelay? = null


    override fun cmdParams() = buildList {
        if (!listOnly) {
            add(if (isExportDeleted) "-d" else "-a")
            if (expirationTimeDelay != null) {
                add("--expire=$expirationTimeDelay")
            }

            add("-f")
        }

        if (password != null) {
            add("--password=$password")
        }

        if (remotePath != null) {
            add(remotePath)
        }
    }


    override fun call(): ExportInfo {
        return try {
            execCmdWithSingleOutputOrNull()
                ?.let { ExportInfo.parseExportInfo(it) }
                ?: throw MegaInvalidResponseException("Invalid response while exporting '%s'", remotePath)
        } catch (e: IOException) {
            throw MegaIOException("Error while exporting $remotePath")
        }
    }

    fun list(): List<ExportInfo> {
        justList()
        return try {
            execCmdWithOutput().map { ExportInfo.parseExportListInfo(it) }
        } catch (e: IOException) {
            throw MegaIOException("Error while exporting $remotePath")
        }
    }

    fun enablePublicLink(): MegaCmdExport {
        isExportDeleted = false
        return this
    }

    fun removePublicLink(): MegaCmdExport {
        isExportDeleted = true
        return this
    }

    fun setPassword(password: String): MegaCmdExport {
        this.password = password
        return this
    }

    fun removePassword(): MegaCmdExport {
        password = null
        return this
    }

    fun setExpirationTimeDelay(expirationTimeDelay: TimeDelay): MegaCmdExport {
        this.expirationTimeDelay = expirationTimeDelay
        return this
    }

    fun setExpireDate(endDateTimeExclusive: LocalDateTime): MegaCmdExport {
        val period = Period.between(LocalDate.now(), endDateTimeExclusive.toLocalDate())
        val duration = Duration.between(LocalDateTime.now(), endDateTimeExclusive)
        return setExpirationTimeDelay(TimeDelay.of(period, duration))
    }

    fun setExpireDate(date: LocalDate?): MegaCmdExport {
        val period = Period.between(LocalDate.now(), date)
        return setExpirationTimeDelay(TimeDelay.of(period))
    }

    fun withoutExpiration(): MegaCmdExport {
        expirationTimeDelay = null
        return this
    }

    private fun justList(): MegaCmdExport {
        listOnly = true
        return this
    }
}
