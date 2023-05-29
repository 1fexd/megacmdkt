package fe.megacmdkt.cmd


import fe.megacmdkt.Mega
import fe.megacmdkt.MegaSession
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag

@Tag("remote")
open class AbstractRemoteTests {
    companion object {
        lateinit var sessionMega: MegaSession

        @JvmStatic
        @BeforeAll
        fun setupSession() {
            sessionMega = Mega.init()!!
        }

        @JvmStatic
        @AfterAll
        fun finishSession() {
            sessionMega.logout()
        }
    }
}
