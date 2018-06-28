package budgetdemo

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class BudgetQuery(val budgetRepo: BudgetRepository) {
    fun query(from: LocalDate, to: LocalDate): Int {
        if (from.isAfter(to)) {
            throw InvalidDateException()
        }

        val allBudgets = budgetRepo.findAll().map { YearMonth.of(it.year, it.month) to it.amount }.toMap()
        val numOfDays = ChronoUnit.DAYS.between(from, to)

        return listOf(0..numOfDays).flatMap { it }.map { i ->
            YearMonth.from(from.plusDays(i))
        }.groupingBy { it }.eachCount().map {
            var yearMonth = it.key
            val lengthOfMonth = yearMonth.lengthOfMonth()
            (allBudgets[yearMonth]?:0) * it.value / lengthOfMonth
        }.sum()
    }
}

class InvalidDateException : Throwable() {

}
