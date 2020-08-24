package pl.kontroler.carspendsmoney.fastAdapterItem

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.swipe.IDrawerSwipeableViewHolder
import com.mikepenz.fastadapter.swipe.ISwipeable
import com.mikepenz.fastadapter.swipe.SimpleSwipeDrawerCallback
import kotlinx.android.synthetic.main.fast_adapter_item_refuel.view.*
import pl.kontroler.carspendsmoney.R
import pl.kontroler.domain.model.FuelExpense


/**
 * @author Rafał Nowowieski
 */

class RefuelFastAdapterItem : AbstractItem<RefuelFastAdapterItem.ViewHolder>(), ISwipeable {

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
        get() = R.id.fastadapter_item_refuel_id


//    @SuppressLint("SetTextI18n")
//    override fun bindView(binding: FastAdapterItemRefuelBinding, payloads: List<Any>) {
//        super.bindView(binding, payloads)
//        binding.fuelType.text = fuelExpense.fuelType.code
//        binding.date.text = fuelExpense.date.toString()
//        binding.totalPrice.text = fuelExpense.totalPrice.toString()
//        binding.quantity.text = "${fuelExpense.quantity}${fuelExpense.unit}"
//        binding.unitPrice.text =
//            "${fuelExpense.unitPrice} ${fuelExpense.currency}/${fuelExpense.unit}"
//        binding.kmSinceLastRefueling.text = getKmSinceLastRefueling()
//        if (fuelExpense.description.isBlank()) {
//            binding.description.visibility = View.GONE
//        } else {
//            binding.description.text = fuelExpense.description
//        }
//    }

    @SuppressLint("SetTextI18n")
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)
        holder.fuelType.text = fuelExpense.fuelType.code
        holder.date.text = fuelExpense.date.toString()
        holder.totalPrice.text = fuelExpense.totalPrice.toString()
        holder.quantity.text = "${fuelExpense.quantity}${fuelExpense.unit}"
        holder.unitPrice.text =
            "${fuelExpense.unitPrice} ${fuelExpense.currency}/${fuelExpense.unit}"
        holder.kmSinceLastRefueling.text = getKmSinceLastRefueling()
        if (fuelExpense.description.isBlank()) {
            holder.description.visibility = View.GONE
        } else {
            holder.description.text = fuelExpense.description
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.fuelType.text = null
        holder.date.text = null
        holder.totalPrice.text = null
        holder.quantity.text = null
        holder.unitPrice.text = null
        holder.kmSinceLastRefueling.text = null
        holder.description.text = null
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), IDrawerSwipeableViewHolder {
        var itemContent = view.fastAdapterItem_refuel
        var fuelType = view.fuelType
        var date = view.date
        var totalPrice = view.totalPrice
        var quantity = view.quantity
        var unitPrice = view.unitPrice
        var kmSinceLastRefueling = view.kmSinceLastRefueling
        var description = view.description

        override val swipeableView: View
            get() = itemContent
    }

    override val layoutRes: Int
        get() = R.layout.fast_adapter_item_refuel

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override val isSwipeable: Boolean = true

}