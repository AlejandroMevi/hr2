package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models

data class DepartamentResponce(
    val codigo: String,
    val resultado: String,
    val calendario: List<Departament>
)

data class Departament(
    val claveCalendario: Int,
    val descripcionCalendario: String,
)
