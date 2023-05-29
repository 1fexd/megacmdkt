package fe.megacmdkt.cmd

import java.time.Duration
import java.time.Period
import java.time.temporal.*

class TimeDelay private constructor(period: Period, duration: Duration) : TemporalAmount {
    private val days = duration.toDays().let { durationDays ->
        if (durationDays == 0L) period.days else durationDays.toInt()
    }

    val period: Period = Period.of(period.years, period.months, days)
    val duration: Duration = duration.minusDays(duration.toDays())

    override fun get(unit: TemporalUnit): Long {
        return when (unit) {
            ChronoUnit.YEARS -> period.years.toLong()
            ChronoUnit.MONTHS -> period.months.toLong()
            ChronoUnit.DAYS -> period.days.toLong()
            ChronoUnit.HOURS -> toHours()
            ChronoUnit.MINUTES -> toMinutes()
            ChronoUnit.SECONDS -> toSeconds()
            else -> throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
        }
    }

    override fun getUnits(): List<TemporalUnit> {
        return SUPPORTED_UNITS
    }

    override fun addTo(temporal: Temporal): Temporal {
        return period.addTo(duration.addTo(temporal))
    }

    override fun subtractFrom(temporal: Temporal): Temporal {
        return period.subtractFrom(duration.subtractFrom(temporal))
    }

    private fun toSeconds() = duration.seconds % SECONDS_PER_MINUTE
    private fun toMinutes() = duration.toMinutes() % MINUTES_PER_HOUR
    private fun toHours() = duration.toHours() % HOURS_PER_DAY


    override fun toString(): String {
        return if (this == ZERO) {
            "0m"
        } else {
            val buf = StringBuilder()
            val period = period
            if (period != Period.ZERO) {
                if (period.years != 0) {
                    buf.append(period.years).append('y')
                }
                if (period.months != 0) {
                    buf.append(period.months).append('m')
                }
                if (period.days != 0) {
                    buf.append(period.days).append('d')
                }
            }
            val duration = duration
            if (duration != Duration.ZERO) {
                val hours = toHours()
                if (hours != 0L) {
                    buf.append(hours).append('h')
                }
                val minutes = toMinutes()
                if (minutes != 0L) {
                    buf.append(minutes).append('m')
                }
                val seconds = toSeconds()
                if (seconds != 0L) {
                    buf.append(seconds).append('s')
                }
            }
            buf.toString()
        }
    }

    companion object {
        val ZERO = TimeDelay(Period.ZERO, Duration.ZERO)
        const val HOURS_PER_DAY = 24
        const val MINUTES_PER_HOUR = 60
        const val SECONDS_PER_MINUTE = 60
        private val SUPPORTED_UNITS = listOf(
            ChronoUnit.YEARS,
            ChronoUnit.MONTHS,
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS,
            ChronoUnit.NANOS
        )

        fun of(period: Period, duration: Duration): TimeDelay {
            return TimeDelay(period, duration)
        }

        fun of(period: Period): TimeDelay {
            return TimeDelay(period, Duration.ZERO)
        }

        fun of(duration: Duration): TimeDelay {
            return TimeDelay(Period.ZERO, duration)
        }
    }
}
