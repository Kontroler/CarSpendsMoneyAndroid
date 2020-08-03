package pl.kontroler.carspendsmoney.adapter

import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import pl.kontroler.domain.model.Currency
import pl.kontroler.domain.model.FuelType
import pl.kontroler.firebase.util.Resource2

object SpinnerBindingAdapters {

    // FUEL TYPE
    @BindingAdapter(
        value = ["fuelTypesList", "selectedFuelType", "selectedFuelTypeAttrChanged"],
        requireAll = false
    )
    @JvmStatic
    fun setItems(
        spinner: AppCompatSpinner,
        items: Resource2<List<FuelType>>?,
        selectedItem: FuelType?,
        listener: InverseBindingListener
    ) {
        if (items == null) return
        if (items !is Resource2.Success) return

        if (spinner.selectedItem != null &&
            selectedItem != null &&
            (spinner.selectedItem as FuelType) == selectedItem &&
            items.data.size == spinner.adapter.count
        ) return

        spinner.adapter = FuelTypeArrayAdapter(
            spinner.context,
            android.R.layout.simple_spinner_dropdown_item,
            items.data
        )

        setCurrentSelection(spinner, selectedItem)
        setSpinnerListener(spinner, listener)
    }

    // FUEL TYPE
    @BindingAdapter(
        value = ["currenciesList", "selectedCurrency", "selectedCurrencyAttrChanged"],
        requireAll = false
    )
    @JvmStatic
    fun setItems(
        spinner: AppCompatSpinner,
        items: Resource2<List<Currency>>?,
        selectedItem: Currency?,
        listener: InverseBindingListener
    ) {
        if (items == null) return
        if (items !is Resource2.Success) return

        if (spinner.selectedItem != null &&
            selectedItem != null &&
            (spinner.selectedItem as Currency) == selectedItem &&
            items.data.size == spinner.adapter.count
        ) return

        spinner.adapter = CurrencyArrayAdapter(
            spinner.context,
            android.R.layout.simple_spinner_dropdown_item,
            items.data
        )

        setCurrentSelection(spinner, selectedItem)
        setSpinnerListener(spinner, listener)
    }

    private fun setSpinnerListener(spinner: AppCompatSpinner, listener: InverseBindingListener) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) = listener.onChange()

            override fun onNothingSelected(adapterView: AdapterView<*>) = listener.onChange()
        }
    }

    private fun <T> setCurrentSelection(spinner: AppCompatSpinner, selectedItem: T?): Boolean {
        if (selectedItem == null) {
            return false
        }

        for (index in 0 until spinner.adapter.count) {
            val currentItem = spinner.getItemAtPosition(index)
            if (currentItem == selectedItem) {
                spinner.setSelection(index)
                return true
            }
        }

        return false
    }

    @InverseBindingAdapter(attribute = "selectedFuelType", event = "selectedFuelTypeAttrChanged")
    @JvmStatic
    fun getSelectedFuelType(spinner: AppCompatSpinner): FuelType {
        return spinner.selectedItem as FuelType
    }

    @InverseBindingAdapter(attribute = "selectedCurrency", event = "selectedCurrencyAttrChanged")
    @JvmStatic
    fun getSelectedCurrency(spinner: AppCompatSpinner): Currency {
        return spinner.selectedItem as Currency
    }

}