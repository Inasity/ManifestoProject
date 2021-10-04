package com.example.android.manifestproject.signinscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.android.manifestproject.databinding.FragmentSignInBinding



class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    private lateinit var _binding: FragmentSignInBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        _binding.viewModel = viewModel

        _binding.saveAndSignButton.setOnClickListener { v: View ->
            v.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainScreenFragment())
        }

        viewModel.login_mediator.observe(viewLifecycleOwner, Observer { isGood ->
            _binding.saveAndSignButton.isEnabled = isGood
        })

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}