package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.net

import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.AddPreautorizacionTiemposResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.PreautorizacionRequest
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class AddPreauTiemposService {

    val autservice = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun addPreauTiempos(token: String, preautorizacionRequest: PreautorizacionRequest): ApiResponceStatus<AddPreautorizacionTiemposResponse> {
        return makeNetworkCall {
            val response = autservice.create(AddPreauTiemposApiClient::class.java).addPreauTiempos(token, preautorizacionRequest.cia,preautorizacionRequest.numEmp,preautorizacionRequest.mes,preautorizacionRequest.anio)
            evaluateResponce(response.codigo)
            response
        }
    }
}