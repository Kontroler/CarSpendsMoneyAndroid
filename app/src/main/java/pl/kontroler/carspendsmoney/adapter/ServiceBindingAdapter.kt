package pl.kontroler.carspendsmoney.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.swipe.SimpleSwipeDrawerCallback
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import io.reactivex.rxjava3.functions.Consumer
import pl.kontroler.carspendsmoney.fastAdapterItem.ServiceFastAdapterItem
import pl.kontroler.domain.model.ServiceExpense
import pl.kontroler.firebase.util.Resource

object ServiceBindingAdapter {

    @BindingAdapter(
        value = ["serviceExpenseList", "deleteAction"],
        requireAll = false
    )
    @JvmStatic
    fun setItems(
        recyclerView: RecyclerView,
        items: Resource<List<ServiceExpense>>?,
        deleteAction: (serviceExpense: ServiceExpense) -> Unit,
    ) {
        if (items == null) return
        if (items !is Resource.Success) return

        val itemAdapter = ItemAdapter<ServiceFastAdapterItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        setRecyclerViewAdapter(recyclerView, fastAdapter)

        val newItems = createNewItems(items.data, deleteAction)
        itemAdapter.add(newItems)
        fastAdapter.notifyDataSetChanged()

        val touchCallback = SimpleSwipeDrawerCallback(ItemTouchHelper.LEFT)
            .withSwipeLeft(80)
            .withSensitivity(10f)

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(recyclerView)

    }

    private fun setRecyclerViewAdapter(
        recyclerView: RecyclerView,
        fastAdapter: FastAdapter<ServiceFastAdapterItem>,
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = SlideDownAlphaAnimator()
        recyclerView.adapter = fastAdapter
    }

    private fun createNewItems(
        items: List<ServiceExpense>,
        deleteAction: (serviceExpense: ServiceExpense) -> Unit,
    ): List<ServiceFastAdapterItem> {
        val returnedItems = mutableListOf<ServiceFastAdapterItem>()
        items.forEach { serviceExpense ->
            val refuelFastAdapterItem = ServiceFastAdapterItem()
                .withServiceExpense(serviceExpense)
            refuelFastAdapterItem.deleteAction = Consumer { item -> deleteAction(item) }
            returnedItems.add(refuelFastAdapterItem)
        }
        return returnedItems
    }

}