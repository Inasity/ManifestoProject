package com.example.android.manifestproject.signinscreen

import android.content.res.ColorStateList
import android.graphics.Color
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

        viewModel.name_check.observe(viewLifecycleOwner, Observer { goodName ->
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

        viewModel.phone_number_check.observe(viewLifecycleOwner, Observer { goodPhone ->
            if(viewModel.showPhoneError()){
                binding.textFieldPhone.isErrorEnabled = true
                binding.textFieldPhone.error = "Must enter 10 digit number."
            }
            else{
                binding.textFieldPhone.isErrorEnabled = false
            }
        })

        viewModel.email_check.observe(viewLifecycleOwner, Observer { goodEmail ->
            if(viewModel.showEmailError()){
                binding.textFieldEmail.isErrorEnabled = true
                binding.textFieldEmail.error = "We do not recognize that as an email. Try again."
            }
            else{
                binding.textFieldEmail.isErrorEnabled = false
            }
        })

        viewModel.emergency_number_check.observe(viewLifecycleOwner, Observer { goodEmergencyNum ->
            if(viewModel.showEmergencyPhoneError()){
                binding.textFieldEmergencyNumber.isErrorEnabled = true
                binding.textFieldEmergencyNumber.error = "Must enter 10 digit number."
            }
            else{
                binding.textFieldEmergencyNumber.isErrorEnabled = false
            }
        })

        viewModel.emergency_name_check.observe(viewLifecycleOwner, Observer { goodEmergencyName ->
            if(viewModel.showEmergencyNameError()){
                binding.textFieldEmergencyName.isErrorEnabled = true
                binding.textFieldEmergencyName.error =
                    "Must be 2-12 character's long and have no special characters."
            }
            else{
                binding.textFieldEmergencyName.isErrorEnabled = false
            }
        })

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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
}