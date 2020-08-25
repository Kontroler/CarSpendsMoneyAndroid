package pl.kontroler.firebase


/**
 * @author Rafał Nowowieski
 */

class PreviousFuelExpenseCounterIsGreaterException(msg: String) : Exception(msg)
class NextFuelExpenseCounterIsLowerException(msg: String) : Exception(msg)

class DeleteFuelExpenseException(msg: String) : Exception(msg)