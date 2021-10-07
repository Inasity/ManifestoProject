package com.example.android.manifestproject

import android.content.res.Resources
import android.net.wifi.aware.AwareResources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.manifestproject.data.GuestEntity
import java.lang.StringBuilder

fun formatGuests(guests: List<GuestEntity>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        guests.forEach{
            append("<br>")
            append(resources.getString(R.string.guest_name))
            append("\t${it.fullName}<br>")
            append(resources.getString(R.string.guest_phone))
            append("\t${it.phoneNumber}<br>")
            append(resources.getString(R.string.guest_email))
            append("\t${it.email}<br>")
            append(resources.getString(R.string.guest_emergency_name))
            append("\t${it.emergencyName}<br>")
            append(resources.getString(R.string.guest_emergency_phone))
            append("\t${it.emergencyPhoneNumber}<br>")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified V : ViewModel> Fragment.createViewModel(crossinline instance: () -> V): V {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return instance() as T
        }
    }
    return ViewModelProvider(this, factory).get(V::class.java)
}