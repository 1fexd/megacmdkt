package fe.megacmdkt.cmd

import fe.megacmdkt.cmd.mega.MegaCmdExport
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class MegaCmdExportTest {
    @DisplayName("MegaCmdExport#setExpireDate using LocalDateTime should produce the right TimeDelay")
    @Test
    fun setExpireDateWithLocalDateTime() {
        val period = Period.ofYears(1).plusMonths(11)
        val duration = Duration.ofHours(5).plusSeconds(50)
        val expirationDate = LocalDateTime.now().plus(period).plus(duration)
        val megaCmdExport = MegaCmdExport("/megacmd/anyfolder")
            .setExpireDate(expirationDate)
        Assertions.assertNotNull(megaCmdExport.expirationTimeDelay)
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.period.years, period.years)
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.period.months, period.months)
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.duration.toHours(), duration.toHours())
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.duration.toMinutes(), duration.toMinutes())
    }

    @DisplayName("MegaCmdExport#setExpireDate using DateTime should produce the right TimeDelay")
    @Test
    fun setExpireDateWithLocalDate() {
        val period = Period.ofDays(2)
        val endDate = LocalDate.now().plus(period)
        val megaCmdExport = MegaCmdExport("/megacmd/anyfolder")
            .setExpireDate(endDate)
        Assertions.assertNotNull(megaCmdExport.expirationTimeDelay)
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.period.days, period.days)
        Assertions.assertEquals(megaCmdExport.expirationTimeDelay!!.duration, Duration.ZERO)
    }
}
