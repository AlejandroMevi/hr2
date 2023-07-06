package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.Calendar
import java.util.Locale
class DateIndicator (
    private val context: Context,
    private val button:MaterialButton,
    private val textBefore:MaterialTextView?=null,
    private val textAfter:MaterialTextView?=null,
    private val viewPager2: ViewPager2?=null) {
    companion object{
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        var dateSelected = MutableLiveData<String>()
        var monthData = calendar.get(Calendar.MONTH)
        var yearData = calendar.get(Calendar.YEAR)
    }
    fun createDialogWithoutDateField() {
        val builder = MonthPickerDialog.Builder(context, { _, _ -> }, Calendar.YEAR, Calendar.MONTH)
        builder
            .setActivatedMonth(monthData)
            .setMinYear(calendar.get(Calendar.YEAR)-5)
            .setActivatedYear(yearData)
            .setMaxYear(calendar.get(Calendar.YEAR)+5)
            .setMinMonth(Calendar.JANUARY)
            .setTitle("Seleccione una Fecha")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
            .setOnMonthChangedListener {month->
                monthData = month
                setDate(month)
            }.setOnYearChangedListener {year->
                yearData = year
                dateSelected.value = year.toString()
                setDate(monthData)
            }.build().show()
    }
    @SuppressLint("SetTextI18n")
    fun setDate(monthSet:Int){
        monthData = monthSet
        button.text = "${getMonthName(monthData)}/${yearData}"
        textBefore?.text = getMonthName(monthData-1)
        textAfter?.text = getMonthName(monthData+1)
        viewPager2?.currentItem = monthData
    }
    private fun getMonthName(index: Int): String? {
        val calendar = Calendar.getInstance(Locale.US)
        calendar[Calendar.MONTH] = index
        calendar[Calendar.DAY_OF_MONTH] = 1
        return java.lang.String.format(Locale.US, "%tb", calendar)
    }
}