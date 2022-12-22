package com.wristband.eko.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.AttendanceViewBinding
import com.wristband.eko.entities.AttendanceWithUser
import com.wristband.eko.entities.User
import java.text.SimpleDateFormat
import java.util.*

class AttendanceAdapter(): PagedListAdapter<AttendanceWithUser, AttendanceAdapter.AttendeeVH>(DIFF_CALLBACK) {

    companion object {
        private val  DIFF_CALLBACK = object : DiffUtil.ItemCallback<AttendanceWithUser>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: AttendanceWithUser, newItem: AttendanceWithUser) = oldItem.user_id == newItem.user_id

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(oldItem: AttendanceWithUser, newItem: AttendanceWithUser) =
                run {
                    val oldDate = oldItem.date?.let { SimpleDateFormat("dd-MM-yyyy").format(it) }
                    val newDate = oldItem.date?.let { SimpleDateFormat("dd-MM-yyyy").format(it) }
                    oldDate == newDate && oldItem.code == newItem.code
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceViewBinding.inflate(inflater)
        return AttendeeVH(binding)
    }

    override fun onBindViewHolder(holder: AttendeeVH, position: Int) {
        holder.bind(getItem(position))
    }

    class AttendeeVH(val binding: AttendanceViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: AttendanceWithUser?) {
            if(attendance != null) {
                var name = attendance.name
                if(name == null) name = "N/A"

                binding.name.text = name
                binding.code.text = attendance.code
                binding.place.text = attendance.place
                binding.category.text = attendance.category
                val todayDate = attendance.date?.let { SimpleDateFormat("E, d H:m a").format(it) }
                binding.date.text = todayDate
            }
        }
    }
}