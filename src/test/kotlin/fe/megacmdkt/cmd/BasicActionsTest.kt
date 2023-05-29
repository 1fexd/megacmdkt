package fe.megacmdkt.cmd

import fe.megacmdkt.MegaTestUtils
import fe.megacmdkt.exception.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.function.Executable
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("MEGAcmd Basic Commands")
@Tag("cmd")
@Tag("integration")
@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class
)
class BasicActionsTest : AbstractRemoteTests() {
    @DisplayName("Enable HTTPS")
    @Tag("https")
    @Order(0)
    @Test
    fun shouldEnableHttps() {
        assertTrue(
            sessionMega.enableHttps(),
            "HTTPS should be enabled after the action"
        )
    }

    @DisplayName("Delete directory if exists")
    @Tag("rm")
    @Order(0)
    @Test
    fun shouldDeleteWorkingDirectoryIfExist() {
        sessionMega.remove("megacmd4j")
            .deleteRecursively()
            .ignoreErrorIfNotPresent()
            .run()
    }

    @DisplayName("Session should have authentication object")
    @Tag("auth")
    @Order(0)
    @Test
    fun sessionShouldHaveAuthenticationObject() {
        Assertions.assertNotNull(sessionMega.getAuthentication())
    }

    @DisplayName("HTTPS should be enabled")
    @Tag("https")
    @Order(1)
    @Test
    fun httpsShouldBeEnabled() {
        assertTrue(sessionMega.isHttpsEnabled, "HTTPS should be enabled")
    }

    @DisplayName("Upload file to root folder")
    @Tag("put")
    @Order(1)
    @Test
    fun shouldUploadFileToRoot() {
        MegaTestUtils.createTextFile(
            "target/yolo-test.txt",
            "You only live once..."
        )
        sessionMega.uploadFile("target/yolo-test.txt")
            .run()
        MegaTestUtils.removeFile("target/yolo-test.txt")
    }

    @DisplayName("Upload file to target folder")
    @Tag("put")
    @Order(2)
    @Test
    fun shouldUploadFileToTargetFolder() {
        MegaTestUtils.createTextFile(
            "target/yolo-infinite.txt",
            "You only live infinitive times..."
        )
        sessionMega.uploadFile("target/yolo-infinite.txt", "/megacmd4j/")
            .createRemotePathIfNotPresent()
            .run()
    }

    @DisplayName("Upload multiple files and create remote folder")
    @Tag("put")
    @Order(3)
    @Test
    fun shouldUploadMultipleFilesAndCreateRemoteFolderSuccessfully() {
        MegaTestUtils.createTextFiles("yolo", 10)
        val megaCmd = sessionMega.uploadFiles("/megacmd4j/")
            .createRemotePathIfNotPresent()



        IntStream.rangeClosed(1, 10).forEach { i: Int ->
            val filename: String = MegaTestUtils.testTextFileName("yolo", i)
            megaCmd.addLocalFileToUpload(filename)
        }
        megaCmd.run()
    }

    @DisplayName("When create folder with multilevel structure without recursively flag then fail")
    @Tag("mkdir")
    @Order(4)
    @Test
    fun failWhenCreateMultiLevelDirectoryWithoutRecursivelyFlag() {
        Assertions.assertThrows(
            MegaWrongArgumentsException::class.java,
            Executable { sessionMega.makeDirectory("megacmd4j/level2/level3").run() })
    }

    @DisplayName("When create folder with multilevel structure with recursively flag then success")
    @Tag("mkdir")
    @Order(5)
    @Test
    fun succeedWhenCreateMultiLevelDirectoryWithRecursivelyFlag() {
        sessionMega.makeDirectory("megacmd4j/level2/level3")
            .recursively()
            .ignoreErrorIfExists()
            .run()
    }

    @DisplayName("When try to create existing folder with multilevel structure then fail")
    @Tag("mkdir")
    @Order(6)
    @Test
    fun failWhenCreateExistingMultiLevelDirectory() {
        Assertions.assertThrows(
            MegaInvalidStateException::class.java,
            Executable {
                sessionMega.makeDirectory("megacmd4j/level2/level3")
                    .recursively()
                    .throwErrorIfExists()
                    .run()
            })
    }

    @DisplayName("Upload file remotely")
    @Tag("cp")
    @Order(7)
    @Test
    fun succeedCopyFileRemotely() {
        sessionMega.copy("megacmd4j/yolo.txt", "megacmd4j/level2/yolo.txt")
            .run()
    }

    @DisplayName("Create folder with spaces in the name")
    @Tag("mkdir")
    @Order(7)
    @Test
    fun shouldCreateFolderWithSpace() {
        sessionMega.makeDirectory("megacmd4j/level2/level 3 with spaces")
            .recursively()
            .run()
    }

    @DisplayName("Should return previously uploaded file")
    @Tag("ls")
    @Order(8)
    @Test
    fun lsShouldReturnAFileAndADirectory() {
        val files: List<FileInfo> = sessionMega.ls("megacmd4j/level2").call()
        assertEquals(
            3,
            files.size,
            "There should be 3 elements"
        )
        val fileInfo = files.asSequence()
            .filter { it.name == "yolo.txt" }
            .firstOrNull() ?: throw MegaInvalidStateException("The previously uploaded filed was not found")

        Assertions.assertTrue(fileInfo.isFile, "The found object is not a file")
        val directoryInfo = files.asSequence()
            .filter { it.name == "level3" }
            .firstOrNull() ?: throw MegaInvalidStateException("The previously uploaded folder was not found")

        Assertions.assertTrue(
            directoryInfo.isDirectory,
            "The found object is not a directory"
        )
        val directoryWithSpacesInfo = files.asSequence()
            .filter { it.name == "level 3 with spaces" }
            .firstOrNull()
            ?: throw MegaInvalidStateException("The previously uploaded folder with spaces was not found")

        Assertions.assertTrue(
            directoryWithSpacesInfo.isDirectory,
            "The found object is not a directory"
        )
    }

    @DisplayName("When download remote folder to nonexistent local folder then fail")
    @Tag("get")
    @Order(9)
    @Test
    fun failWhenDownloadRemoteFolderToNonExistentLocalFolder() {
        Assertions.assertThrows(
            MegaInvalidTypeException::class.java
        ) {
            sessionMega.get("megacmd4j/level2", "target/savedLevel2")
                .run()
        }
    }

    @DisplayName("When download remote folder to existing local folder then success")
    @Tag("get")
    @Order(10)
    @Test
    fun succeedWhenDownloadRemoteFolderToExistingLocalFolder() {
        sessionMega.get("megacmd4j/level2", "target")
            .run()
        Assertions.assertTrue(
            File("target/level2/yolo.txt").isFile,
            "Invalid downloaded file"
        )
        Assertions.assertTrue(
            File("target/level2/level3").isDirectory,
            "Invalid downloaded directory"
        )
        Assertions.assertTrue(
            File("target/level2/level 3 with spaces").isDirectory,
            "Invalid downloaded directory"
        )
    }

    @DisplayName("Download single remote file to local folder")
    @Tag("get")
    @Order(11)
    @Test
    fun shouldDownloadASingleFileIntoLocalFolder() {
        sessionMega.get("megacmd4j/yolo-infinite.txt", "target/level2")
            .run()
        Assertions.assertTrue(
            File("target/level2/yolo-infinite.txt").exists(),
            "The downloaded file should exist locally"
        )
    }

    @DisplayName("Downloaded file should have the same content as when it was uploaded")
    @Tag("get")
    @Order(12)
    @Test
    @Throws(
        IOException::class
    )
    fun downloadedContentShouldBeConsistent() {
        val lineSeparator = System.getProperty("line.separator")
        val firstLineOfGeneratedFile = Files.lines(File("target/yolo-infinite.txt").toPath())
            .collect(Collectors.joining(lineSeparator))
        val firstLineOfDownloadedFile = Files.lines(File("target/level2/yolo-infinite.txt").toPath())
            .collect(Collectors.joining(lineSeparator))
        assertEquals(
            firstLineOfGeneratedFile, firstLineOfDownloadedFile,
            "The downloaded file has differed its content"
        )
    }

    @DisplayName("Move multiple files using pattern into sub-path: *")
    @Tag("ls")
    @Order(13)
    @Test
    fun moveMultipleFilesUsingPatternIntoSubpath() {
        sessionMega.move("megacmd4j/*-*.txt", "/megacmd4j/level2/")
            .run()
        val currentFiles: List<FileInfo> = sessionMega.ls("megacmd4j/").call()
        assertEquals(
            3, currentFiles.size,
            "Only 2 files were expected"
        )
        val amountOfDirectoriesLeft = currentFiles.stream()
            .filter(FileInfo::isDirectory).count()
        assertEquals(
            2, amountOfDirectoriesLeft,
            "There should be only 1 directory left"
        )
        val amountOfFilesLeft = currentFiles.stream()
            .filter(FileInfo::isFile)
            .count()
        assertEquals(
            1, amountOfFilesLeft,
            "There should be only 1 file left"
        )
    }

    @DisplayName("Upload file to remote folder with whitespace")
    @Tag("put")
    @Order(13)
    @Test
    fun uploadFileToFoldersWithWhitespace() {
        MegaTestUtils.createTextFile(
            "target/folder with white spaces/yolo.txt",
            "You only live infinitive times..."
        )
        sessionMega.uploadFile(
            "target/folder with white spaces/yolo.txt",
            "/megacmd4j/remote folder/"
        )
            .createRemotePathIfNotPresent()
            .run()
        assertTrue(
            sessionMega.ls("/megacmd4j/remote folder/yolo.txt")
                .exists()
        )
    }

    @DisplayName("Create remote folder with whitespaces")
    @Tag("put")
    @Order(14)
    @Test
    fun shouldCreateFolderWithWhitespaces() {
        sessionMega.makeDirectory("megacmd4j/level2/another folder/")
            .recursively()
            .run()
        assertTrue(
            sessionMega.ls("megacmd4j/level2/another folder/")
                .exists()
        )
    }

    @DisplayName("HTTPS should be disabled")
    @Tag("https")
    @Order(14)
    @Test
    fun shouldDisableHTTPS() {
        assertFalse(
            sessionMega.disableHttps(),
            "HTTPS should be disabled"
        )
    }

    @DisplayName("When list an existing empty remote folder then exits should return true")
    @Tag("ls")
    @Order(14)
    @Test
    fun givenEmptyFolderWhenListThenExistsIsTrue() {
        assertTrue(
            sessionMega.ls("megacmd4j/level2/level3").exists(),
            "The directory level3 should exist"
        )
    }

    @DisplayName("When list a non-existing empty remote folder then exits should return false")
    @Tag("ls")
    @Order(15)
    @Test
    fun givenNonExistingFileWhenListThenExistsIsFalse() {
        assertFalse(
            sessionMega.ls("megacmd4j/level2/level33").exists(),
            "That file/directory doesnt exist"
        )
    }

    @DisplayName("Move file from/to remote path with whitespaces")
    @Tag("ls")
    @Tag("mv")
    @Order(15)
    @Test
    fun shouldMoveFileFromPathWithWhitespace() {
        sessionMega.move(
            "megacmd4j/remote folder/yolo.txt",
            "megacmd4j/level2/another folder/yolo moved.txt"
        )
            .run()
        assertTrue(
            sessionMega.ls(
                "megacmd4j/level2/another folder/yolo moved.txt"
            ).exists()
        )
    }

    @DisplayName("Delete multiple files with mask: *")
    @Tag("exists")
    @Tag("rm")
    @Order(15)
    @Test
    fun deleteMultipleFilesWithMaskShouldBeOk() {
        if (sessionMega.exists("megacmd4j/level2/yolo-*.txt")) {
            sessionMega.remove("megacmd4j/level2/yolo-*.txt").run()
        }
        assertEquals(
            1,
            sessionMega.count("megacmd4j/level2", FileInfo::isFile),
            "There should be only 1 file"
        )
        assertTrue(
            sessionMega.exists("megacmd4j/level2/level3"),
            "There should be left a directory"
        )
    }

    @DisplayName("Remove remote directory")
    @Tag("rm")
    @Order(16)
    @Test
    fun shouldRemoveRemoteDirectory() {
        sessionMega.removeDirectory("megacmd4j/level2/level3").run()
    }

    @DisplayName("When remove nonexistent remote folder")
    @Tag("rm")
    @Order(17)
    @Test
    fun givenNonexistentDirectoryWhenRemoveThenFail() {
        Assertions.assertThrows(
            MegaException::class.java,
            Executable { sessionMega.removeDirectory("megacmd4j/level2/level3").run() })
    }

    @DisplayName("HTTPS should be disabled")
    @Tag("https")
    @Order(17)
    @Test
    fun httpsShouldBeDisabled() {
        assertFalse(
            sessionMega.isHttpsEnabled,
            "HTTPS should be disabled"
        )
    }

    @DisplayName("When share nonexistent remote folder then fail")
    @Tag("share")
    @Order(18)
    @Test
    fun givenNonexistentDirectoryWhenShareThenFail() {
        val username = System.getenv(fe.megacmdkt.Mega.USERNAME_ENV_VAR)
        Assertions.assertThrows(
            MegaResourceNotFoundException::class.java,
            Executable {
                sessionMega.share("megacmd4j/unexisting-folder", username)
                    .grantReadAndWriteAccess()
                    .run()
            })
    }

    @DisplayName("Share existing remote folder")
    @Tag("share")
    @Order(18)
    @Test
    fun givenExistingDirectoryWhenShareThenSuccess() {
        val username = System.getenv(fe.megacmdkt.Mega.USERNAME_ENV_VAR)
        sessionMega.share("megacmd4j/level2", username)
            .grantReadAndWriteAccess()
            .run()
    }

    @DisplayName("When export nonexistent remote folder then fail")
    @Tag("export")
    @Order(19)
    @Test
    fun givenNonexistentDirectoryWhenExportThenFail() {
        Assertions.assertThrows(
            MegaResourceNotFoundException::class.java,
            Executable {
                sessionMega.export("megacmd4j/unexisting-folder")
                    .enablePublicLink()
                    .call()
            })
    }

    @DisplayName("Export existing remote folder")
    @Tag("export")
    @Order(19)
    @Test
    fun givenExistingDirectoryWhenExportThenSuccess() {
        val exportInfo: ExportInfo = sessionMega.export("megacmd4j/level2")
            .enablePublicLink()
            .call()
        assertEquals("/megacmd4j/level2", exportInfo.remotePath)
        Assertions.assertTrue(
            exportInfo.publicLink.startsWith("https://mega.nz/"),
            "The exported public link should be located in MEGA.nz"
        )
        Assertions.assertTrue(
            exportInfo.publicLink.length
                    - "https://mega.nz/".length > 5,
            "The exported url should have more than 5 characters at least"
        )
    }

    @DisplayName("Get information about exported folder")
    @Tag("export")
    @Order(20)
    @Test
    fun exportedFolderShouldAppearInListings() {
        val exportedFiles: List<ExportInfo> = sessionMega.export("megacmd4j").list()
        Assertions.assertTrue(exportedFiles.stream()
            .map(ExportInfo::remotePath)
            .filter { anObject: String -> "megacmd4j/level2".equals(anObject) }
            .findAny().isPresent)
    }

    companion object {
        @AfterAll
        fun finishSession() {
            sessionMega.removeDirectory("megacmd4j").run()
            sessionMega.remove("yolo*.txt").ignoreErrorIfNotPresent().run()
        }
    }
}
