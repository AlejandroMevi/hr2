package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models

import com.venturessoft.human.humanrhdapp.uiFragment.timeManagement.reports.reportesAT.data.models.RespItem

data class ConceptosResponse(
	val codigo: String? = null,
	val resp: List<RespItem?>? = null,
	val mensaje: String? = null,
	val error: Boolean? = null
)

