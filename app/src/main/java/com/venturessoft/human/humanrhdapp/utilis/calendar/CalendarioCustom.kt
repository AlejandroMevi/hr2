package com.venturessoft.human.humanrhdapp.utilis.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.venturessoft.human.humanrhdapp.utilis.complements.getColorCompat
import com.venturessoft.human.humanrhdapp.R
import java.util.Calendar

class CalendarioCustom {

    fun aDiaAusentismos(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val diaAusentismos = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "A") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = parts[2]
                diaAusentismos.add(day.toInt())
            }
        }
        return diaAusentismos
    }

    fun aMesesAusentismos(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val mesAusentismos = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "A") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val month = parts[1]
                mesAusentismos.add(month.toInt())
            }
        }
        return mesAusentismos
    }

    fun aDiaRetardos(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val diaRetardos = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "®") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = parts[2]
                diaRetardos.add(day.toInt())
            }
        }
        return diaRetardos
    }

    fun aMesesRetardos(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val mesRetardos = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "®") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val month = parts[1]
                mesRetardos.add(month.toInt())
            }
        }
        return mesRetardos
    }

    fun aDiaVacaciones(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val diaVacaciones = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "V") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = parts[2]
                diaVacaciones.add(day.toInt())
            }
        }
        return diaVacaciones
    }

    fun aMesesVacaciones(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val mesVacaciones = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "V") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val month = parts[1]
                mesVacaciones.add(month.toInt())
            }
        }
        return mesVacaciones
    }

    fun aDiaFaltas(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val diaFaltas = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "F") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = parts[2]
                diaFaltas.add(day.toInt())
            }
        }
        return diaFaltas
    }

    fun aMesesFaltas(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val mesFaltas = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] == "F") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val month = parts[1]
                mesFaltas.add(month.toInt())
            }
        }
        return mesFaltas
    }

    fun aDiaOthers(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val diaFaltas = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] != "A" && marcas[i] != "V" && marcas[i] != "R" && marcas[i] != "F" && marcas[i] != "®") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val day = parts[2]
                diaFaltas.add(day.toInt())
            }
        }
        return diaFaltas
    }

    fun aMesesOthers(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<Int> {
        val mesOthers = ArrayList<Int>()
        for (i in fechas.indices) {
            if (marcas[i] != "A" && marcas[i] != "V" && marcas[i] != "R" && marcas[i] != "F" && marcas[i] != "®") {
                val string = fechas[i]
                val parts =
                    string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val month = parts[1]
                mesOthers.add(month.toInt())
            }
        }
        return mesOthers
    }

    fun aDiasFestivos(fechas: ArrayList<String>): ArrayList<Int> {
        val dias = ArrayList<Int>()
        for (i in fechas.indices) {
            val string = fechas[i]
            val parts = string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val day = parts[2]
            dias.add(day.toInt())
        }
        return dias
    }

    fun aMesesFestivos(fechas: ArrayList<String>): ArrayList<Int> {
        val mes = ArrayList<Int>()
        for (i in fechas.indices) {
            val string = fechas[i]
            val parts = string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val month = parts[1]
            mes.add(month.toInt())
        }
        return mes
    }

    fun aDiasDescansos(fechas: ArrayList<String>): ArrayList<Int> {
        val dias = ArrayList<Int>()
        for (i in fechas.indices) {
            val string = fechas[i]
            val parts = string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val day = parts[2]
            dias.add(day.toInt())
        }
        return dias
    }

    fun aMesesDescansos(fechas: ArrayList<String>): ArrayList<Int> {
        val mes = ArrayList<Int>()
        for (i in fechas.indices) {
            val string = fechas[i]
            val parts = string.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val month = parts[1]
            mes.add(month.toInt())
        }
        return mes
    }
    fun aMarcasOthers(fechas: ArrayList<String>, marcas: ArrayList<String>): ArrayList<String> {
        val marcasOthers = ArrayList<String>()
        for (i in fechas.indices) {
            if (marcas[i] != "A" && marcas[i] != "V" && marcas[i] != "R" && marcas[i] != "F" && marcas[i] != "®") {
                val string = marcas[i]
                marcasOthers.add(string)
            }
        }
        return marcasOthers
    }
    fun pintaVacaciones(calendarDayText: TextView, context: Context) {
        calendarDayText.text = "V"
        calendarDayText.setTextColor(context.getColor(R.color.black))
    }

    fun pintaFaltas(calendarDayText: TextView, requireContext: Context) {
        calendarDayText.text = "F"
        calendarDayText.setTextColor(requireContext.getColor(R.color.black))
    }

    @SuppressLint("SetTextI18n")
    fun pintaOthers(calendarDayText: TextView, requireContext: Context, kardexMensualOthers: List<CustomDay>) {
        for(i in kardexMensualOthers.indices){
            calendarDayText.text = kardexMensualOthers[0].marca
            calendarDayText.setTextColor(requireContext.getColor(R.color.black))
        }
    }
    @SuppressLint("SetTextI18n")
    fun pintaDescansos(calendarDayText: TextView, requireContext: Context, descansos: List<CustomDay>) {
        calendarDayText.text = "Descansos"
        calendarDayText.setBackgroundColor(requireContext.getColorCompat(descansos[0].color))
        calendarDayText.setTextColor(requireContext.getColor(R.color.letraD))
    }
    @SuppressLint("SetTextI18n")
    fun pintaFestivos(calendarDayText: TextView, requireContext: Context, festivos: List<CustomDay>) {
        calendarDayText.text = "Festivos"
        calendarDayText.setBackgroundColor(requireContext.getColorCompat(festivos[0].color))
        calendarDayText.setTextColor(requireContext.getColor(R.color.letrasF))
    }
    @SuppressLint("SetTextI18n")
    fun pintaAusentismos(calendarDayText: TextView, requireContext: Context, ausentismos: List<CustomDay>) {
        calendarDayText.text = "Ausentismos"
        calendarDayText.setBackgroundColor(requireContext.getColorCompat(ausentismos[0].color))
        calendarDayText.setTextColor(requireContext.getColor(R.color.letrasA))
    }

    fun pintaRetardos(calendarDayText: TextView, requireContext: Context) {
        calendarDayText.text = "®"
        calendarDayText.setTextColor(requireContext.getColor(R.color.black))
    }

    fun validaTxt(texto: String, context: Context, id: Int): String {

        return when (texto) {
            "F" -> context.getString(R.string.f)
            "V" -> context.getString(R.string.v)
            "Others" -> context.getString(R.string.others)
            "Descansos" -> context.getString(R.string.descansos)
            "Festivos" -> context.getString(R.string.Festivos)
            "Ausentismos" -> context.getString(R.string.Ausentismos)
            "®" -> context.getString(R.string.Retardos)
            else -> {
                if (id == 1) context.getString(R.string.otros) else "else"
            }
        }
    }

    fun mostrarFecha(year: Int, month: Int, dayOfMonth: Int): String {
        var fechaTxt = ""
        if (month < 10) {
            fechaTxt = "$dayOfMonth/0$month/$year"
        }
        if (dayOfMonth < 10) {
            fechaTxt = "0$dayOfMonth/$month/$year"
        }
        if (dayOfMonth < 10 && month < 10) {
            fechaTxt = "0$dayOfMonth/0$month/$year"
        }
        return fechaTxt
    }

    fun asignarFecha(year: Int, month: Int, dayOfMonth: Int): String {
        var fechaTxt = ""
        if (month < 10) {
            fechaTxt = "$dayOfMonth-0$month-$year"
        }
        if (dayOfMonth < 10) {
            fechaTxt = "0$dayOfMonth-$month-$year"
        }
        if (dayOfMonth < 10 && month < 10) {
            fechaTxt = "0$dayOfMonth-0$month-$year"
        }
        return fechaTxt
    }

    fun diferenciaFechas(fechaInicial: String, fechaFinal: String): Long {
        val fechaI = fechaInicial.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val fechaF = fechaFinal.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val cal = Calendar.getInstance()
        var diasSeleccionados: Long?
        cal[Calendar.DAY_OF_MONTH] = fechaI[0].toInt()
        cal[Calendar.MONTH] = fechaI[1].toInt()
        cal[Calendar.YEAR] = fechaI[2].toInt()
        val firstDate = cal.time
        cal[Calendar.DAY_OF_MONTH] = fechaF[0].toInt()
        cal[Calendar.MONTH] = fechaF[1].toInt()
        cal[Calendar.YEAR] = fechaF[2].toInt()
        val secondDate = cal.time
        val diferencia = secondDate.time - firstDate.time
        diasSeleccionados = diferencia / 1000 / 60 / 60 / 24
        diasSeleccionados.toInt()
        diasSeleccionados += 1
        println("Diferencia en dias: " + diferencia / 1000 / 60 / 60 / 24)
        return diasSeleccionados
    }
}