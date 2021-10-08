package com.example.android.manifestproject.mainscreen

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.manifestproject.data.GuestEntity
import com.example.android.manifestproject.databinding.ListItemGuestEntityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

class GuestEntityAdapter(private val negDialogAction: KSuspendFunction1<GuestEntity, Unit>) : ListAdapter<GuestEntity,
        GuestEntityAdapter.ViewHolder>(GuestEntityDiffCallback()) {

    class ViewHolder private constructor(
        private val binding: ListItemGuestEntityBinding,
        private val negDialogAction: KSuspendFunction1<GuestEntity, Unit>,
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: GuestEntity) {

            binding.guestName.text = item.fullName

            binding.editButton.setOnClickListener { v: View ->
                v.findNavController().navigate(
                    MainScreenFragmentDirections.actionMainScreenFragmentToSignInFragment(item.guestID)
                )
            }

            binding.cancelButton.setOnClickListener { v: View ->



                val dialogBuilderDelete = AlertDialog.Builder(v.rootView.context)
                dialogBuilderDelete.setCancelable(false)
                dialogBuilderDelete.setMessage("Continue to delete ${binding.guestName.text}?")
                dialogBuilderDelete.apply {
                    setPositiveButton("ALLOW"
                    ) { dialog, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            negDialogAction.invoke(item)
                        }
                        Log.d("Zelda", "Deleted ${binding.guestName.text}.")
                        dialog.dismiss()
                    }
                    setNegativeButton("DENY"
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                dialogBuilderDelete.show()
            }
        }

        companion object {
            fun from(parent: ViewGroup, negDialogAction: KSuspendFunction1<GuestEntity, Unit>): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGuestEntityBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, negDialogAction)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, negDialogAction)
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

