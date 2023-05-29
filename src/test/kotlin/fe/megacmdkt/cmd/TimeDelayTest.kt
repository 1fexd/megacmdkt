package fe.megacmdkt.cmd

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit

@DisplayName("TimeDelayTest")
class TimeDelayTest {
    @get:Test
    val yearsUsingPeriodShouldBeOk: Unit
        get() {
            val timeDelay = TimeDelay.of(Period.ofYears(1))
            Assertions.assertEquals(1L, timeDelay[ChronoUnit.YEARS])
        }

    @get:Test
    val daysUsingPeriodShouldBeOk: Unit
        get() {
            val timeDelay = TimeDelay.of(Period.ofDays(2))
            Assertions.assertEquals(2L, timeDelay[ChronoUnit.DAYS])
        }

    @get:Test
    val daysUsingDurationShouldBeOk: Unit
        get() {
            val timeDelay = TimeDelay.of(Duration.ofDays(3))
            Assertions.assertEquals(3L, timeDelay[ChronoUnit.DAYS])
        }

    @get:Test
    val unitsShouldSupportYEARS: Unit
        get() {
            val timeDelay = TimeDelay.of(Period.ofYears(5))
            Assertions.assertEquals(5, timeDelay[ChronoUnit.YEARS])
        }

    @get:Test
    val unitsShouldSupportMONTHS: Unit
        get() {
            val timeDelay = TimeDelay.of(Period.ofMonths(6))
            Assertions.assertEquals(6, timeDelay[ChronoUnit.MONTHS])
        }

    @get:Test
    val unitsShouldSupportHOURS: Unit
        get() {
            val timeDelay = TimeDelay.of(Duration.ofHours(6))
            Assertions.assertEquals(6, timeDelay[ChronoUnit.HOURS])
        }

    @get:Test
    val unitsShouldSupportMinutes: Unit
        get() {
            val timeDelay = TimeDelay.of(Duration.ofMinutes(7))
            Assertions.assertEquals(7, timeDelay[ChronoUnit.MINUTES])
        }

    @get:Test
    val unitsShouldSupportSECONDS: Unit
        get() {
            val timeDelay = TimeDelay.of(Duration.ofSeconds(9))
            Assertions.assertEquals(9, timeDelay[ChronoUnit.SECONDS])
        }

    @Test
    fun addToLocalDateShouldBeOk() {
        val date = LocalDate.of(2020, 4, 1)
        val timeDelay = TimeDelay.of(Period.ofYears(1).plusMonths(1))
        val result = date.plus(timeDelay)
        Assertions.assertEquals(LocalDate.of(2021, 5, 1), result)
    }

    @Test
    fun addToLocalDateTimeShouldBeOk() {
        val dateTime = LocalDateTime.of(2020, 4, 1, 0, 30, 5, 0)
        val timeDelay = TimeDelay.of(Period.ofDays(3), Duration.ofSeconds(6))
        val result = dateTime.plus(timeDelay)
        val expectedResult = LocalDateTime.of(2020, 4, 4, 0, 30, 11, 0)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun subtractFromLocalDateShouldBeOk() {
        val date = LocalDate.of(2020, 4, 1)
        val timeDelay = TimeDelay.of(Period.ofYears(1).plusMonths(2))
        val result = date.minus(timeDelay)
        Assertions.assertEquals(LocalDate.of(2019, 2, 1), result)
    }

    @Test
    fun subtractFromLocalDateTimeShouldBeOk() {
        val dateTime = LocalDateTime.of(2020, 4, 1, 1, 0, 0, 10)
        val timeDelay = TimeDelay.of(Period.ofMonths(5), Duration.ofNanos(10))
        val result = dateTime.minus(timeDelay)
        val expectedResult = LocalDateTime.of(2019, 11, 1, 1, 0, 0, 0)
        Assertions.assertEquals(expectedResult, result)
    }

    @DisplayName("TimeDelay#toString should return 0m")
    @Test
    fun toTimeDelayWithZERO() {
        Assertions.assertEquals("0m", TimeDelay.ZERO.toString())
    }

    @DisplayName("TimeDelay#toString should return 12d3h1m")
    @Test
    fun toTimeDelayWithDurationOnly() {
        val timeDelay = TimeDelay.of(
            Duration.ofDays(12).plusHours(3).plusMinutes(1)
        )
        Assertions.assertEquals("12d3h1m", timeDelay.toString())
    }

    @DisplayName("TimeDelay#toString should return 3y11m15d")
    @Test
    fun toTimeDelayWithPeriodOnly() {
        val timeDelay = TimeDelay.of(
            Period.ofYears(3).plusMonths(11).plusDays(15)
        )
        Assertions.assertEquals("3y11m15d", timeDelay.toString())
    }

    @DisplayName("TimeDelay#toString should return 5y2m4d11h1m31s")
    @Test
    fun toTimeDelayWithPeriodAndDuration() {
        val timeDelay = TimeDelay.of(
            Period.ofYears(5).plusMonths(2).plusDays(4),
            Duration.ofHours(11).plusMinutes(1).plusSeconds(31)
        )
        Assertions.assertEquals("5y2m4d11h1m31s", timeDelay.toString())
    }
}
