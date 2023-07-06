package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.net
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.AddEntradasSalidasRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputResponce
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices
import com.venturessoft.human.humanrhdapp.utilis.complements.User

class EntradasSalidasService {

    val autservice = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun getInputOutput(inputOutputRequest: InputOutputRequest): ApiResponceStatus<InputOutputResponce> {
        return makeNetworkCall {
            val response = autservice.create(EntradasSalidasApiClient::class.java).getInputOutput(User.token, inputOutputRequest)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun entradasSalidas(token: String, addEntradasSalidasRequest: AddEntradasSalidasRequest, ): ApiResponceStatus<GeneralResponse> {
        return makeNetworkCall {
            val response = autservice.create(EntradasSalidasApiClient::class.java).getAddEntradasSalidas(token, addEntradasSalidasRequest)
            evaluateResponce(response.codigo)
            response
        }
    }
}