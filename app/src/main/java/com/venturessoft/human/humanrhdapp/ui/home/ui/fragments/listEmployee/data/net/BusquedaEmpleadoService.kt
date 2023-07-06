package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.data.net

import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.data.models.BusquedaUsuarioResponse
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class BusquedaEmpleadoService {

    val autservice = RetrofitConnection(TypeServices.BASE).getRetrofit()

    suspend fun busquedEmpleado(
        token: String, numCia: Long, supervisor: String, size: Long, cadenaBusqueda: String,
        page: Long, area: String? = null, centro: String? = null, linea: String? = null
    ): ApiResponceStatus<BusquedaUsuarioResponse> {
        return makeNetworkCall {
            val response = autservice.create(BusquedaEmpleadoApiClient::class.java)
                .getBusquedaEmpleadoList(
                    token,
                    numCia,
                    supervisor,
                    size,
                    cadenaBusqueda,
                    page,
                    area,
                    centro,
                    linea
                )
            response
        }
    }
}