package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models

data class ReasonsResponce(
    val codigo: String,
    val resp: List<Motivo>,
    val mensaje: String,
    val error: Boolean
)

data class Motivo(
    val key: String,
    val value: String
)
