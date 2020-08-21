package pl.kontroler.firebase

import java.lang.Exception


/**
 * @author Rafał Nowowieski
 */

class PreviousFuelExpenseCounterIsGreaterException(msg: String) : Exception(msg)
class NextFuelExpenseCounterIsLowerException(msg: String) : Exception(msg)