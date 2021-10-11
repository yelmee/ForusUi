package com.example.forusuistudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.forusuistudy.databinding.FragmentLogInBinding

class LogInFragment: Fragment() {
    private lateinit var binding: FragmentLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<FragmentLogInBinding>(
            inflater, R.layout.fragment_log_in, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogInEmailLogin.setOnClickListener {
            val direction: NavDirections = LogInFragmentDirections
                .actionLogInFragmentToSignInFragment()

            it.findNavController().navigate(direction)
        }

        binding.tvLogInSignUpBtn.setOnClickListener {
            val directions: NavDirections = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            it.findNavController().navigate(directions)
        }
    }
}