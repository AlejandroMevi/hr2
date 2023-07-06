package com.venturessoft.human.humanrhdapp.ui.login.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.venturessoft.human.humanrhdapp.ui.home.ui.HomeActivity
import com.venturessoft.human.humanrhdapp.ui.login.LoginInterface
import com.venturessoft.human.humanrhdapp.ui.login.ui.vm.LoginActivityViewModel
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.core.ApiResponceStatus
import com.venturessoft.human.humanrhdapp.databinding.FragmentLoginBinding
import com.venturessoft.human.humanrhdapp.utilis.complements.Utilities

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginActivityViewModel: LoginActivityViewModel by activityViewModels()
    private var email: String = ""
    private var password: String = ""
    private var loginInterface: LoginInterface? = null
    companion object {
        var selectedItemIndex = 0
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }
    override fun onResume() {
        super.onResume()
        loginInterface?.showRecoverPassword(false)
    }
    @SuppressLint("SetTextI18n")
    private fun initializeView() {
        with(binding) {
            emailTextField.editText?.setText("vmjdelator")
            passwordTextField.editText?.setText("aochoa09")
            enterButton.setOnClickListener { clickLogin() }
            forgotNipButton.setOnClickListener { clickForgotNip() }
        }
    }
    private fun clickLogin() {
        email = binding.emailTextField.editText?.text.toString().trim()
        password = binding.passwordTextField.editText?.text!!.toString().trim()
        val checkFieldsMessage = checkFields()
        if (checkFieldsMessage == "true") {
            loginActivityViewModel.dataLogin.value = null
            corutineLoginService()
        } else {
            Toast.makeText(requireContext(), checkFieldsMessage, Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkFields(): String {
        if (email.isEmpty()) {
            return getString(R.string.a_login_error_email_empty)
        }
        if (password.isEmpty()) {
            return getString(R.string.a_login_error_nip_empty)
        }
        return "true"
    }
    private fun clickForgotNip() {
        findNavController().navigate(R.id.to_recoverPasswordFragment)
        loginInterface?.showRecoverPassword(true)
    }
    private fun corutineLoginService() {
        loginActivityViewModel.getLoginService(requireContext(), email, password)
        statusObserve()
        loginActivityViewModel.dataEmpleadoList.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.codigo == "0") {
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    startActivity(intent)
                    loginActivityViewModel.dataEmpleadoList.value = null
                    requireActivity().finish()
                }
            }
        }
    }
    private fun statusObserve() {
        loginActivityViewModel.status.observe(viewLifecycleOwner) { status ->
            if (status != null) {
                when (status) {
                    is ApiResponceStatus.Loading -> {
                        loginInterface?.showLoading(true)
                    }
                    is ApiResponceStatus.Success -> {
                        clearService()
                    }
                    is ApiResponceStatus.Error -> {
                        clearService()
                        val errorCode = Utilities.textcode(status.messageId, requireContext())
                        Utilities.loadMessageError(errorCode, requireActivity())
                    }
                }
            }
        }
    }
    private fun clearService() {
        loginActivityViewModel.status.removeObservers(this)
        loginActivityViewModel.status.value = null
        loginInterface?.showLoading(false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginInterface) {
            loginInterface = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        loginInterface = null
    }
}