package com.example.android.manifestproject.signinscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.manifestproject.MainActivity
import com.example.android.manifestproject.R
import com.example.android.manifestproject.createViewModel
import com.example.android.manifestproject.data.ManifestoDatabase
import com.example.android.manifestproject.databinding.FragmentSignInBinding
import com.example.android.manifestproject.mainscreen.GuestEntityAdapter


class SignInFragment : Fragment() {

    //private lateinit var viewModelZ: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        val binding: FragmentSignInBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_in, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ManifestoDatabase.getInstance(application).manifestoDatabaseDao

        val guestIdFragmentArgs by navArgs<SignInFragmentArgs>()

        val viewModelFactory = SignInViewModelFactory(dataSource, application, guestIdFragmentArgs.guestID)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)

//        viewModelZ = createViewModel {
//            SignInViewModel(
//                ManifestoDatabase.getInstance(MainActivity.appContext).manifestoDatabaseDao,
//                MainActivity.App
//            )
//        }

        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.navigateToMainScreen.observe(viewLifecycleOwner, Observer { canWeNavigate ->
            if (canWeNavigate == true)
            {
                findNavController().navigate(
                    SignInFragmentDirections
                        .actionSignInFragmentToMainScreenFragment()
                )
                viewModel.doneNavigating()
            }
        })

        viewModel.login_mediator.observe(viewLifecycleOwner, Observer { isGood ->
            binding.saveAndSignButton.isEnabled = isGood
        })

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
}