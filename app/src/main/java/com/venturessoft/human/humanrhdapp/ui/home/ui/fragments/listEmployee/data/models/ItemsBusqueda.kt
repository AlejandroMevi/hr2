package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.listEmployee.data.models

class ItemsBusqueda (
    val totalRegistros: Int,
    val item: List<ItemItemBusqeda?>,
    val paginaActual: Int,
    val last: Boolean,
    val hasPrevious: Boolean,
    val hasNext: Boolean,
    val totalPaginas: Int,
    val first: Boolean
)