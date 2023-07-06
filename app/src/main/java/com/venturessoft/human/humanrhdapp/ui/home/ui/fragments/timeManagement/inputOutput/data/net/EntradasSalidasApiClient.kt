package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.net

import com.venturessoft.human.humanrhdapp.network.URL
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models.GeneralResponse
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.AddEntradasSalidasRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models.InputOutputResponce
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface EntradasSalidasApiClient {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionDeTiempos.GETENTRADASSALIDAS)
    suspend fun getInputOutput(
        @Header("Authorization") token: String,
        @Body inputOutputRequest: InputOutputRequest
    ): InputOutputResponce

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(URL.AdministracionDeTiempos.ADDENTRADASSALIDAS)
    suspend fun getAddEntradasSalidas(
        @Header("Authorization") token: String,
        @Body addEntradasSalidasRequest: AddEntradasSalidasRequest
    ): GeneralResponse
}