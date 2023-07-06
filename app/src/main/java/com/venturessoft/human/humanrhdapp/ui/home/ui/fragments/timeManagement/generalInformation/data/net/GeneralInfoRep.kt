package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.net

import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.InfoGenralRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.MRResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.UpdateInfoGeneralResponce
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices
import com.venturessoft.human.humanrhdapp.utilis.complements.User

class GeneralInfoRep {

    val autservice = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun getMR(numEmp: Long): ApiResponceStatus<MRResponce> {
        return makeNetworkCall {
            val response = autservice.create(GeneralInfoApi::class.java).getMR(User.token,User.numCia,numEmp)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun addAdministrarMR(infoGenralRequest: InfoGenralRequest): ApiResponceStatus<GeneralResponse> {
        return makeNetworkCall {
            val response = autservice.create(GeneralInfoApi::class.java).addAdministrarMR(User.token,infoGenralRequest)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun editAdministrarMR(numEmp:Long,fechaAplicacion:String,infoGenralRequest: InfoGenralRequest): ApiResponceStatus<UpdateInfoGeneralResponce> {
        return makeNetworkCall {
            val response = autservice.create(GeneralInfoApi::class.java).editAdministrarMR(User.token,User.numCia,numEmp,fechaAplicacion,infoGenralRequest)
            evaluateResponce(response.status.toString())
            response
        }
    }
}