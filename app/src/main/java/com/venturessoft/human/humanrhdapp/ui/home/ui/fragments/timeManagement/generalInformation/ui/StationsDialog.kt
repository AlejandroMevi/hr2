package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.timeManagement.generalInformation.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.venturessoft.human.humanrhdapp.databinding.DialogStationsBinding

class StationsDialog : DialogFragment() {

    lateinit var binding: DialogStationsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogStationsBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        initView()
        return builder.create()
    }

    private fun initView(){
        val list = listOf("","","","","","","","","","","","","","","","","","","")
    }
}