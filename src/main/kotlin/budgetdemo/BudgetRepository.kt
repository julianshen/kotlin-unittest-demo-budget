package budgetdemo

interface BudgetRepository {
    fun findAll():List<Budget>
}
