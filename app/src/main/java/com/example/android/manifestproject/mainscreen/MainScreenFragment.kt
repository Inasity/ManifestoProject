package com.example.android.manifestproject.mainscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.android.manifestproject.R
import com.example.android.manifestproject.databinding.FragmentMainScreenBinding


class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by viewModels()

    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        binding.signInButton.setOnClickListener{ v: View ->
            v.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSignInFragment())
        }

        return binding.root
    }
}