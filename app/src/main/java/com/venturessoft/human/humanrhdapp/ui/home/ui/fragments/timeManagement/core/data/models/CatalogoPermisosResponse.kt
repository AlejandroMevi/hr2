package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.data.models

data class CatalogoPermisosResponse(
	val codigo: String? = null,
	val resp: List<RespItem?>? = null,
	val mensaje: String? = null,
	val error: Boolean? = null
)

data class RespItem(
	val value: String? = null,
	val key: String? = null
)

