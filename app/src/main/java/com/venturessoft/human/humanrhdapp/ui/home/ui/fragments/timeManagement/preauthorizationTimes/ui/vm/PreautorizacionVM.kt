package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.AddPreautorizacionTiemposResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.PreautorizacionRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.net.AddPreauTiemposService
import kotlinx.coroutines.launch

class PreautorizacionVM : ViewModel() {
    val respository = AddPreauTiemposService()

    var status = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataPreauthorization = MutableLiveData<AddPreautorizacionTiemposResponse?>(null)
        private set
    fun addPreauTiempos(token: String, preautorizacionRequest: PreautorizacionRequest) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = respository.addPreauTiempos(token, preautorizacionRequest)
            if (responce is ApiResponceStatus.Success) {
                dataPreauthorization.value = responce.data
            }else{
                dataPreauthorization.value = null
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
}