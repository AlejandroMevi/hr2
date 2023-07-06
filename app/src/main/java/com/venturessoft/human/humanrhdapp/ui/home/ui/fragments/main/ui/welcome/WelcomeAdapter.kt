package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.welcome

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.Calendario
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.models.KardexMensualItem
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.humanrhdapp.databinding.ListItemBinding

class WelcomeAdapter(
    private val item: List<KardexMensualItem>
    , private val requireActivity: Activity) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {
    private var ca: Calendario = Calendario()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: ViewHolderGeneral<*>, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bind(item[position])
        }
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int = item.size

    private inner class ViewHolder(
        val binding: ListItemBinding
    ) : ViewHolderGeneral<KardexMensualItem>(binding.root) {
        override fun bind(item: KardexMensualItem) {
            item.marcas?.let {marks->
                marks.indices.forEachIndexed { index, i ->
                    when (index) {
                        0 -> binding.dayOne.text = marks[i]
                        1 -> binding.dayTwo.text = marks[i]
                        2 -> binding.dayThree.text = marks[i]
                        3 -> binding.dayFour.text = marks[i]
                        4 -> binding.dayFive.text = marks[i]
                        5 -> binding.daySix.text = marks[i]
                        6 -> binding.daySix.text = marks[i]
                        7 -> binding.daySeven.text = marks[i]
                        8 -> binding.dayEight.text = marks[i]
                        9 -> binding.dayNine.text = marks[i]
                        10 -> binding.dayTen.text = marks[i]
                        11 -> binding.dayEleven.text = marks[i]
                        12 -> binding.dayTwelve.text = marks[i]
                        13 -> binding.dayThirteen.text = marks[i]
                        14 -> binding.dayFourteen.text = marks[i]
                        15 -> binding.dayFiveteen.text = marks[i]
                        16 -> binding.daySixteen.text = marks[i]
                        17 -> binding.daySeventeen.text = marks[i]
                        18 -> binding.dayEighteen.text = marks[i]
                        19 -> binding.dayNineteen.text = marks[i]
                        20 -> binding.dayTwenty.text = marks[i]
                        21 -> binding.dayTwentyOne.text = marks[i]
                        22 -> binding.dayTwentyTwo.text = marks[i]
                        23 -> binding.dayTwentyThree.text = marks[i]
                        24 -> binding.dayTwentyFour.text = marks[i]
                        25 -> binding.dayTwentyFive.text = marks[i]
                        26 -> binding.dayTwentySix.text = marks[i]
                        27 -> binding.dayTwentySeven.text = marks[i]
                        28 -> binding.dayTwentyEight.text = marks[i]
                        29 -> binding.dayTwentyNine.text = marks[i]
                        30 -> binding.dayThirty.text = marks[i]
                        31 -> binding.dayThirtyOne.text = marks[i]
                    }
                }
            }
            val diaA = listOf(
                binding.dayOne,
                binding.dayTwo,
                binding.dayThree,
                binding.dayFour,
                binding.dayFive,
                binding.daySix,
                binding.daySeven,
                binding.dayEight,
                binding.dayNine,
                binding.dayTen,
                binding.dayEleven,
                binding.dayTwelve,
                binding.dayThirteen,
                binding.dayFourteen,
                binding.dayFiveteen,
                binding.daySixteen,
                binding.daySeventeen,
                binding.dayEighteen,
                binding.dayNineteen,
                binding.dayTwenty,
                binding.dayTwentyOne,
                binding.dayTwentyTwo,
                binding.dayTwentyThree,
                binding.dayTwentyFour,
                binding.dayTwentyFive,
                binding.dayTwentySix,
                binding.dayTwentySeven,
                binding.dayTwentyEight,
                binding.dayTwentyNine,
                binding.dayThirty,
                binding.dayThirtyOne
            )
            diaA[ca.getDia() - 1].setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity,
                    R.color.principal
                )
            )
            diaA[ca.getDia() - 1].setTextColor(Color.WHITE)
        }
    }
}