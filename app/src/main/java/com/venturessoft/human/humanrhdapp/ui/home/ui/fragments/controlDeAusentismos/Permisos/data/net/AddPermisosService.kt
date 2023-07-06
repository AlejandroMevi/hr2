package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.net

import com.venturessoft.human.humanrhdapp.network.Response.AddPermisosResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.models.AddPermisosRequest
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class AddPermisosService {

    val autservice = RetrofitConnection(TypeServices.BASE).getRetrofit()

    suspend fun addPermisos(
        token: String, addPermisosRequest: AddPermisosRequest
    ): ApiResponceStatus<AddPermisosResponse> {
        return makeNetworkCall {
            val response = autservice.create(AddPermisosApiClient::class.java)
                .getAddPermisosCA(token, addPermisosRequest)
            evaluateResponce(response.codigo.toString())
            response
        }
    }
}