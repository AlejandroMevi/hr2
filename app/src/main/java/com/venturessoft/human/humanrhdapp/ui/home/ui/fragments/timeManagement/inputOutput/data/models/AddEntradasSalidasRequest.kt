package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models

data class AddEntradasSalidasRequest(
    var cia: String,
    var numEmp: String,
    var mes: String,
    var anio: String,
    var fecha: String,
    var entrada: String,
    var salida: String,
    var estacion: String,
    var usuario: String
)
