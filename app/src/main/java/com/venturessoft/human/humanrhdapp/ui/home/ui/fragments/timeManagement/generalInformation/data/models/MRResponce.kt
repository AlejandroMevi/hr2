package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.data.models

data class MRResponce(
    val codigo: String,
    val resultado: String,
    val listaMaestroReloj: List<ListaMaestroReloj>
)
data class ListaMaestroReloj(
    val fechaAplicacion: String,
    val gafete: Int,
    val calendario: Int,
    val rolHorario: Int,
    val turno: Int,
    val dom: String,
    val lun: String,
    val mar: String,
    val mie: String,
    val jue: String,
    val vie: String,
    val sab: String,
    val cond1: String,
    val cond2: String,
    val cond3: String,
    val cond4: String,
    val cond5: String,
    val cond6: String,
    val cond7: String,
    val cond8: String,
    val cond9: String,
    val cond10: String,
    val status: String
):java.io.Serializable

