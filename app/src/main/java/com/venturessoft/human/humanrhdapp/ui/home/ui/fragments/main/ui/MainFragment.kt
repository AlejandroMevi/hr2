package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venturessoft.human.humanrhdapp.databinding.FragmentMainBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.HomeActivity
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.menu.MenuFragment
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.main.ui.welcome.WelcomeFragment

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        binding.vpMain.isUserInputEnabled = false
        val myAdapter = MainViewPagerAdapternb(childFragmentManager, lifecycle)
        myAdapter.addFragment(WelcomeFragment())
        myAdapter.addFragment(MenuFragment())
        binding.vpMain.adapter = myAdapter
        HomeActivity.positionVm.observe(viewLifecycleOwner){ position->
            binding.vpMain.currentItem = position
        }
    }
}