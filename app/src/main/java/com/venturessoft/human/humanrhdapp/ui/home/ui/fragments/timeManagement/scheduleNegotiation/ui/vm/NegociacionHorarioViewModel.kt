package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiationResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.net.AddNegociacionHorarioService
import kotlinx.coroutines.launch

class NegociacionHorarioViewModel : ViewModel() {

    val respository = AddNegociacionHorarioService()

    var status = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataScheduleNegotiation = MutableLiveData<ScheduleNegotiationResponce?>(null)
        private set

    fun getScheduleNegotiation(numEmp:Long,mes:Int,anio:Int) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = respository.getScheduleNegotiation(numEmp, mes,anio)
            if (responce is ApiResponceStatus.Success) {
                dataScheduleNegotiation.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
}