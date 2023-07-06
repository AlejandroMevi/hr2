package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.net

import com.venturessoft.human.humanrhdapp.network.Response.GetMotivoPermisosResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class GetPermisosService {

    val autservice = RetrofitConnection(TypeServices.BASE).getRetrofit()

    suspend fun getPermisos(
        token: String, numCia: Long
    ): ApiResponceStatus<GetMotivoPermisosResponse> {

        return makeNetworkCall {
            val response = autservice.create(GetPermisosApiClient::class.java)
                .getPermisos(token, numCia)

            evaluateResponce(response.codigo.toString())
            response
        }
    }
}