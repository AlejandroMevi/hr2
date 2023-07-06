package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.Permisos.data.net

import com.venturessoft.human.humanrhdapp.network.Response.GetMotivoPermisosResponse
import com.venturessoft.human.humanrhdapp.network.URL
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GetPermisosApiClient {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.ControlDeAusentismos.GETMOTIVOPERM)
    suspend fun getPermisos(
        @Header("Authorization") token: String,
        @Query("cia") cia: Long
    ): GetMotivoPermisosResponse

}