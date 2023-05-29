package fe.megacmdkt.platform

import java.util.*

/**
 * Abstractions to encapsulate Operative System/Platforms specific actions.
 * E.g.: Running the MEGAcmd commands.
 */
enum class OSPlatform(val cmd: String) {
    UnixPlatform("mega-%s"),
    WindowsPlatform("cmd.exe /c MegaClient %s");

    fun cmdInstruction(parameter: String) = cmd.format(parameter)

    companion object {
        val current by lazy {
            if (System.getProperty("os.name").lowercase(Locale.getDefault()).startsWith("windows")) {
                WindowsPlatform
            } else UnixPlatform
        }
    }
}


