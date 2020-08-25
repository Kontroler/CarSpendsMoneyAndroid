package pl.kontroler.carspendsmoney.fastAdapterItem

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.swipe.IDrawerSwipeableViewHolder
import com.mikepenz.fastadapter.swipe.ISwipeable
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.android.synthetic.main.fast_adapter_item_refuel.view.*
import pl.kontroler.carspendsmoney.R
import pl.kontroler.domain.model.FuelExpense


/**
 * @author Rafał Nowowieski
 */

class RefuelFastAdapterItem : AbstractItem<RefuelFastAdapterItem.ViewHolder>(), ISwipeable {

    private lateinit var fuelExpense: FuelExpense
    private lateinit var previousFuelExpense: FuelExpense

    var deleteAction: Consumer<FuelExpense>? = null

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

        holder.deleteBtn.setImageDrawable(
            IconicsDrawable(holder.itemView.context, MaterialDesignIconic.Icon.gmi_delete).apply {
                colorInt = Color.RED
                sizeDp = 24
            }
        )

        holder.deleteActionRunnable = Runnable { deleteAction?.accept(fuelExpense) }
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
        holder.deleteBtn.setImageDrawable(null)
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
        var deleteBtn = view.delete

        var deleteActionRunnable: Runnable? = null

        override val swipeableView: View
            get() = itemContent

        init {
            deleteBtn.setOnClickListener { deleteActionRunnable?.run() }
        }
    }

    override val layoutRes: Int
        get() = R.layout.fast_adapter_item_refuel

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override val isSwipeable: Boolean = true

}