package com.venturessoft.human.humanrhdapp.ui.login.data.net

import com.venturessoft.human.humanrhdapp.ui.login.data.models.UsuariosListaResponse
import com.venturessoft.human.humanrhdapp.network.URL
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface EmpleadoListApiClient {
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET(URL.Login.EMPLEADO_LISTADO)
    suspend fun getEmpleadoList(@Header("Authorization") token: String,
                           @Query("numCia") numCia: Long, @Query("supervisor") supervisor: String,
                           @Query("size") size: Long) : UsuariosListaResponse
}