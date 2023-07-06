package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.AddEntradasSalidasRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.net.EntradasSalidasService
import kotlinx.coroutines.launch

class InputOutputVM : ViewModel() {

    val respository = EntradasSalidasService()
    var status = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataInputOutput = MutableLiveData< InputOutputResponce?>(null)
        private set
    var dataEntradasSalidas = MutableLiveData<GeneralResponse?>(null)
        private set

    fun getInputOutput(inputOutputRequest: InputOutputRequest) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = respository.getInputOutput(inputOutputRequest)
            if (responce is ApiResponceStatus.Success) {
                dataInputOutput.value = responce.data
            }else{
                dataInputOutput.value = null
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }

    fun addEntradasSalidas(token: String, addEntradasSalidasRequest: AddEntradasSalidasRequest) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = respository.entradasSalidas(token, addEntradasSalidasRequest)
            if (responce is ApiResponceStatus.Success) {
                dataEntradasSalidas.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
}