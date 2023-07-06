package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.altaVacaciones.data.net

import com.venturessoft.human.humanrhdapp.network.Response.GetVacacionesResponse
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class GetVacacionesService {

    val autservice = RetrofitConnection(TypeServices.BASE).getRetrofit()

    suspend fun getVacaciones(
        token: String, numCia: Long, numEmp: Long, anio: Int
    ): ApiResponceStatus<GetVacacionesResponse> {
        return makeNetworkCall {
            val response = autservice.create(GetVacacionesApiClient::class.java)
                .getVacacionesCA(token, numCia, numEmp, anio)
            evaluateResponce(response.codigo.toString())
            response
        }
    }
}