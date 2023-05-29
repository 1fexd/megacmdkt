package fe.megacmdkt.cmd

import fe.megacmdkt.exception.MegaInvalidResponseException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.time.LocalDate
import java.time.format.DateTimeParseException

@DisplayName("Exported info")
@Tag("export")
class ExportInfoTest {
    @DisplayName("Parse export info with expire date and success")
    @Test
    fun parseExportWithExpireDateInfoShouldBeOk() {
        //Given
        val validExportInfo =
            "Exported /level1: https://mega.nz/folder/bmxnAJ6C#DWxI3_NL5SEpI1LFJ67b8w expires at Mon, 10 Aug 2020 09:06:40 +0200"

        //When
        val exportInfo = ExportInfo.parseExportInfo(validExportInfo)

        //Then
        Assertions.assertNotNull(exportInfo)
        Assertions.assertEquals("/level1", exportInfo.remotePath)
        Assertions.assertEquals("https://mega.nz/folder/bmxnAJ6C#DWxI3_NL5SEpI1LFJ67b8w", exportInfo.publicLink)
        Assertions.assertEquals(LocalDate.of(2020, 8, 10), exportInfo.expireDate!!)
    }

    @DisplayName("Parse export info with invalid message and failed")
    @Test
    fun parseExportWithInvalidMessageInfoShouldThrowException() {
        //Given
        val inValidExportInfo = "Error : unable to export the folder"

        //When
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ExportInfo.parseExportInfo(inValidExportInfo) })
    }

    @DisplayName("Parse export info with wrong date format and failed")
    @Test
    fun parseExportWithInvalidDateFormatShouldThrowException() {
        //Given
        val inValidExportInfo =
            "Exported /level1: https://mega.nz/folder/bmxnAJ6C#DWxI3_NL5SEpI1LFJ67b8w expires at 10 Aug 2020 09:06:40"

        //When
        Assertions.assertThrows<DateTimeParseException>(
            DateTimeParseException::class.java,
            Executable { ExportInfo.parseExportInfo(inValidExportInfo) })
    }

    @DisplayName("Parse export info and success")
    @Test
    fun parseExportInfoShouldBeOk() {
        //Given
        val validExportInfo = "Exported /megacmd4j/level2: https://mega.nz/#F!8OQxgYgY!xxxg0kyQ9wibextVq5FZbQ"

        //When
        val exportInfo = ExportInfo.parseExportInfo(validExportInfo)

        //Then
        Assertions.assertNotNull(exportInfo)
        Assertions.assertEquals("/megacmd4j/level2", exportInfo.remotePath)
        Assertions.assertEquals(
            "https://mega.nz/#F!8OQxgYgY!xxxg0kyQ9wibextVq5FZbQ",
            exportInfo.publicLink
        )
    }

    @DisplayName("Given invalid remotePath, when parse export info then fail")
    @Test
    fun failWhenParseInvalidExportInfo() {
        val entryWithInvalidRemotePath = "megacmd4j/level2(folder, shared as exported " +
                " folder link: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ExportInfo.parseExportListInfo(entryWithInvalidRemotePath) })
    }

    @DisplayName("Given invalid public link prefix, when parse export list info then fail")
    @Test
    fun failWhenParseLinkWithInvalidPrefix() {
        val entryWithInvalidRemotePath = "megacmd4j/level2 (folder, shared as exported " +
                " folder content: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ExportInfo.parseExportListInfo(entryWithInvalidRemotePath) })
    }

    @DisplayName("Given invalid public link ending, when parse export list info then fail")
    @Test
    fun failWhenParseExportListInfoWithInvalidPublicLinkEnding() {
        val entryWithInvalidRemotePath = "megacmd4j/level2 (folder, shared as exported " +
                " folder content: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ExportInfo.parseExportListInfo(entryWithInvalidRemotePath) })
    }

    @DisplayName("Given public link with invalid MEGA.nz url, when parse export list info then fail")
    @Test
    fun failWhenParseExportListInfoWithInvalidMegaPublicLink() {
        val responseWithInvalidMegaUrl = "megacmd4j (folder, shared as exported permanent " +
                "folder link: http://mega.com/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)"
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ExportInfo.parseExportListInfo(responseWithInvalidMegaUrl) })
    }

    @DisplayName("Given remote path with sub-paths, when parse export list info then success")
    @Test
    fun succeedWhenParseExportListInfoWithValidRemotePathWithSubPaths() {
        //Given
        val responseWithRemotePathWithSubPaths = "megacmd4j/level2/level3 (folder, shared as exported permanent " +
                "folder link: https://mega.nz/folder/xSx1ybja#cBos0Ly_71GXu6v-rKZXzg)"

        //When
        val exportInfo = ExportInfo.parseExportListInfo(responseWithRemotePathWithSubPaths)

        //Then
        Assertions.assertEquals("megacmd4j/level2/level3", exportInfo.remotePath)
        Assertions.assertEquals(
            "https://mega.nz/folder/xSx1ybja#cBos0Ly_71GXu6v-rKZXzg",
            exportInfo.publicLink
        )
    }

    @DisplayName("Given remote path with single folder, when parse export list info then success")
    @Test
    fun succeedWhenParseExportListInfoWithSingleFolder() {
        //Given
        val responseWithRemotePathWithSingleFolder = "megacmd4j (folder, shared as exported permanent " +
                "folder link: https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)"

        //When
        val exportInfo = ExportInfo.parseExportListInfo(responseWithRemotePathWithSingleFolder)

        //Then
        Assertions.assertEquals("megacmd4j", exportInfo.remotePath)
        Assertions.assertEquals(
            "https://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA",
            exportInfo.publicLink
        )
    }

    @DisplayName("Given public non-HTTPS link, when parse export list info then success")
    @Test
    fun succeedWhenParseListInfoWithNonSecureLink() {
        //Given
        val responseWithHttp = "megacmd4j (folder, shared as exported permanent " +
                "folder link: http://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA)"

        //When
        val exportInfo = ExportInfo.parseExportListInfo(responseWithHttp)

        //Then
        Assertions.assertEquals("megacmd4j", exportInfo.remotePath)
        Assertions.assertEquals(
            "http://mega.nz/#F!APJmCbiJ!lfKu3tVd8pNceLoH6qe_tA",
            exportInfo.publicLink
        )
    }
}
