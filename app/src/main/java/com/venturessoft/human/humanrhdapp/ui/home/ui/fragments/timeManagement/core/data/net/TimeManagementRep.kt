package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.net

import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.*
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models.CodidResponse
import com.venturessoft.human.humanrhdapp.uiFragment.controlDeAusentismos.reportes.reportHolidayControlAusentismo.data.models.AreaCentroLineaResponse
import com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.generalInformation.data.net.TimeManagementApi
import com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data.ZonaResponse
import com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data.models.SupervisorResponse
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import kotlinx.coroutines.*

class TimeManagementRep {

    val autservice = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun getRolTurn(): ApiResponceStatus<List<Any>> =
        withContext(Dispatchers.IO) {
            try {
                val deferreds = listOf(
                    async { autservice.create(TimeManagementApi::class.java).getCatalogoRol(User.token, User.numCia) },
                    async { autservice.create(TimeManagementApi::class.java).getCatalogoTurno(User.token, User.numCia) }
                )
                ApiResponceStatus.Success(deferreds.awaitAll())
            } catch (exception: java.lang.Exception) {
                ApiResponceStatus.Error(exception.message.toString())
            }
        }

    suspend fun getAreaCentroLinea(area: String?, centro: String?): ApiResponceStatus<AreaCentroLineaResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getAreaCentroLinea(User.token, User.numCia, User.usuario, area, centro)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun getConceptos(): ApiResponceStatus<ConceptosResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getConceptos(User.token, User.numCia)
            evaluateResponce(response.codigo.toString())
            response
        }
    }

    suspend fun getZona(): ApiResponceStatus<ZonaResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getZona(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun getSupervisor(): ApiResponceStatus<SupervisorResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getSupervisor(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun getReasons(): ApiResponceStatus<ReasonsResponce> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getMotivos(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun getCalendar(): ApiResponceStatus<CalendarResponce> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getCalendar(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }
    suspend fun getDepartment(): ApiResponceStatus<DepartamentResponce> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getDepartment(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }
    suspend fun getCategory(): ApiResponceStatus<CategoryResponce> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).getCategory(User.token, User.numCia)
            evaluateResponce(response.codigo)
            response
        }
    }

    suspend fun codid(): ApiResponceStatus<CodidResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).codid(User.token, User.numCia, User.usuario)
            evaluateResponce(response.codigo.toString())
            response
        }
    }
    suspend fun catalogoPermisos(): ApiResponceStatus<CatalogoPermisosResponse> {
        return makeNetworkCall {
            val response = autservice.create(TimeManagementApi::class.java).catalogoPermisos(User.token, User.numCia)
            evaluateResponce(response.codigo.toString())
            response
        }
    }
}