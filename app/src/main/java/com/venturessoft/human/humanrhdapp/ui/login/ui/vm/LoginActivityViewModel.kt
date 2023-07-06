package com.venturessoft.human.humanrhdapp.ui.login.ui.vm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.venturessoft.human.humanrhdapp.ui.login.data.models.ForgotPasswordResponse
import com.venturessoft.human.humanrhdapp.ui.login.data.models.LoginResponse
import com.venturessoft.human.humanrhdapp.ui.login.data.models.UsuariosListaResponse
import com.venturessoft.human.humanrhdapp.ui.login.data.net.EmpleadoListService
import com.venturessoft.human.humanrhdapp.ui.login.data.net.LoginService
import com.venturessoft.human.humanrhdapp.ui.login.ui.LoginActivity
import com.venturessoft.human.humanrhdapp.ui.login.ui.fragments.LoginFragment.Companion.selectedItemIndex
import com.venturessoft.human.humanrhdapp.network.Response.ItemItem
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import kotlinx.coroutines.launch

class LoginActivityViewModel : ViewModel() {

    val respository = LoginService()
    val repositoryEmpleadoList = EmpleadoListService()
    var status = MutableLiveData<ApiResponceStatus<Any>?>(null)
        private set
    var dataLogin = MutableLiveData<LoginResponse?>(null)
        private set
    var dataEmpleadoList = MutableLiveData<UsuariosListaResponse?>(null)
        private set
    var dataForgotNip = MutableLiveData<ForgotPasswordResponse?>(null)
        private set
    fun getLoginService(context: Context, email: String, password: String){
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
                val responce = respository.login(email, password)
                if (responce is ApiResponceStatus.Success){
                    dataLogin.value = responce.data
                    User.nombreSupervisor = responce.data.ssupervisor?.nombre.toString()
                    User.token = responce.data.token.toString()
                    User.usuario = email
                    User.foto = responce.data.foto.toString()
                    val empresa = ArrayList<String>()
                    val itemsCiaNum = ArrayList<Long>()
                    val itemsOrganigrama = ArrayList<Boolean>()

                    for(i in responce.data.ssupervisor?.scia?.indices!!){
                        empresa.add(responce.data.ssupervisor!!.scia?.get(i)?.razonSocial.toString())
                        itemsCiaNum.add(responce.data.ssupervisor!!.scia?.get(i)?.cia!!.toLong())
                        itemsOrganigrama.add(responce.data.ssupervisor!!.scia?.get(i)?.organigrama!!)
                    }
                    if (empresa.isEmpty()){
                        Toast.makeText(context, "Sin compañias", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        var selectedEmpresas = empresa[selectedItemIndex]
                        var selectedCia = itemsCiaNum[selectedItemIndex]
                        var selectedOrganigrama = itemsOrganigrama[selectedItemIndex]

                        MaterialAlertDialogBuilder(context, R.style.AlertDialogCustom)
                            .setTitle(context.getString(R.string.list_company_title))
                            .setSingleChoiceItems(empresa.toTypedArray(), selectedItemIndex) { _, which ->
                                selectedItemIndex = which
                                selectedEmpresas = empresa[which]
                                selectedCia = itemsCiaNum[which]
                                selectedOrganigrama = itemsOrganigrama[which]
                            }
                            .setPositiveButton(context.getString(R.string.list_company_positive)){ _, _ ->
                                User.razonSocial = selectedEmpresas
                                User.numCia = selectedCia
                                User.organigrama = selectedOrganigrama
                                empleadoListService(User.token, User.numCia, User.usuario, size = 30)
                            }
                            .setNeutralButton(context.getString(R.string.list_company_negative)){ _, _ ->
                            }
                            .show()
                    }
                }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
    private fun empleadoListService(token: String, numCia: Long, supervisor: String, size: Long){
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = repositoryEmpleadoList.empleadoList(token, numCia, supervisor, size)
            if (responce is ApiResponceStatus.Success){
                dataEmpleadoList.value = responce.data
                val list = java.util.ArrayList<ItemItem>()
                for (i in responce.data.items?.item?.indices!!){
                    val dataModel=  ItemItem()
                    dataModel.nombreCompleto = responce.data.items.item[i]?.nombreCompleto.toString()
                    dataModel.puesto = responce.data.items.item[i]?.puesto.toString()
                    dataModel.fotografia= responce.data.items.item[i]?.fotografia.toString()
                    dataModel.numEmp= responce.data.items.item[i]?.numEmp.toString()
                    list.add(dataModel)
                    LoginActivity.usuariosListaArrayResponse = list
                    User.listUsu = LoginActivity.usuariosListaArrayResponse
                }
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }

    fun getForgotnNip(email: String){
        viewModelScope.launch {
            status.value = ApiResponceStatus.Loading()
            val responce = respository.reportAsisOrgAt(email)
            if (responce is ApiResponceStatus.Success){
                dataForgotNip.value = responce.data
            }
            status.value = responce as ApiResponceStatus<Any>
        }
    }
}