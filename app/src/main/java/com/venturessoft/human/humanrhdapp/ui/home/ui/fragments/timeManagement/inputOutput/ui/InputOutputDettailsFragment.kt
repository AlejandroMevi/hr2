package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.venturessoft.human.humanrhdapp.databinding.FragmentInputOutputDettailsBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutput
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants.Companion.DATA_KEY
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities

class InputOutputDettailsFragment : Fragment() {

    private lateinit var binding: FragmentInputOutputDettailsBinding
    private val welcomeVM: WelcomeFragmentViewModel by activityViewModels()
    private var inputOutput: InputOutput?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            inputOutput = bundle.getSerializable(DATA_KEY) as InputOutput
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentInputOutputDettailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHintSpinner()
        welcomeVM.idMenu.observe(viewLifecycleOwner) { dataUser ->
            binding.tvNameEmployee.text = dataUser.nombreCompleto
            binding.tvNameEmployee.text = "Nombre: ${dataUser.nombreCompleto}"
            binding.tvNumEmployee.text = "Num Empleado: ${dataUser.numEmp}"
            if (inputOutput !=null){
                binding.tvDate.text = "Fecha: ${Utilities.formatYearMonthDay(inputOutput?.fecha.toString())}"
                binding.tvConcept.text = "Concepto: ${inputOutput?.concepto.toString()}"
                binding.tvTurn.text = "Turno: ${inputOutput?.sec.toString()}"
                binding.tvInputText.text = inputOutput?.entrada.toString()
            }else{
                binding.tvDate.text = "Fecha: ${Utilities.getDateLocal()}"
            }
        }
    }
    private fun setHintSpinner(){
        binding.spStation.inputLayout.hint = "Estacion"
        binding.spStatus.inputLayout.hint = "Status"
    }
}