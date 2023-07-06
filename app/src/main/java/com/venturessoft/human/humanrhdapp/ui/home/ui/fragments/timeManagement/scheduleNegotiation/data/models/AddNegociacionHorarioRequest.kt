package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.scheduleNegotiation.data.models

data class ScheduleNegotiationResponce(
    var codigo: String,
    var resultado: String,
    var sLista: List<ScheduleNegotiation>,
)
data class ScheduleNegotiation(
    var fechaInicio: String,
    var fechaFin: String,
    var rolTurno: Int,
    var turno: Int,
    var dom: String,
    var lun: String,
    var mar: String,
    var mie: String,
    var jue: String,
    var vie: String,
    var sab: String,
    var categoria: String,
    var zona: Long,
    var departamento: String,
    var auditoria: String
):java.io.Serializable
