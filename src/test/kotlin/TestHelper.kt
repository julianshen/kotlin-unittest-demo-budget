package budgetdemo

import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.YearMonth

inline fun <reified T : Any> mock():T {
    return org.mockito.Mockito.mock(T::class.java)
}

class GivenBudgetDsl {
    val budgetList = mutableListOf<Budget>()

    infix fun Int.to(dateStr:String) {
        val yearMonth = YearMonth.parse(dateStr)

        budgetList.add(Budget(yearMonth.year, yearMonth.month.value, this))
    }
}

fun givenBudgetIn(mockedBudgetRepository: BudgetRepository, init:GivenBudgetDsl.()->Unit) {
    val givenBudgetDsl = GivenBudgetDsl()
    givenBudgetDsl.init()

    `when`(mockedBudgetRepository.findAll()).thenReturn(givenBudgetDsl.budgetList)
}