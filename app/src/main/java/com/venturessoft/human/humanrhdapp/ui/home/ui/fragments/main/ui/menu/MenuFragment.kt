package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.databinding.FragmentMenuBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.MenuData
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.data.MenuModel
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.vm.WelcomeFragmentViewModel
import com.venturessoft.human.humanrhdapp.ui.login.ui.LoginActivity
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants
import com.venturessoft.human.humanrhdapp.utilis.complements.DialogListener
import com.venturessoft.human.humanrhdapp.utilis.complements.User
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities.Companion.toggleLayout

class MenuFragment : Fragment(), MenuAdapter.OnClickListener {

    private lateinit var binding: FragmentMenuBinding
    private var mainInterface: MainInterface? = null
    private val w : WelcomeFragmentViewModel by activityViewModels()
    private var assistControlExpanded = false
    private var timeManagementExpanded = false
    private var settingsExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menu = MenuData(requireContext())
        binding.rvAssistControl.adapter = MenuAdapter(menu.setAssistControl(),this)
        binding.rvTimeManagement.adapter = MenuAdapter(menu.setTimeManagement(),this)
        binding.rvSettings.adapter = MenuAdapter(menu.setSettings(),this)
        binding.usernameTv.text = User.nombreSupervisor
        binding.assistControl.setOnClickListener {
            val show: Boolean = toggleLayout(!assistControlExpanded, binding.arrow1, binding.rvAssistControl)
            assistControlExpanded = show
        }
        binding.timeManagement.setOnClickListener {
            val show: Boolean = toggleLayout(!timeManagementExpanded, binding.arrow2, binding.rvTimeManagement)
            timeManagementExpanded = show
        }
        binding.settings.setOnClickListener {
            val show: Boolean = toggleLayout(!settingsExpanded, binding.arrow3, binding.rvSettings)
            settingsExpanded = show
        }
    }
    override fun onResume() {
        super.onResume()
        assistControlExpanded = false
        timeManagementExpanded = false
        settingsExpanded = false
        mainInterface?.setTextToolbar(User.razonSocial)
        w.idMenu.value = null
        loadPhoto()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun loadPhoto(){
        if (User.foto.contains("File not foundjava", ignoreCase = true) || User.foto.isEmpty()) {
            binding.profilePicture.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.user_icon_big))
        } else {
            val imagenBase64 = Base64.decode(User.foto, Base64.DEFAULT)
            val imagenconverBitmap =
                BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
            binding.profilePicture.setImageBitmap(imagenconverBitmap)
        }
    }
    override fun onClick(menuModel: MenuModel) {
        w.idMenu.value = menuModel
        when (menuModel.id){
            1,2,3,4,5,6,8,9,10,11 -> findNavController().navigate(R.id.to_listEmployeeFragment)
            7 -> findNavController().navigate(R.id.to_menuReportesControlDeAusentismosFragment)
            12 -> findNavController().navigate(R.id.to_reportesFragment)
            13 -> findNavController().navigate(R.id.to_aboutUsFragment)
            14-> showExitDialog()
            15 -> findNavController().navigate(R.id.to_succesFragment)
        }
    }
    private fun showExitDialog(){
        Utilities.showDialog(
            title = getString(R.string.signoff_title),
            message = getString(R.string.signoff_message),
            positiveButtonText = getString(R.string.signoff_postive),
            negativeButtonText = getString(R.string.signoff_negative),
            context = requireContext(),
            listener = object : DialogListener {
                override fun onPositiveButtonClicked() {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.putExtra(Constants.FROM_LOG_OUT, true)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    requireContext().startActivity(intent)
                    User.listUsu?.clear()
                    User.listUsu = null
                    ActivityCompat.finishAffinity(Activity())
                }
                override fun onNegativeButtonClicked() {}
            }
        )
    }
}