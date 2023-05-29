package fe.megacmdkt

import fe.megacmdkt.exception.*
import fe.megacmdkt.platform.OSPlatform
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object MegaUtils {
    fun parseMegaValue(value: String) = value.split(": ").getOrNull(1)?.trim()

    fun parseFileDate(dateStr: String): LocalDateTime {
        return LocalDateTime.parse(dateStr, MEGA_FILE_DATE_TIME_FORMATTER)
    }

    fun parseBasicISODate(dateStr: String): LocalDate {
        return LocalDateTime.parse(dateStr, MEGA_EXPORT_EXPIRE_DATE_FORMATTER)
            .toLocalDate()
    }

    fun handleResult(code: Int?) {
        when (code?.let { abs(it) } ?: -1) {
            0 -> {}
            51, 2 -> throw MegaWrongArgumentsException()
            12 -> throw MegaResourceAlreadyExistsException()
            52 -> throw MegaInvalidEmailException()
            53 -> throw MegaResourceNotFoundException()
            54 -> throw MegaInvalidStateException()
            55 -> throw MegaInvalidTypeException()
            56 -> throw MegaOperationNotAllowedException()
            57 -> throw MegaLoginRequiredException()
            58 -> throw MegaNodesNotFetchedException()
            59 -> throw MegaUnexpectedFailureException()
            60 -> throw MegaConfirmationRequiredException()
            else -> throw MegaUnexpectedFailureException(code!!)
        }
    }

    fun toCmdInstruction(cmd: String) = OSPlatform.current.cmdInstruction(cmd).split("\\s+")

    @Throws(IOException::class, InterruptedException::class)
    fun execCmd(cmd: String) = execCmd(toCmdInstruction(cmd))

    @Throws(IOException::class, InterruptedException::class)
    fun execCmd(fullCmd: Array<String>): Int {
        val process = Runtime.getRuntime().exec(fullCmd)
        val succeeded = process.waitFor(MEGA_TTL.toLong(), TimeUnit.MILLISECONDS)

        return if (succeeded) process.exitValue() else -1
    }

    @Throws(IOException::class, InterruptedException::class)
    fun execCmd(fullCmd: List<String>) = execCmd(fullCmd.toTypedArray())

    @Throws(IOException::class)
    fun handleCmdWithOutput(cmd: String) = handleCmdWithOutput(toCmdInstruction(cmd))

    @Throws(IOException::class)
    fun handleCmdWithOutput(fullCmd: List<String>) = handleCmdWithOutput(ProcessBuilder(fullCmd), fullCmd)


    private fun handleCmdWithOutput(processBuilder: ProcessBuilder, cmd: Any?): List<String> {
        val process = processBuilder.apply {
            redirectErrorStream(true)
        }.start()

        val inputScanner = Scanner(process.inputStream).useDelimiter(System.getProperty("line.separator"))
        val result = collectValidCmdOutput(inputScanner)
        process.destroy()

        try {
            handleResult(process.waitFor())
        } catch (ex: InterruptedException) {
            throw MegaIOException("The execution of '%s' was interrupted", cmd)
        }

        return result
    }

    @Throws(IOException::class)
    fun execCmdWithSingleOutput(cmd: String) = execCmdWithSingleOutput(toCmdInstruction(cmd))

    @Throws(IOException::class)
    fun execCmdWithSingleOutput(cmd: List<String>): String? {
        return runCatching { handleCmdWithOutput(cmd)[0] }.getOrNull()
    }

    fun isEmail(token: String): Boolean {
        return EMAIL_REGEX.find(token) != null
    }

    fun isDirectory(token: String): Boolean {
        return !isEmail(token) && DIRECTORY_REGEX.find(token) != null
    }

    fun collectValidCmdOutput(inputScanner: Scanner): List<String> {
        val result = mutableListOf<String>()
        runCatching {
            inputScanner.skip(BANNER_REGEX.toPattern())
        }

        while (inputScanner.hasNext()) {
            result.add(inputScanner.next())
        }

        return result
    }

    private val MEGA_FILE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMMyyyy HH:mm:ss", Locale.US)
    private val MEGA_EXPORT_EXPIRE_DATE_FORMATTER = DateTimeFormatter.ofPattern(
        "EEE, dd MMM yyyy HH:mm:ss Z", Locale.US
    )

    private val EMAIL_REGEX = Regex(
        "^[\\w]+@[\\w]+\\.[a-zA-Z]{2,6}$",
        RegexOption.IGNORE_CASE
    )

    private val DIRECTORY_REGEX = Regex(
        "[\\/]?[\\p{Alnum}]+(\\/[\\p{Alnum}]+)*",
        RegexOption.IGNORE_CASE
    )

    private val MEGA_TTL = System.getenv(Mega.CMD_TTL_ENV_VAR)?.toIntOrNull() ?: 20000

    private val BANNER_REGEX = Regex(
        "^\\s?(-){5,}(.+)(-){5,}\\R",
        setOf(RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )
}
