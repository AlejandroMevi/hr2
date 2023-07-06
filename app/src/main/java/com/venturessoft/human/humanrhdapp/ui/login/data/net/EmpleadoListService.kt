package com.venturessoft.human.humanrhdapp.ui.login.data.net

import com.venturessoft.human.humanrhdapp.ui.login.data.models.UsuariosListaResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class EmpleadoListService {

    val autservice = RetrofitConnection(TypeServices.BASE).getRetrofit()

    suspend fun empleadoList(
        token: String,
        numCia: Long,
        supervisor: String,
        size: Long
    ): ApiResponceStatus<UsuariosListaResponse> {
        return makeNetworkCall {
            val response = autservice.create(EmpleadoListApiClient::class.java)
                .getEmpleadoList(token, numCia, supervisor, size)
            evaluateResponce(response.codigo.toString())
            response
        }
    }
}