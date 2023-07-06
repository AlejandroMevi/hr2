package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.net

import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiationResponce
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices
import com.venturessoft.human.humanrhdapp.utilis.complements.User

class AddNegociacionHorarioService {

    val autservice = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun getScheduleNegotiation(numEmp:Long,mes:Int,anio:Int): ApiResponceStatus<ScheduleNegotiationResponce> {
        return makeNetworkCall {
            val response = autservice.create(AddNegociacionHorarioApiClient::class.java).getScheduleNegotiation(User.token,User.numCia,numEmp,mes,anio)
            evaluateResponce(response.codigo)
            response
        }
    }
}