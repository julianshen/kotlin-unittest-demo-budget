package budgetdemo

import java.time.YearMonth

data class Budget(val year: Int, val month: Int, val amount: Int) {
    val yearMonth: YearMonth
        get() {
            return YearMonth.of(year, month)
        }
}
