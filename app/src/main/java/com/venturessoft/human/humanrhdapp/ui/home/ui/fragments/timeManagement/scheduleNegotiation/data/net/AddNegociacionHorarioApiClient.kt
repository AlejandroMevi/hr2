package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.net

import com.venturessoft.human.humanrhdapp.network.URL
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models.ScheduleNegotiationResponce
import retrofit2.http.*

interface AddNegociacionHorarioApiClient {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministracionDeTiempos.GETNEGOCIACIONHORARIO)
    suspend fun getScheduleNegotiation(
        @Header("Authorization") token: String,
        @Query("cia") cia: Long,
        @Query("numEmp") numEmp: Long,
        @Query("mes") mes: Int,
        @Query("anio") anio: Int,
    ): ScheduleNegotiationResponce
}