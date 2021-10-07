package com.example.android.manifestproject.mainscreen

import android.app.AlertDialog
import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.manifestproject.R
import com.example.android.manifestproject.data.GuestEntity
import com.example.android.manifestproject.databinding.ListItemGuestEntityBinding
import com.example.android.manifestproject.dialog.GuestDialogFragment

class GuestEntityAdapter() : ListAdapter<GuestEntity,
        GuestEntityAdapter.ViewHolder>(GuestEntityDiffCallback())
{

    class ViewHolder private constructor(val binding: ListItemGuestEntityBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: GuestEntity){

                val res = itemView.context.resources

                binding.guestName.text = item.fullName

                binding.editButton.setOnClickListener{v: View ->
                    v.findNavController().navigate(
                        MainScreenFragmentDirections.
                        actionMainScreenFragmentToSignInFragment(item.guestID))
                }

                binding.cancelButton.setOnClickListener{v: View ->
                    var dialogBuilder = AlertDialog.Builder(v.rootView.context)
                    val dialogView = LayoutInflater.from(v.rootView.context).inflate(
                        R.layout.fragment_guest_delete_dialog, null)

                    dialogBuilder.setView(dialogView)
                    dialogBuilder.setCancelable(true)

                    dialogBuilder.show()

                }
            }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGuestEntityBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
}

class GuestEntityDiffCallback : DiffUtil.ItemCallback<GuestEntity>() {
    override fun areItemsTheSame(oldItem: GuestEntity, newItem: GuestEntity): Boolean {
        return oldItem.guestID == newItem.guestID
    }

    override fun areContentsTheSame(oldItem: GuestEntity, newItem: GuestEntity): Boolean {
        return oldItem == newItem
    }

}
