package fe.megacmdkt.cmd

import fe.megacmdkt.MegaTestUtils
@org.junit.jupiter.api.Tag("ls")
internal class MegaCmdListTest : AbstractRemoteTests() {
    @org.junit.jupiter.api.DisplayName("List files with a whitespace in their name or not")
    @org.junit.jupiter.api.Test
    fun shouldListFileWithSpaceInTheRootFolder() {
        MegaTestUtils.createTextFile(
            "target/test 123.txt",
            "Content of file with space"
        )
        MegaTestUtils.createTextFile(
            "target/test123.txt",
            "Content of file without space"
        )
        sessionMega!!.uploadFiles("/", "target/test123.txt", "target/test 123.txt")
            .run()
        val files: List<FileInfo> = sessionMega!!.ls("/").call()
        org.junit.jupiter.api.Assertions.assertTrue(
            files.stream().filter { f: FileInfo -> f.name == "test123.txt" }.findAny().isPresent
        )
        org.junit.jupiter.api.Assertions.assertTrue(
            files.stream().filter { f: FileInfo -> f.name == "test 123.txt" }.findAny().isPresent
        )
        org.junit.jupiter.api.Assertions.assertFalse(
            files.stream().filter { f: FileInfo -> f.name == "test123123123.txt" }.findAny().isPresent
        )
    }
}
