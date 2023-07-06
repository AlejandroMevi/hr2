package com.venturessoft.human.humanrhdapp.utilis.complements

class Constants {
    companion object {
        const val FROM_LOG_OUT = "fromLogOut"
        const val PREPRODUCTIVO = "Preproductivo"
        const val DESARROLLO = "Desarrollo"
        const val CONTROLCALIDAD = "ControlCalidad"
        const val SUCCES_ANIMOTIONLOTTIE_TIMEOUT = 2500L

        const val MENU_KEY = "menuKey"
        const val DATA_KEY = "dataKey"
        const val STRING_KEY = "string"
        const val PDF_KEY = "pdf"
        const val TITULO_KEY = "string"

        const val AREA = 1
        const val CENTRO = 2
        const val LINEA = 3
    }
}

enum class TypeServices {
    BASE,
    KARDEX,
    REPORTESAT
}
