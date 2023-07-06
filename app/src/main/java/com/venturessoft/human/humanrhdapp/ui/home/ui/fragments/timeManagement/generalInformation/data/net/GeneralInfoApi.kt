package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.net


import com.venturessoft.human.humanrhdapp.network.URL
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.InfoGenralRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.MRResponce
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models.UpdateInfoGeneralResponce
import retrofit2.http.*

interface GeneralInfoApi {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.AdministracionDeTiempos.MR)
    suspend fun getMR(
        @Header("Authorization") token: String,
        @Query("cia") cia: Long,
        @Query("numEmp") numEmp: Long,
    ): MRResponce

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionDeTiempos.ADDINFORMACIONGENERAL)
    suspend fun addAdministrarMR(
        @Header("Authorization") token: String,
        @Body infoGenralRequest: InfoGenralRequest,
    ): GeneralResponse

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT(URL.AdministracionDeTiempos.EDITINFORMACIONGENERAL)
    suspend fun editAdministrarMR(
        @Header("Authorization") token: String,
        @Query("cia") cia: Long,
        @Query("numEmp") numEmp: Long,
        @Query("fechaAplicacion") fechaAplicacion : String,
        @Body infoGenralRequest: InfoGenralRequest,
    ): UpdateInfoGeneralResponce
}