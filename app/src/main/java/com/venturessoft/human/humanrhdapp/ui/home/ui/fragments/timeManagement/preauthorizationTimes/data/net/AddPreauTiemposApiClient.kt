package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.net

import com.venturessoft.human.humanrhdapp.network.URL
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models.AddPreautorizacionTiemposResponse
import retrofit2.http.*

interface AddPreauTiemposApiClient {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministracionDeTiempos.ADDPREAUTORIZACIONTIEMPOS)
    suspend fun addPreauTiempos(
        @Header("Authorization") token: String,
        @Query("cia") cia: Int,
        @Query("numEmp") numEmp: Int,
        @Query("mes") mes: Int,
        @Query("anio") anio: Int
    ) : AddPreautorizacionTiemposResponse
}