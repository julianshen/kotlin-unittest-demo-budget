package budgetdemo

import java.time.LocalDate

class BudgetQuery(val budgetRepo: BudgetRepository) {
    fun query(from: LocalDate, to: LocalDate): Int {
        if (from.isAfter(to)) {
            throw InvalidDateException()
        }

        val queryDuration = Duration(from, to)
        return budgetRepo.findAll().map {
            it.getAmount(queryDuration)
        }.sum()
    }
}

class InvalidDateException : Throwable() {
}
