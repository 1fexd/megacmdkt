package fe.megacmdkt.cmd


import fe.megacmdkt.MegaUtils
import fe.megacmdkt.exception.MegaInvalidResponseException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.*

@DisplayName("Parse FileInfo")
@Tag("ls")
class FileInfoTest {
    @DisplayName("Parse FileInfo from string: ----    1         50 04May2018 17:54:11 yolo-2.txt")
    @Test
    fun givenFileWhenValueOfThenShouldBeOk() {
        //Given
        val fileInfoStr = "----    1         50 04May2018 17:54:11 yolo-2.txt"

        //When
        val fileInfo = FileInfo.parseInfo(fileInfoStr)

        //Then
        Assertions.assertNotNull(fileInfo)
        Assertions.assertTrue(fileInfo.isFile)
        assertEquals("yolo-2.txt", fileInfo.name)
        assertEquals(
            MegaUtils.parseFileDate("04May2018 17:54:11"),
            fileInfo.date
        )
        assertEquals(Optional.of(50L), fileInfo.size)
        assertEquals(Optional.of(1), fileInfo.version)
    }

    @DisplayName("Parse FileInfo from string: d---    -          - 31Jan2018 00:25:12 megacmd4j")
    @Test
    fun givenDirectoryWhenValueOfThenShouldBeOk() {
        //Given
        val fileInfoStr = "d---    -          - 31Jan2018 00:25:12 megacmd4j"

        //When
        val fileInfo = FileInfo.parseInfo(fileInfoStr)

        //Then
        Assertions.assertNotNull(fileInfo)
        Assertions.assertTrue(fileInfo.isDirectory)
        assertEquals("megacmd4j", fileInfo.name)
        assertEquals(
            MegaUtils.parseFileDate("31Jan2018 00:25:12"),
            fileInfo.date
        )
        Assertions.assertNull(fileInfo.size)
        Assertions.assertNull(fileInfo.version)
    }

    @DisplayName("Parse FileInfo from string: ----    -         50 04May2018 17:54:11 yolo-2.txt")
    @Test
    fun ifNoVersionItShouldBeOk() {
        //Given
        val fileInfoStr = "----    -         50 04May2018 17:54:11 yolo-2.txt"

        //When
        val fileInfo = FileInfo.parseInfo(fileInfoStr)

        //Then
        Assertions.assertNotNull(fileInfo)
        Assertions.assertNull(fileInfo.version)
    }

    @DisplayName("Parse FileInfo from string: ----    1         - 04May2018 17:54:11 yolo-2.txt")
    @Test
    fun ifNoSizeItShouldBeOk() {
        //Given
        val fileInfoStr = "----    1         - 04May2018 17:54:11 yolo-2.txt"

        //When
        val fileInfo = FileInfo.parseInfo(fileInfoStr)

        //Then
        Assertions.assertNotNull(fileInfo)
        Assertions.assertNull(fileInfo.size)
    }

    @DisplayName("Parse FileInfo from string should fail: ----    1         50 04May20xx 17:54:11 yolo-2.txt")
    @Test
    fun valueOfShouldFailIfDateIsIncorrect() {
        val fileInfoStr = "----    1         50 04May20xx 17:54:11 yolo-2.txt"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java
        ) { FileInfo.parseInfo(fileInfoStr) }
    }

    @DisplayName("Parse FileInfo from string should fail: ----    1         50 04May20xx 17:54:11")
    @Test
    fun valueOfShouldFailIfSizeOfResponseIsNot6() {
        val fileInfoStr = "----    1         50 04May20xx 17:54:11"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java
        ) { FileInfo.parseInfo(fileInfoStr) }
    }

    @DisplayName("Parse token of FileInfo where name has spaces")
    @Test
    fun parseTokenWithFileWithSpacesShouldBeOk() {
        val fileInfoString = "d---    -          - 29Jun2021 21:01:04 level 3 with spaces"
        val fileInfo = FileInfo.parseInfo(fileInfoString)
        assertEquals(fileInfo.name, "level 3 with spaces")
    }

    @DisplayName("----   22         29 24Apr2022 19:11:00 test123.txt  should be valid")
    @Test
    fun txtFileWithoutSpaceShouldBeValid() {
        Assertions.assertTrue(FileInfo.isValid("----   22         29 24Apr2022 19:11:00 test123.txt"))
    }

    @DisplayName("d---    -          - 29Jun2021 23:15:48 MegaCMD4J test workplace should be valid")
    @Test
    fun emptyFolderShouldBeValid() {
        Assertions.assertTrue(FileInfo.isValid("d---    -          - 29Jun2021 23:15:48 MegaCMD4J test workplace"))
    }
}
