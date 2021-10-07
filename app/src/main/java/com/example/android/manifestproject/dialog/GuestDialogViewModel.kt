package com.example.android.manifestproject.dialog

import android.app.Application
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.android.manifestproject.data.ManifestoDatabaseDao

class GuestDialogViewModel (private val database: ManifestoDatabaseDao,
                            private val guestId: Long)
    : ViewModel() {

}