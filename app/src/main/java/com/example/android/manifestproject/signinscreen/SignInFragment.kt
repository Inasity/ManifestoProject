package com.example.android.manifestproject.signinscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.manifestproject.R
import com.example.android.manifestproject.data.ManifestoDatabase
import com.example.android.manifestproject.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment

        val binding: FragmentSignInBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_in, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ManifestoDatabase.getInstance(application).manifestoDatabaseDao

        val guestIdFragmentArgs by navArgs<SignInFragmentArgs>()

        val viewModelFactory = SignInViewModelFactory(dataSource, application, guestIdFragmentArgs.guestID)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.nameCheck.observe(viewLifecycleOwner, {
            if(viewModel.showNameError())
            {
                binding.textFieldTIL.isErrorEnabled = true
                binding.textFieldTIL.error =
                    "Must be 2-12 character's long and have no special characters."
            }
            else{
                binding.textFieldTIL.isErrorEnabled = false
            }
        })

        viewModel.phoneNumberCheck.observe(viewLifecycleOwner, {
            if(viewModel.showPhoneError()){
                binding.textFieldPhone.isErrorEnabled = true
                binding.textFieldPhone.error = "Must enter 10 digit number."
            }
            else{
                binding.textFieldPhone.isErrorEnabled = false
            }
        })

        viewModel.emailCheck.observe(viewLifecycleOwner, {
            if(viewModel.showEmailError()){
                binding.textFieldEmail.isErrorEnabled = true
                binding.textFieldEmail.error = "We do not recognize that as an email. Try again."
            }
            else{
                binding.textFieldEmail.isErrorEnabled = false
            }
        })

        viewModel.emergencyNumberCheck.observe(viewLifecycleOwner, {
            if(viewModel.showEmergencyPhoneError()){
                binding.textFieldEmergencyNumber.isErrorEnabled = true
                binding.textFieldEmergencyNumber.error = "Must enter 10 digit number."
            }
            else{
                binding.textFieldEmergencyNumber.isErrorEnabled = false
            }
        })

        viewModel.emergencyNameCheck.observe(viewLifecycleOwner, {
            if(viewModel.showEmergencyNameError()){
                binding.textFieldEmergencyName.isErrorEnabled = true
                binding.textFieldEmergencyName.error =
                    "Must be 2-12 character's long and have no special characters."
            }
            else{
                binding.textFieldEmergencyName.isErrorEnabled = false
            }
        })

        viewModel.navigateToMainScreen.observe(viewLifecycleOwner, { canWeNavigate ->
            if (canWeNavigate == true)
            {
                findNavController().navigate(
                    SignInFragmentDirections
                        .actionSignInFragmentToMainScreenFragment()
                )
                viewModel.doneNavigating()
            }
        })

        viewModel.loginMediator.observe(viewLifecycleOwner, { isGood ->
            binding.saveAndSignButton.isEnabled = isGood
            if (isGood == true)
            {
                binding.saveAndSignButton.alpha= 1.0F
            }
            else{
                binding.saveAndSignButton.alpha= 0.3F
            }
        })

        return binding.root
    }
}