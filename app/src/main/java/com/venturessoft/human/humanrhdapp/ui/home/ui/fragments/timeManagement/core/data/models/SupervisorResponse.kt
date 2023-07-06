package com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data.models

data class SupervisorResponse(
	val codigo: String,
	val resp: List<RespItem>,
	val mensaje: String,
	val error: Boolean
)

data class RespItem(
	val value: String,
	val key: String
)

