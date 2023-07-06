package com.venturessoft.human.humanrhdapp.network

import com.venturessoft.human.humanrhdapp.utilis.complements.Constants

class URL {
    companion object {
        private var ambiente: String = Constants.DESARROLLO
        var URL_BASE = "http://192.168.0.88/Human/RHD/HumaneLand/"
        var URL_KARDEX = "http://192.168.0.88:6001/consultaKardex/"
        var URL_REPORTESAT = "http://192.168.0.88:6001/HumaneLand/RHD/"
        init {
            when (ambiente) {
                Constants.PREPRODUCTIVO -> {
                    URL_BASE = "http://192.168.0.88/Human/RHD/HumaneLand/"
                    URL_KARDEX = "http://192.168.0.88:6001/consultaKardex/"
                    URL_REPORTESAT = "http://192.168.0.88:6001/HumaneLand/RHD/"
                }
                Constants.DESARROLLO -> {
                    URL_BASE = "http://192.168.0.88/Human/RHD/HumaneLand/"
                    URL_KARDEX = "http://192.168.0.88:6001/consultaKardex/"
                    URL_REPORTESAT = "http://192.168.0.88:6001/HumaneLand/RHD/"
                }
                Constants.CONTROLCALIDAD -> {
                    URL_BASE = "http://192.168.0.88/Human/RHD/HumaneLand/"
                    URL_KARDEX = "http://192.168.0.88:6001/consultaKardex/"
                    URL_REPORTESAT = "http://192.168.0.88:6001/HumaneLand/RHD/"
                }
            }
        }
    }
    class Login {
        companion object {
            const val LOGIN = "LoginRHD"
            const val EMPLEADO_LISTADO = "RHD/Listado/Page"
            const val OLVIDE_CONTRASENA = "LoginRHD/Remember/Password"
            const val DASHBOARD = "DashboardEntradasSalidas"
            const val BUSQUEDA_EMPLEADO = "RHD/Listado/Page"

        }
    }
    class ControlDeAusentismos {
        companion object {
            const val VACACIONES = "Vacaciones"
            const val PROGANUALVACACIONES = "PgmVacaciones"
            const val LISTAUSUARIOS = "PgmVacaciones"
            const val GETMOTIVOPERM = "RHD/getMotivosPermisos"
            const val ADDPERMISOS = "RHD/addPermisos"
            const val REPORTAUSENTISMOS = "Ausentismos/Ausentismos"
            const val REPORTEVACACIONES = "Ausentismos/Vacaciones"
            const val REPORTINCAPACIDADES = "Ausentismos/Incapacidades"
            const val REPORTPERMISOS = "Ausentismos/Permisos"
            const val AREA = "Catalogo/AreaCentroLinea"
        }
    }
    class AdministracionDeTiempos {
        companion object {
            const val REPASISTENCIAORG = "AsistenciaOrganigrama"
            const val REPCONTROLASISTENCIA = "ControlAsistencia"
            const val REPENTRADASSALIDAS = "EntradasSalidas"
            const val REPENTRADASSALIDASXCONCEPTO = "EntradasSalidasPorConcepto"
            const val REPHORASLABORADAS = "HoraLaboradas"
            const val REPTARJETAEMPLEADOS = "TarjetaEmpleado"
            const val REPORTAUSENTISMOS = "Ausentismos"
            const val REPDIASXDISF = "DiasPorDisfrutar"
            const val CATALOGOCALENDARIO = "getCatalogoCalendario"
            const val CATALOGOCONCEPTO = "Catalogo/Concepto"
            const val CATALOGOCODID = "Catalogo/CodId"
            const val CATALOGOPERMISOS = "Catalogo/Permiso"
            const val CATALOGOROL = "getCatalogoRolHorario"
            const val CATALOGOTURNO = "getCatalogoTurno"
            const val MR = "getMR"
            const val CATALOGOZONA = "Catalogo/Zona"
            const val ADDINFORMACIONGENERAL = "addAdministrarMR"
            const val EDITINFORMACIONGENERAL = "updateAdministrarMR"
            const val GETNEGOCIACIONHORARIO = "getNegociacionHorario"
            const val GETENTRADASSALIDAS = "getEntradasSalidas"
            const val ADDENTRADASSALIDAS = "addEntradasSalidas"
            const val ADDPREAUTORIZACIONTIEMPOS = "PreautorizacionTiempoExtra"
            const val SUPERVISOR = "Catalogo/Supervisor"
            const val MOTIVOS = "Catalogo/Motivo"
            const val CALENDARIO = "getCatalogoCalendario"
            const val DEPARTAMENT = "Catalogo/getCatalogoCalendario"
            const val CATEGORY = "Catalogo/Categoria"
        }
    }
    class Kardex {
        companion object {
            const val KARDEX = "Consulta/Reporte"
            const val KARDEXANUAL = "Consulta/Anual"
            const val DIASFESTIVOS = "Consulta/DiasFestivos"
            const val DIASDESCANSOS = "Consulta/DiasDescanso"
        }
    }
}