package pl.kontroler.carspendsmoney.fastAdapterItem

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.swipe.IDrawerSwipeableViewHolder
import com.mikepenz.fastadapter.swipe.ISwipeable
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.android.synthetic.main.fast_adapter_item_service.view.*
import pl.kontroler.carspendsmoney.R
import pl.kontroler.domain.model.ServiceExpense
import java.math.BigDecimal


/**
 * @author Rafał Nowowieski
 */

class ServiceFastAdapterItem : AbstractItem<ServiceFastAdapterItem.ViewHolder>(), ISwipeable {

    private lateinit var serviceExpense: ServiceExpense

    var deleteAction: Consumer<ServiceExpense>? = null

    fun withServiceExpense(serviceExpense: ServiceExpense): ServiceFastAdapterItem {
        this.serviceExpense = serviceExpense
        return this
    }

    override val type: Int
        get() = R.id.fastadapter_item_service_id

    @SuppressLint("SetTextI18n")
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.date.text = serviceExpense.date.toString()
        holder.name.text = serviceExpense.name
        holder.description.text = serviceExpense.description
        holder.counter.text = serviceExpense.counter.toString()
        holder.totalPrice.text =
            "${serviceExpense.totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP)} " +
                    serviceExpense.currency

        holder.deleteBtn.setImageDrawable(
            IconicsDrawable(holder.itemView.context, MaterialDesignIconic.Icon.gmi_delete).apply {
                colorInt = Color.RED
                sizeDp = 24
            }
        )

        holder.deleteActionRunnable = Runnable { deleteAction?.accept(serviceExpense) }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.date.text = null
        holder.name.text = null
        holder.description.text = null
        holder.counter.text = null
        holder.totalPrice.text = null
        holder.deleteBtn.setImageDrawable(null)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), IDrawerSwipeableViewHolder {

        var date: MaterialTextView = view.date
        var name: MaterialTextView = view.name
        var description: MaterialTextView = view.description
        var counter: MaterialTextView = view.counter
        var totalPrice: MaterialTextView = view.totalPrice
        var itemContent: ConstraintLayout = view.fastAdapterItem_service
        var deleteBtn: ImageButton = view.delete

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