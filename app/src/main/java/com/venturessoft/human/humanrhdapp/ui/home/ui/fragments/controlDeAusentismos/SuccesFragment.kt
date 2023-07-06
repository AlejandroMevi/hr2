package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.venturessoft.human.humanrhdapp.R
import com.venturessoft.human.humanrhdapp.databinding.FragmentSuccesBinding
import com.venturessoft.human.humanrhdapp.ui.home.MainInterface
import com.venturessoft.human.humanrhdapp.utilis.complements.Constants

class SuccesFragment : Fragment() {

    private lateinit var binding: FragmentSuccesBinding
    private var mainInterface: MainInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuccesBinding.inflate(inflater, container, false)
        mainInterface?.showLottie(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.lottieSuccess.setAnimation("lottieanimation/checkmark-.json")
        binding.lottieSuccess.playAnimation()
        binding.lottieSuccess.repeatMode
        Handler(Looper.getMainLooper()).postDelayed({ backView() }, Constants.SUCCES_ANIMOTIONLOTTIE_TIMEOUT)
    }

    private fun backView(){
        findNavController().navigate(R.id.to_listEmployeeFragment)
        mainInterface?.showLottie(true)
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
}