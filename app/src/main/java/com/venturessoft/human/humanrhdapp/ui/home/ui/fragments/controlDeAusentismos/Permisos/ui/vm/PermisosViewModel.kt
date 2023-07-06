package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venturessoft.human.humanrhdapp.network.Response.AddPermisosResponse
import com.venturessoft.human.humanrhdapp.network.Response.GetMotivoPermisosResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.models.AddPermisosRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.net.AddPermisosService
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.net.GetPermisosService
import kotlinx.coroutines.launch

class PermisosViewModel : ViewModel() {

    val respository = GetPermisosService()
    val respositoryAddPermisos = AddPermisosService()

    var statusGetPermisos = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var statusAddPermisos = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataGetPermisos = MutableLiveData<GetMotivoPermisosResponse?>(null)
        private set
    var dataAddPermisos = MutableLiveData<AddPermisosResponse?>(null)
        private set

    fun getPermisos(token: String, numCia: Long) {
        viewModelScope.launch {
            statusGetPermisos.value = ApiResponceStatus.Loading()
            val responce = respository.getPermisos(token, numCia)
            if (responce is ApiResponceStatus.Success) {
                dataGetPermisos.value = responce.data
            }
            statusGetPermisos.value = responce as ApiResponceStatus<Any>
        }
    }

    fun addPermisos(
        token: String, addPermisosRequest: AddPermisosRequest
    ) {
        viewModelScope.launch {
            statusAddPermisos.value = ApiResponceStatus.Loading()
            val responce = respositoryAddPermisos.addPermisos(token, addPermisosRequest)
            if (responce is ApiResponceStatus.Success) {
                dataAddPermisos.value = responce.data
            }
            statusAddPermisos.value = responce as ApiResponceStatus<Any>
        }
    }
}