package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.AnimationExpanded
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.FragmentEntradasSalidas2Binding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.DateIndicator
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutput
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.ui.vm.InputOutputVM
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.DATA_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import java.util.*


class EntradasSalidasFragment : Fragment(), InputOutputAdapter.OnClickListener {

    private lateinit var binding: FragmentEntradasSalidas2Binding
    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private val inputOutputVM: InputOutputVM by  activityViewModels()
    private var inputOutputRequest = InputOutputRequest()
    private var mainInterface: MainInterface? = null
    private var isPeriod = false
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntradasSalidas2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateIndicator = DateIndicator(requireContext(), binding.btnDate)
        dateIndicator.setDate(DateIndicator.monthData).toString()
        statusObserve()
        welcomeVM.idMenu.observe(viewLifecycleOwner){ userData->
            inputOutputRequest.cia = User.numCia.toString()
            inputOutputRequest.numEmp = "1"
            inputOutputRequest.proceso = "1"
            inputOutputRequest.mes = calendar.get(Calendar.MONTH).toString()
            inputOutputRequest.anio = calendar.get(Calendar.YEAR).toString()
            inputOutputVM.getInputOutput(inputOutputRequest)
        }
        inputOutputVM.dataInputOutput.observe(viewLifecycleOwner){data->
            if (data != null){
                binding.tvDataEmpty.isVisible = false
                binding.rvInputOutput.adapter = InputOutputAdapter(data.sLista, this)
            }else{
                binding.tvDataEmpty.isVisible = true
                binding.rvInputOutput.adapter = InputOutputAdapter(listOf(), this)
            }
        }

        binding.btnDate.setOnClickListener {
            dateIndicator.createDialogWithoutDateField()
        }

        binding.floating.setOnClickListener {
            findNavController().navigate(R.id.to_inputOutputDettailsFragment)
        }

        binding.rgType.setOnCheckedChangeListener { radioGroup, _ ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.rb_month -> isPeriod = false
                R.id.rb_period -> isPeriod = true
            }
        }

        binding.btnFilter.setOnClickListener {
            inputOutputRequest.periodo = isPeriod
            inputOutputRequest.mes = DateIndicator.monthData.toString()
            inputOutputRequest.anio = DateIndicator.yearData.toString()
            inputOutputVM.getInputOutput(inputOutputRequest)
        }

        binding.rvInputOutput.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){
                    binding.floating.shrink()
                    if(isExpanded){
                        val show: Boolean = toggleLayout(false, binding.viewMoreBtn, binding.layoutExpand)
                        isExpanded = show
                    }
                } else {
                    binding.floating.extend()
                }
            }
        })

        binding.cardviewlista.setOnClickListener {
            val show: Boolean = toggleLayout(!isExpanded, binding.viewMoreBtn, binding.layoutExpand)
            isExpanded = show
        }
    }

    internal fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: View): Boolean {
        AnimationExpanded().toggleArrow(v, isExpanded)
        if (isExpanded) {
            AnimationExpanded().expand(layoutExpand)
        } else {
            AnimationExpanded().collapse(layoutExpand)
        }
        return isExpanded
    }
    override fun onClick(inputOutput: InputOutput) {
        val bundle = Bundle()
        bundle.putSerializable(DATA_KEY,inputOutput)
        findNavController().navigate(R.id.to_inputOutputDettailsFragment,bundle)
    }

    private fun statusObserve() {
        inputOutputVM.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        mainInterface?.showLoading(true)
                    }
                    is ApiResponceStatus.Success -> {
                        clearService()
                    }
                    is ApiResponceStatus.Error -> {
                        clearService()
                    }
                }
            }
        }
    }
    private fun clearService() {
        inputOutputVM.status.removeObservers(viewLifecycleOwner)
        inputOutputVM.status.value = null
        mainInterface?.showLoading(false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}