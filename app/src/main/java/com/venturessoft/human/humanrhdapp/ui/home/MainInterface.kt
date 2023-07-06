package com.venturessoft.human.humanrhdapp.ui.home

interface MainInterface {
    fun setTextToolbar(text:String)
    fun showLottie(isVisible: Boolean)
    fun showLoading(isShowing: Boolean = false)
}