package fe.megacmdkt.cmd


import fe.megacmdkt.Mega
import fe.megacmdkt.MegaSession
import fe.megacmdkt.exception.MegaInvalidResponseException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.function.Executable

@DisplayName("Import")
@Tag("import")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ImportInfoTest {
    @Test
    fun given_invalid_remotePath_when_parseImportInfo_then_fail() {
        //Given
        val entryWithInvalidRemotePath = "Imported: /test"

        //When
        Assertions.assertThrows(
            MegaInvalidResponseException::class.java,
            Executable { ImportInfo.parseImportInfo(entryWithInvalidRemotePath) })
    }

    @Test
    fun given_valid_remotePath_when_parseImportInfo_then_success() {
        //Given
        val entryWithInvalidRemotePath = "Imported folder complete: /test"

        //When
        val result = ImportInfo.parseImportInfo(entryWithInvalidRemotePath)
        Assertions.assertEquals(result.remotePath, "/test")
    }

    @Test
    @Throws(Exception::class)
    fun given_remotePath_without_key_when_import_then_create_directory_NO_KEY() {
        //Given
        sessionMega!!.makeDirectory("megacmd4j/sampleDirToImportWithoutRemotePath")
            .recursively()
            .run()
        val exportInfo: ExportInfo = sessionMega!!.export("megacmd4j/sampleDirToImportWithoutRemotePath")
            .enablePublicLink()
            .call()

        //When
        val importInfo: ImportInfo = sessionMega!!.importLink(exportInfo.publicLink).call()

        //Then
        Assertions.assertEquals("/NO_KEY", importInfo.remotePath)

        //After
        sessionMega!!.removeDirectory("/NO_KEY")
            .ignoreErrorIfNotPresent()
            .run()
    }

    @Test
    @Throws(Exception::class)
    fun given_remotePath_destination_when_import_without_password_then_create_directory_NO_KEY() {
        //Given
        sessionMega!!.makeDirectory("megacmd4j/sampleDirToImportWithRemotePath")
            .recursively()
            .run()
        val exportInfo: ExportInfo = sessionMega!!.export("megacmd4j/sampleDirToImportWithRemotePath")
            .enablePublicLink()
            .call()

        //When
        val importInfo: ImportInfo = sessionMega!!.importLink(exportInfo.publicLink)
            .setRemotePath("megacmd4j/testImport/")
            .call()

        //Then
        Assertions.assertEquals("/megacmd4j/testImport/NO_KEY", importInfo.remotePath)
    }

    //TODO Get pro account
    @Throws(Exception::class)
    fun given_remotePath_destination_and_password_when_import_then_success() {
        //This requires Pro User

        //Given
        // /megacmd4j/folder3
        val responseWithRemotePath =
            "https://mega.nz/#P!AgDqMhBNisD3c8H98Uw6TNjW1lmgCDzxf_BoXc680RCJelSaQnOnFn-kgFiK21t0afxDJTp_8NW81Jl1JfX9ePp_34VzI9Z5aMTjQkLGMl9ePAdN-gidRg"
        val key = "cemotdepasseestvraimentvraimentrobuste"

        //When
        val importInfo: ImportInfo? =
            sessionMega!!.importLink(responseWithRemotePath)?.setRemotePath("/megacmd4j/testImport/")?.setPassword(key)
                ?.call()

        //Then
        Assertions.assertEquals("/megacmd4j/testImport/folder3", importInfo?.remotePath)
    }

    //TODO Get pro account
    @Throws(Exception::class)
    fun given_remotePath_password_when_import_then_success() {
        //Given
        // /megacmd4j/folder4
        val responseWithRemotePath =
            "https://mega.nz/#P!AgDuIHIAgtrWosqbzuTgQVBVYMnjlfZdiC3alPI_jXxQzn72-gA1kegotSG7hC8Sv8Ck1q9Nxkfv7F9_jSMTa6VbA8qAyZHOYohfJODN7DER3aGzxcE2XQ"
        val password = "cemotdepasseestvraimentvraimentrobuste"

        //When
        val importInfo: ImportInfo = sessionMega!!.importLink(responseWithRemotePath)
            .setPassword(password)
            .call()

        //Then
        Assertions.assertEquals("/folder4", importInfo.remotePath)

        //After
        sessionMega!!.removeDirectory("/folder4")
            .ignoreErrorIfNotPresent()
            .run()
    }

    companion object {
        private var sessionMega: MegaSession? = null

        @BeforeAll
        fun setupSession() {
            sessionMega = Mega.init()
            removeTestResourcesIfExist()
            sessionMega!!.makeDirectory("megacmd4j/testImport")
                .recursively()
                .run()
        }

        @AfterAll
        fun finishSession() {
            removeTestResourcesIfExist()
            sessionMega!!.logout()
        }

        private fun removeTestResourcesIfExist() {
            sessionMega!!.removeDirectory("megacmd4j")
                .ignoreErrorIfNotPresent()
                .run()
        }
    }
}
