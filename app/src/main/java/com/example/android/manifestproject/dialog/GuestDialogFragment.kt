package com.example.android.manifestproject.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.android.manifestproject.R
import com.example.android.manifestproject.databinding.FragmentGuestDeleteDialogBinding

class GuestDialogFragment(): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGuestDeleteDialogBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_guest_delete_dialog, container, false
        )

        binding.denyButton.setOnClickListener{
            dismiss()
        }

        return binding.root

    }
}