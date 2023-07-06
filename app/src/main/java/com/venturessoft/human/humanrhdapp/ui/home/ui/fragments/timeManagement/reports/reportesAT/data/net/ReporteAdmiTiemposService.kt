package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.net

import android.content.Context
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.core.RetrofitConnection
import com.venturessoft.human.humanrhdapp.core.evaluateResponce
import com.venturessoft.human.humanrhdapp.core.makeNetworkCall
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models.ReporteAdmiTiemposRequest
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models.ReporteAdmiTiemposResponse
import com.venturessoft.human.humanrhdapp.utilis.complements.TypeServices

class ReporteAdmiTiemposService {
    private val autserviceReport = RetrofitConnection(TypeServices.REPORTESAT).getRetrofit()

    suspend fun reporteAdmiTiempos(
        typeReport: String,
        reportRequest: ReporteAdmiTiemposRequest,
        context: Context
    ): ApiResponceStatus<ReporteAdmiTiemposResponse> {
        return makeNetworkCall {
            val response = when (typeReport) {
                context.getString(R.string.ATAsistencia_Organigrama) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).getReportAsistOrgAt(
                    reportRequest.token.toString(),
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea,
                    reportRequest.zona,
                    reportRequest.turno,
                    reportRequest.rol
                )

                context.getString(R.string.ATAusentismo) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reporteAusentismosAT(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.retardos,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea
                )

                context.getString(R.string.ATControl_Asistencia) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reporteControlAsistenciaAT(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.grupo,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea
                )

                context.getString(R.string.ATentradas_salidas_x_concepto) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reportEentradasSalidasXConceptoAT(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea,
                    reportRequest.supervisor,
                    reportRequest.descripcionPorEmpleado,
                    reportRequest.totalDepartamento,
                    reportRequest.conceptos
                )

                context.getString(R.string.ATentradas_salidas) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reportEentradasSalidasAT(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea,
                    reportRequest.supervisor,
                    reportRequest.impresionPorEmpleado,
                    reportRequest.empleadosInactivo,
                    reportRequest.mrActiva
                )

                context.getString(R.string.ATHoras_laboradas) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reporteHorasLaboradas(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea
                )

                context.getString(R.string.ATtarjeta_empleados) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reporteTarjetaEmpleadosAT(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.fechaTermino,
                    reportRequest.fechaInicio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea
                )

                context.getString(R.string.ATDias_por_Disfrutar) -> autserviceReport.create(
                    ReporteAdmiTiemposApiClient::class.java
                ).reporteDiasXDisfrutar(
                    reportRequest.token.toString(),
                    reportRequest.numEmp,
                    reportRequest.cia,
                    reportRequest.anio,
                    reportRequest.usuario,
                    reportRequest.area,
                    reportRequest.centro,
                    reportRequest.linea,
                    reportRequest.codId
                )

                else -> ReporteAdmiTiemposResponse("", "", "")
            }
            evaluateResponce(response.codigo)
            response
        }
    }
}