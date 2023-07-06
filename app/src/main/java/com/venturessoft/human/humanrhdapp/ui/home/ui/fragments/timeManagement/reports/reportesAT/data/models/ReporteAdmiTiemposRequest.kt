package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models

data class ReporteAdmiTiemposRequest(
    var token: String? = null,
    var numEmp: List<Long>? = null,
    var cia: Long,
    var fechaTermino: String,
    var fechaInicio: String,
    var usuario: String,
    var retardos: Boolean? = null,
    var area: String? = null,
    var centro: String? = null,
    var linea: String? = null,
    var grupo: String? = null,
    var zona: Long? = null,
    var turno: Int? = null,
    var rol: Int? = null,
    var empleadosInactivo: Boolean? = null,
    var mrActiva: Boolean? = null,
    var impresionPorEmpleado: Boolean? = null,
    var totalDepartamento: Boolean? = null,
    var descripcionPorEmpleado: Boolean? = null,
    var conceptos: ArrayList<Int>? = null,
    var supervisor: String? = null,
    var codId: Long? = null,
    var anio: Int? = null
)
