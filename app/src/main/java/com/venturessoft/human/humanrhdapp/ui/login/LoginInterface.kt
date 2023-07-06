package com.venturessoft.human.humanrhdapp.ui.login

interface LoginInterface {
    fun showLoading(isShowing: Boolean)

    fun showRecoverPassword(isVisible: Boolean)

    fun setTextToolbar(text:String)
}