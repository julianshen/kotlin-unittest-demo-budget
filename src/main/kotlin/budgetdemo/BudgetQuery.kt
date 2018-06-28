package budgetdemo

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class BudgetQuery(val budgetRepo: BudgetRepository) {
    fun query(from: LocalDate, to: LocalDate): Int {
        if (from.isAfter(to)) {
            throw InvalidDateException()
        }

        return getTotalAmount(from, to, getAllBudgetsAsMap())
    }

    private fun getAllBudgetsAsMap() = budgetRepo.findAll().map { it.yearMonth to it.amount }.toMap()

    private inline fun getTotalAmount(from: LocalDate, to: LocalDate, allBudgets: Map<YearMonth, Int>): Int {
        val numOfDays = ChronoUnit.DAYS.between(from, to)
        return listOf(0..numOfDays).flatMap { it }.map {
            YearMonth.from(from.plusDays(it))
        }.groupingBy { it }.eachCount().map {
            (allBudgets[it.key] ?: 0) * it.value / it.key.lengthOfMonth()
        }.sum()
    }
}

class InvalidDateException : Throwable() {

}
