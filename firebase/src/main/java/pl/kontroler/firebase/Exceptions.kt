package pl.kontroler.firebase


/**
 * @author Rafał Nowowieski
 */

class PreviousFuelExpenseCounterIsGreaterException(msg: String) : Exception(msg)
class NextFuelExpenseCounterIsLowerException(msg: String) : Exception(msg)

class PreviousServiceExpenseCounterIsGreaterException(msg: String) : Exception(msg)
class NextServiceExpenseCounterIsLowerException(msg: String) : Exception(msg)

class WriteServiceExpenseException(msg: String) : Exception(msg)

class DeleteFuelExpenseException(msg: String) : Exception(msg)