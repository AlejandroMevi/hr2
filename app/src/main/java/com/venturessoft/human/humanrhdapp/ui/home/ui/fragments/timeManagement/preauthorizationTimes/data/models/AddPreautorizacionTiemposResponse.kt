package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.preauthorizationTimes.data.models
data class AddPreautorizacionTiemposResponse(
    val codigo: String,
    val resultado: String,
    val sTiempoExtra:List<TiempoExtra>
)
data class TiempoExtra(
    var fecha:String,
    var horaEntrada:String,
    var horaSalida:String,
    var horas:String,
    var categoria:String,
    var zona:String,
    var departamento:String,
    var isExpanded:Boolean = false
)

