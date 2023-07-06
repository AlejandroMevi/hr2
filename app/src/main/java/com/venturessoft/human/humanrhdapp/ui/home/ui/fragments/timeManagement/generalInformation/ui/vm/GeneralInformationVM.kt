package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.InfoGenralRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.MRResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.UpdateInfoGeneralResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.net.GeneralInfoRep
import kotlinx.coroutines.launch

class GeneralInformationVM : ViewModel() {

    val generalInfoRep = GeneralInfoRep()

    var status = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataMR = MutableLiveData<MRResponce?>(null)
        private set
    var dataResponce = MutableLiveData<GeneralResponse?>(null)
        private set
    var dataResponceUpdate = MutableLiveData<UpdateInfoGeneralResponce?>(null)
        private set
    fun getMR(numEmp:Long) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = generalInfoRep.getMR(numEmp)
            if (responce is ApiResponceStatus.Success) {
                dataMR.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
    fun addAdministrarMR(infoGenralRequest: InfoGenralRequest) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = generalInfoRep.addAdministrarMR(infoGenralRequest)
            if (responce is ApiResponceStatus.Success) {
                dataResponce.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
    fun editAdministrarMR(numEmp:Long,fechaAplicacion:String,infoGenralRequest: InfoGenralRequest) {
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = generalInfoRep.editAdministrarMR(numEmp,fechaAplicacion,infoGenralRequest)
            if (responce is ApiResponceStatus.Success) {
                dataResponceUpdate.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
}