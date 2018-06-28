package budgetdemo

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

data class Budget(val year: Int, val month: Int, val amount: Int) {
    val duration: Duration
        get() = Duration(YearMonth.of(year, month).atDay(1), YearMonth.of(year, month).atEndOfMonth())
    val yearMonth:YearMonth
        get() = YearMonth.of(year, month)

    fun getAmount(querDuration: Duration):Int {
        return amount * duration.caclulateIntersactionDays(querDuration).toInt() / yearMonth.lengthOfMonth()
    }
}

data class Duration(val from: LocalDate, val to: LocalDate) {
    fun caclulateIntersactionDays(another: Duration): Long {
        val from = if (this.from.isAfter(another.from)) this.from else another.from
        val to = if (this.to.isBefore(another.to)) this.to else another.to

        val diffDays = ChronoUnit.DAYS.between(from, to) + 1
        return if(diffDays < 0) 0 else diffDays
    }
}