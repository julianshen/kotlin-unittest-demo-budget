package budgetdemo

import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate.of as date

class BudgetTest {

    val budgetRepo = mock<BudgetRepository>()
    val budgetQuery = BudgetQuery(budgetRepo)

    @Test
    fun end_date_before_start_date() {
        assertThatExceptionOfType(InvalidDateException::class.java).isThrownBy { budgetQuery.query(from = date(2018, 2, 1), to = date(2018, 1, 28)) }
    }

    @Test
    fun same_day() {
        givenBudgetIn(budgetRepo) {
            28 to "2018-02"
        }

        expectAmount(1,
                budgetQuery.query(
                        from = date(2018, 2, 1),
                        to = date(2018, 2, 1)))
    }

    @Test
    fun two_month() {
        givenBudgetIn(budgetRepo) {
            28 to "2018-02"
            31 to "2018-03"
        }
        expectAmount(28 + 31,
                budgetQuery.query(
                        from = date(2018, 2, 1),
                        to = date(2018, 3, 31)))
    }

    @Test
    fun cross_months() {
        givenBudgetIn(budgetRepo) {
            28 to "2018-02"
            31 to "2018-03"
        }
        expectAmount(14 + 15,
                budgetQuery.query(
                        from = date(2018, 2, 15),
                        to = date(2018, 3, 15)))
    }

    @Test
    fun within_month() {
        givenBudgetIn(budgetRepo) {
            28 to "2018-02"
        }
        expectAmount(11,
                budgetQuery.query(
                        from = date(2018, 2, 15),
                        to = date(2018, 2, 25)))
    }

    @Test
    fun long_months_with_some_empty_budgets() {
        givenBudgetIn(budgetRepo) {
            300 to "2018-04"
            90 to "2018-06"
        }
        expectAmount(16 * 10 + 15 * 3,
                budgetQuery.query(
                        from = date(2018, 4, 15),
                        to = date(2018, 6, 15)))
    }

    @Test
    fun empty_budgets() {
        givenBudgetIn(budgetRepo) {
            //Empty budget
        }

        expectAmount(0,
                budgetQuery.query(
                        from = date(2018, 2, 15),
                        to = date(2018, 3, 15)))
    }

    @Test
    fun cross_years() {
        givenBudgetIn(budgetRepo) {
            28 to "2018-02"
            62 to "2018-03"
            300 to "2018-04"
            90 to "2018-06"
        }
        expectAmount(28 + 62 + 300 + 90,
                budgetQuery.query(
                        from = date(2017, 2, 1),
                        to = date(2018, 6, 30)))
    }

    private fun expectAmount(amount: Int, actual: Int) {
        assertEquals(amount, actual)
    }
}