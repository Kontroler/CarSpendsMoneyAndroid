package pl.kontroler.carspendsmoney.fastAdapterItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import pl.kontroler.carspendsmoney.R
import pl.kontroler.carspendsmoney.databinding.FastAdapterItemRefuelBinding
import pl.kontroler.domain.model.FuelExpense


/**
 * @author Rafał Nowowieski
 */

class RefuelFastAdapterItem : AbstractBindingItem<FastAdapterItemRefuelBinding>() {

    private lateinit var fuelExpense: FuelExpense
    private lateinit var previousFuelExpense: FuelExpense

    fun withFuelExpense(fuelFuelExpense: FuelExpense): RefuelFastAdapterItem {
        this.fuelExpense = fuelFuelExpense
        return this
    }

    fun withPreviousFuelExpense(previousFuelExpense: FuelExpense): RefuelFastAdapterItem {
        this.previousFuelExpense = previousFuelExpense
        return this
    }

    override val type: Int
        get() = R.id.fastAdapterItem_refuel

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): FastAdapterItemRefuelBinding {
        return FastAdapterItemRefuelBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bindView(binding: FastAdapterItemRefuelBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.fuelType.text = fuelExpense.fuelType.code
        binding.date.text = fuelExpense.date.toString()
        binding.totalPrice.text = fuelExpense.totalPrice.toString()
        binding.quantity.text = "${fuelExpense.quantity}${fuelExpense.unit}"
        binding.unitPrice.text =
            "${fuelExpense.unitPrice} ${fuelExpense.currency}/${fuelExpense.unit}"
        binding.kmSinceLastRefueling.text = getKmSinceLastRefueling()
        if (fuelExpense.description.isBlank()) {
            binding.description.visibility = View.GONE
        } else {
            binding.description.text = fuelExpense.description
        }
    }

    private fun getKmSinceLastRefueling(): String {
        return if (::previousFuelExpense.isInitialized) {
            val currentCounter = fuelExpense.counter
            val previousCounter = previousFuelExpense.counter
            "$currentCounter (+${currentCounter - previousCounter}) km"
        } else {
            val currentCounter = fuelExpense.counter
            "$currentCounter (+0) km"
        }
    }

}