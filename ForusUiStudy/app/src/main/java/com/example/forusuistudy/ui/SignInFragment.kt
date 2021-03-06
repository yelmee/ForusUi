package com.example.forusuistudy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.forusuistudy.R
import com.example.forusuistudy.databinding.FragmentSignInBinding

class SignInFragment: Fragment() {

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<FragmentSignInBinding>(
            inflater, R.layout.fragment_sign_in, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignInFindPw.setOnClickListener {
            val navDirections =
                SignInFragmentDirections.actionSignInFragmentToFindPwFragment()
            it.findNavController().navigate(navDirections)
        }

        binding.btnSingInNext.setOnClickListener{
            val navDirections =
                SignInFragmentDirections.actionSignInFragmentToMainFragment()
            it.findNavController().navigate(navDirections)
        }

    }
}