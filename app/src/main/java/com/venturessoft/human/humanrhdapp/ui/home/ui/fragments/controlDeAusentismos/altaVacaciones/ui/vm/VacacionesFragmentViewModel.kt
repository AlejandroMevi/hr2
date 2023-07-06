package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.network.Response.GetVacacionesResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.net.GetVacacionesService
import kotlinx.coroutines.launch

class VacacionesFragmentViewModel : ViewModel() {

    val respositoryGetVacaciones = GetVacacionesService()
    val respositoryAddVacaciones =
        com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.net.AddVacacionesService()

    var statusGetVacaciones = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var statusAddVacaciones = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataGetVacaciones = MutableLiveData<GetVacacionesResponse?>(null)
        private set
    var dataAddVacaciones = MutableLiveData<com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.models.AddVacacionesResponse?>(null)
        private set

    fun getVacaciones(token: String, numCia: Long, numEmp: Long, anio: Int) {
        viewModelScope.launch {
            statusGetVacaciones.value = ApiResponceStatus.Loading()
            val responce = respositoryGetVacaciones.getVacaciones(token, numCia, numEmp, anio)
            if (responce is ApiResponceStatus.Success) {
                dataGetVacaciones.value = responce.data
            }
            statusGetVacaciones.value = responce as ApiResponceStatus<Any>
        }
    }

    fun addVacaciones(token: String, addVacacionesRequest: com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.models.AddVacacionesRequest) {
        viewModelScope.launch {
            statusAddVacaciones.value = ApiResponceStatus.Loading()
            val responce = respositoryAddVacaciones.getVacaciones(token, addVacacionesRequest)
            if (responce is ApiResponceStatus.Success) {
                dataAddVacaciones.value = responce.data
            }
            statusAddVacaciones.value = responce as ApiResponceStatus<Any>
        }
    }
}
