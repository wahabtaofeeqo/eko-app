package com.wristband.eko.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.AgentViewBinding

class AgentAdapter: RecyclerView.Adapter<AgentAdapter.AgentVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AgentViewBinding.inflate(inflater)
        return AgentVH(binding)
    }

    override fun onBindViewHolder(holder: AgentVH, position: Int) {
        //
    }

    override fun getItemCount(): Int {
        return 10
    }

    class AgentVH(binding: AgentViewBinding): RecyclerView.ViewHolder(binding.root) {

    }
}