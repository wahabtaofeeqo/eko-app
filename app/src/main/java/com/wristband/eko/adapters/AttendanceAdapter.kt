package com.wristband.eko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.AttendanceViewBinding
import com.wristband.eko.entities.AttendanceWithUser

class AttendanceAdapter(val context: Context, private val list: List<AttendanceWithUser>): RecyclerView.Adapter<AttendanceAdapter.AttendeeVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AttendanceViewBinding.inflate(inflater)
        return AttendeeVH(binding)
    }

    override fun onBindViewHolder(holder: AttendeeVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AttendeeVH(val binding: AttendanceViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: AttendanceWithUser) {
            binding.name.text = attendance.name
            binding.code.text = attendance.code
            binding.place.text = attendance.place
            binding.category.text = attendance.category
        }
    }
}