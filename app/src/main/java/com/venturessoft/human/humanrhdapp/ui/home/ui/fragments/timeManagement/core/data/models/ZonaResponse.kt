package com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data

data class ZonaResponse(
    val codigo: String,
    val resp: List<RespItem>,
    val mensaje: String,
    val error: Boolean
)

data class RespItem(
	val value: String,
	val key: String
)

