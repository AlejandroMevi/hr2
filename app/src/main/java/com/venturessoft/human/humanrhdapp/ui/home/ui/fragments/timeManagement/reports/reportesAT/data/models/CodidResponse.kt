package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.reports.reportesAT.data.models

import com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data.models.RespItem

data class CodidResponse(
	val codigo: String? = null,
	val resp: List<RespItem?>? = null,
	val mensaje: String? = null,
	val error: Boolean? = null
)

