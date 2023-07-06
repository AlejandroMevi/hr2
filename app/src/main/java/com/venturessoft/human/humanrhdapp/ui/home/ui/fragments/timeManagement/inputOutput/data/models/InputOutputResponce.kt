package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.inputOutput.data.models

data class InputOutputResponce(
    val codigo: String,
    val resultado: String,
    val sLista:List<InputOutput>
):java.io.Serializable

data class InputOutput(
    val fecha: String,
    val sec : Int,
    val entrada: String,
    val estacion: String,
    val concepto: Int
):java.io.Serializable
