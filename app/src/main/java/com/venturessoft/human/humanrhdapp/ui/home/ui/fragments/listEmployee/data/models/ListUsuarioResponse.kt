package com.venturessoft.human.humanrhdapp.network.Response

import com.google.gson.annotations.SerializedName

class ListUsuarioResponse{

	@field:SerializedName("totalRegistros")
	val totalRegistros: Int? = null

	@field:SerializedName("codigo")
	val codigo: String? = null

	@field:SerializedName("item")
	val item: List<ItemItem?>? = null

	@field:SerializedName("paginaActual")
	val paginaActual: Int? = null

	@field:SerializedName("last")
	val last: Boolean? = null

	@field:SerializedName("hasPrevious")
	val hasPrevious: Boolean? = null

	@field:SerializedName("hasNext")
	val hasNext: Boolean? = null

	@field:SerializedName("totalPaginas")
	val totalPaginas: Int? = null

	@field:SerializedName("first")
	val first: Boolean? = null
	}

data class ItemItem(

	@field:SerializedName("puesto")
	var puesto: String? = null,

	@field:SerializedName("numEmp")
	var numEmp: String? = null,

	@field:SerializedName("nombreCompleto")
	var nombreCompleto: String? = null,

	@field:SerializedName("fotografia")
	var fotografia: String? = null
)
