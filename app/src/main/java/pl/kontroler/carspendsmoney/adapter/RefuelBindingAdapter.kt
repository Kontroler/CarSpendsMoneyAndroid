package pl.kontroler.carspendsmoney.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.swipe.ISwipeable
import com.mikepenz.fastadapter.swipe.SimpleSwipeDrawerCallback
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import pl.kontroler.carspendsmoney.fastAdapterItem.RefuelFastAdapterItem
import pl.kontroler.domain.model.FuelExpense
import pl.kontroler.firebase.util.Resource


/**
 * @author Rafał Nowowieski
 */

object RefuelBindingAdapter {

    @BindingAdapter(
        value = ["fuelExpenseList"],
        requireAll = false
    )
    @JvmStatic
    fun setItems(
        recyclerView: RecyclerView,
        items: Resource<List<FuelExpense>>?
    ) {
        if (items == null) return
        if (items !is Resource.Success) return

        val itemAdapter = ItemAdapter<RefuelFastAdapterItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        setRecyclerViewAdapter(recyclerView, fastAdapter)

        val newItems = createNewItems(items.data)
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
        fastAdapter: FastAdapter<RefuelFastAdapterItem>
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = SlideDownAlphaAnimator()
        recyclerView.adapter = fastAdapter
    }

    private fun createNewItems(items: List<FuelExpense>): List<RefuelFastAdapterItem> {
        val returnedItems = mutableListOf<RefuelFastAdapterItem>()
        items.forEachIndexed { index, fuelExpense ->
            if (index > 0) {
                returnedItems.add(
                    RefuelFastAdapterItem()
                        .withFuelExpense(fuelExpense)
                        .withPreviousFuelExpense(items[index - 1])
                )
            } else {
                returnedItems.add(RefuelFastAdapterItem().withFuelExpense(fuelExpense))
            }
        }
        return returnedItems
    }

}