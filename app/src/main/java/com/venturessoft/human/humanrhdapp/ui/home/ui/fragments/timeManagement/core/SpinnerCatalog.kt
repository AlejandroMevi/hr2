package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.core.vm.TimeManagementVM
import com.venturessoft.human.humanrhdapp.uiFragment.controlDeAusentismos.reportes.reportHolidayControlAusentismo.data.models.Resp
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.observeOnce

class SpinnerCatalog(private var viewModel: TimeManagementVM, private val fragment: Fragment) {
    companion object {
        var rol: Int? = null
        var turno: Int? = null
        var supervisor: String? = null
        var zona: Long? = null
        var reason: String? = null
        var calendar: Long? = null
        var codid: Long? = null
    }

    fun getCatalogoRolService(spinner: AutoCompleteTextView, keyData: Int? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<Int>()
        var keyposition = 0
        viewModel.dataGetCatalogoRol.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.rolHorario!!.indices) {
                        arrayCatalog.add(response.rolHorario[i]?.descripcionRolHorario ?: "")
                        response.rolHorario[i]?.claveRolHorario?.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            rol = if (position == 0) null else key[position]
        }
    }

    fun getCatalogoTurnoService(spinner: AutoCompleteTextView, keyData: Int? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<Int>()
        var keyposition = 0
        viewModel.dataGetCatalogoTurno.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.turno!!.indices) {
                        arrayCatalog.add(response.turno[i]?.descripcionTurno ?: "")
                        response.turno[i]?.claveTurno?.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            turno = if (position == 0) null else key[position]
        }
    }

    fun supervisorService(spinner: MaterialAutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        supervisor = null
        viewModel.dataSupervisor.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp.indices) {
                        arrayCatalog.add(response.resp[i].value)
                        response.resp[i].key.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            supervisor = if (position == 0) null else key[position]
        }
    }

    fun getCatalogoZonaService(spinner: MaterialAutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        viewModel.dataZona.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp.indices) {
                        arrayCatalog.add(response.resp[i].value)
                        response.resp[i].key.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            zona = if (position == 0) null else key[position].toLong()
        }
    }

    fun getCatalogoReasons(spinner: MaterialAutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        viewModel.dataReasons.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp.indices) {
                        arrayCatalog.add(response.resp[i].value)
                        response.resp[i].key.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            reason = if (position == 0) null else key[position]
        }
    }

    fun getCatalogoCalendar(spinner: AutoCompleteTextView, keyData: Int? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<Int>()
        var keyposition = 0
        viewModel.dataCalendar.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.calendario.indices) {
                        arrayCatalog.add(response.calendario[i].descripcionCalendario)
                        response.calendario[i].claveCalendario.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            calendar = if (position == 0) null else key[position].toLong()
        }
    }

    fun getCatalogoDepartament(spinner: AutoCompleteTextView, keyData: Int? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<Int>()
        var keyposition = 0
        viewModel.dataDepartament.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.calendario.indices) {
                        arrayCatalog.add(response.calendario[i].descripcionCalendario)
                        response.calendario[i].claveCalendario.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            calendar = if (position == 0) null else key[position].toLong()
        }
    }

    fun getCatalogoCategory(spinner: AutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        viewModel.dataCategory.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp.indices) {
                        arrayCatalog.add(response.resp[i].value)
                        response.resp[i].key.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            calendar = if (position == 0) null else key[position].toLong()
        }
    }

    fun codidService(spinner: MaterialAutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        viewModel.dataCodid.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp!!.indices) {
                        arrayCatalog.add(response.resp[i]?.value ?: "")
                        response.resp[i]?.key?.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            key.add(it1)
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            codid = if (position == 0) null else key[position].toLong()
        }
    }

    //Todo Area centro linea

    private var lisSpinner: List<MaterialAutoCompleteTextView> = listOf()
    private var posArea = -1
    private var posCentro = -1
    private val listArea = mutableListOf<Resp>()
    private val listCentro = mutableListOf<Resp>()
    private val listLinea = mutableListOf<Resp>()
    private val listSelection = mutableListOf<Resp>()
    private var typeAreaCentroLinea = Constants.AREA

    fun getListSelect(): List<Resp> {
        if (listSelection.size != 3) {
            for (i in listSelection.size..2) {
                listSelection.add(Resp("", ""))
            }
        }
        return listSelection
    }

    fun loadServiceArea(lisSpinnerFragment: List<MaterialAutoCompleteTextView>) {
        viewModel.getAreaCentroLinea()
        lisSpinner = lisSpinnerFragment
        typeAreaCentroLinea = Constants.AREA
        getAreaCentroLinea()
    }

    private fun loadSpinerArea() {
        clearArea()
        lisSpinner[0].setOnItemClickListener { _, _, position, _ ->
            if (posArea != position) {
                if (position == 0) {
                    clearArea()
                    posArea = 0
                } else {
                    if (listArea.isNotEmpty()) {
                        listSelection.clear()
                        posArea = position
                        clearArea()
                        listSelection.add(0, listArea[position])
                        loadServiceCentro(listArea[position].key)
                    }
                }
            }
        }
    }

    private fun clearArea() {
        listCentro.clear()
        listLinea.clear()
        lisSpinner[1].setText("")
        lisSpinner[2].setText("")
        lisSpinner[1].setAdapter(null)
        lisSpinner[2].setAdapter(null)
        posCentro = -1
    }

    private fun loadServiceCentro(keyArea: String) {
        typeAreaCentroLinea = Constants.CENTRO
        viewModel.getAreaCentroLinea(keyArea)
        getAreaCentroLinea(keyArea)
    }

    private fun loadSpinerCentro(keyArea: String?) {
        lisSpinner[1].setOnItemClickListener { _, _, position, _ ->
            if (posCentro != position) {
                if (position == 0) {
                    clearCentro()
                    posCentro = 0
                } else {
                    if (listCentro.isNotEmpty()) {
                        posCentro = position
                        clearCentro()
                        if (listSelection.size >= 2) {
                            listSelection.removeAt(1)
                        }
                        listSelection.add(1, listCentro[position])
                        loadServiceLinea(keyArea, listCentro[position].key)
                    }
                }
            }
        }
    }

    private fun clearCentro() {
        listLinea.clear()
        lisSpinner[2].setText("")
        lisSpinner[2].setAdapter(null)
    }

    private fun loadServiceLinea(keyArea: String?, keyCentro: String) {
        typeAreaCentroLinea = Constants.LINEA
        viewModel.getAreaCentroLinea(keyArea, keyCentro)
        getAreaCentroLinea()
    }

    private fun loadSpinerLinea() {
        lisSpinner[2].setOnItemClickListener { _, _, position, _ ->
            if (listLinea.isNotEmpty()) {
                if (listSelection.size >= 3) {
                    listSelection.removeAt(2)
                }
                listSelection.add(2, listLinea[position])
            }
        }
    }

    private fun getAreaCentroLinea(keyArea: String? = null) {
        viewModel.dataAreaCentroLinea.observeOnce(fragment) { response ->
            if (response != null) {
                when (typeAreaCentroLinea) {
                    Constants.AREA -> {
                        setSpinnerData(response.resp, listArea, lisSpinner[0])
                        loadSpinerArea()
                    }

                    Constants.CENTRO -> {
                        setSpinnerData(response.resp, listCentro, lisSpinner[1])
                        loadSpinerCentro(keyArea)
                    }

                    Constants.LINEA -> {
                        setSpinnerData(response.resp, listLinea, lisSpinner[2])
                        loadSpinerLinea()
                    }
                }
            }
        }
    }

    private fun setSpinnerData(
        resp: List<Resp>,
        list: MutableList<Resp>,
        spinner: MaterialAutoCompleteTextView
    ) {
        list.clear()
        val listSpinner = mutableListOf<String>()
        list.add(Resp("", ""))
        listSpinner.add(fragment.getString(R.string.none))
        for (i in resp.indices) {
            list.add(resp[i])
            listSpinner.add(resp[i].value)
        }
        if (list.isEmpty()) {
            Toast.makeText(
                fragment.requireContext(),
                fragment.getString(R.string.sin_infomacion),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val arrayAdp = ArrayAdapter(
                fragment.requireContext(),
                android.R.layout.simple_list_item_1,
                listSpinner
            )
            spinner.setAdapter(arrayAdp)
            spinner.setText(listSpinner[0], false)
        }
    }

    fun getCatalogoPermisos(spinner: MaterialAutoCompleteTextView, keyData: String? = null) {
        val arrayCatalog = ArrayList<String>()
        val key = ArrayList<String>()
        var keyposition = 0
        viewModel.dataCatalogoPermisos.observeOnce(fragment) { response ->
            if (response != null) {
                arrayCatalog.add(fragment.getString(R.string.none))
                if (response.codigo == "0") {
                    for (i in response.resp!!.indices) {
                        response.resp[i]?.value?.let { arrayCatalog.add(it) }
                        response.resp[i]?.key.let { it1 ->
                            if (it1 == keyData) {
                                keyposition = i + 1
                            }
                            if (it1 != null) {
                                key.add(it1)
                            }
                        }
                    }
                }
                val arrayAdap = ArrayAdapter(
                    fragment.requireActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayCatalog
                )
                spinner.setAdapter(arrayAdap)
                spinner.setText(arrayCatalog[keyposition], false)
            }
        }
        spinner.setOnItemClickListener { _, _, position, _ ->
            reason = if (position == 0) null else key[position]
        }
    }
}