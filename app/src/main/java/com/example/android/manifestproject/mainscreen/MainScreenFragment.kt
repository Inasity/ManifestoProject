package com.example.android.manifestproject.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.android.manifestproject.R
import com.example.android.manifestproject.data.ManifestoDatabase
import com.example.android.manifestproject.databinding.FragmentMainScreenBinding


class MainScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentMainScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_screen, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ManifestoDatabase.getInstance(application).manifestoDatabaseDao

        val viewModelFactory = MainScreenViewModelFactory(dataSource, application)

        val viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainScreenViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.signInButton.setOnClickListener{ v: View ->
            v.findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToSignInFragment())
            viewModel.doneNavigating()
        }


        val adapter = GuestEntityAdapter(viewModel::delete)
        binding.guestList.adapter = adapter

        viewModel.guests.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
            if(adapter.itemCount <= 0)
            {
                binding.noOneSignedInTextView.visibility = View.VISIBLE
            }
            else
            {
                binding.noOneSignedInTextView.visibility = View.INVISIBLE
            }
        })



        return binding.root
    }
}