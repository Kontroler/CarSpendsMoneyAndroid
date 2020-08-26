package pl.kontroler.carspendsmoney.binding

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import pl.kontroler.carspendsmoney.R
import pl.kontroler.firebase.util.sumByBigDecimal
import java.math.BigDecimal


object PieChartBindings {

    @BindingAdapter("android:data")
    @JvmStatic
    fun setData(view: PieChart, values: List<ExpensePieChartData>?) {
        configurePieChart(view)
        setPieChartData(values, view)
    }

    private fun configurePieChart(view: PieChart) {
        view.setUsePercentValues(false)
        view.setExtraOffsets(5f, 10f, 5f, 5f)
        view.dragDecelerationFrictionCoef = 0.95f
        view.isDrawHoleEnabled = true
        view.setHoleColor(Color.WHITE)
        view.setTransparentCircleColor(Color.WHITE)
        view.setTransparentCircleAlpha(110)
        view.holeRadius = 58f
        view.transparentCircleRadius = 61f

        view.setDrawCenterText(true)

        view.rotationAngle = 0f

        view.isRotationEnabled = true
        view.isHighlightPerTapEnabled = true

        // add a selection listener
        //        view.setOnChartValueSelectedListener(this)

        view.animateY(1400, Easing.EaseInOutQuad)

        val l: Legend = view.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        view.setEntryLabelColor(Color.BLACK)
        view.setEntryLabelTextSize(12f)
    }

    private fun setPieChartData(
        values: List<ExpensePieChartData>?,
        view: PieChart
    ) {
        val entries = mutableListOf<PieEntry>()
        val valuesSum = values?.sumByBigDecimal { it.value } ?: BigDecimal.ZERO
        if (values.isNullOrEmpty()) {
            view.centerText =
                generateCenterSpannableText(view.context.getString(R.string.pieChart_noExpenses))
        } else {
            view.centerText = generateCenterSpannableText(
                "${view.context.getString(R.string.pieChart_amountsIn)} ${values[0].currency}"
            )
        }

        values?.forEach {
            if (valuesSum > BigDecimal.ZERO) {
                val entry = PieEntry(it.value.toFloat(), it.type.toString())
                entries.add(entry)
            }
        }

        val dataSet = PieDataSet(entries, "Expenses")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(view))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        view.data = data

        view.highlightValues(null)
        view.invalidate()
    }

    private fun generateCenterSpannableText(text: String): SpannableString? {
        val s = SpannableString(text)
        s.setSpan(RelativeSizeSpan(1.8f), 0, s.length, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), 0, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length, 0)
        return s
    }

}