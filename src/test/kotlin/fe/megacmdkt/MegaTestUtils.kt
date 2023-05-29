package fe.megacmdkt

import fe.megacmdkt.exception.MegaIOException
import java.io.*
import java.util.function.IntConsumer
import java.util.stream.IntStream

interface MegaTestUtils {
    companion object {
        fun testTextFileName(namePrefix: String, suffix: Any): String {
            val suffixStr = if (EMPTY_SUFFIX_ENTRIES.contains(suffix.toString())) "" else "-$suffix"
            return String.format("target/%s%s.txt", namePrefix, suffixStr)
        }

        fun createTextFiles(namePrefix: String, numberOfFiles: Int) {
            IntStream.rangeClosed(1, numberOfFiles).forEach { i: Int ->
                createTextFile(
                    testTextFileName(namePrefix, i),
                    "Lorem ipsum dolor...",
                    "This is the content of file #$i"
                )
            }
        }

        fun createTextFile(filename: String, vararg content: String) {
            try {
                File(filename).parentFile.mkdirs()
                BufferedWriter(
                    OutputStreamWriter(
                        FileOutputStream(filename), "utf-8"
                    )
                ).use { writer ->
                    for (line in content) {
                        writer.write(line)
                    }
                }
            } catch (ex: IOException) {
                throw MegaIOException(
                    "Unexpected error while creating file %s: %s",
                    filename, ex.message
                )
            }
        }

        fun removeTextFiles(namePrefix: String, numberOfFiles: Int) {
            IntStream.rangeClosed(1, numberOfFiles).forEach(
                IntConsumer { i: Int -> removeFile(testTextFileName(namePrefix, i)) }
            )
        }

        fun removeFile(filename: String) {
            File(filename).deleteOnExit()
        }

        val EMPTY_SUFFIX_ENTRIES: List<String> = mutableListOf("0", "1", "")
    }
}
