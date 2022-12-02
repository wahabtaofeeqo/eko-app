package com.wristband.eko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.AgentViewBinding
import com.wristband.eko.entities.Agent

class AgentAdapter(val context: Context, private val list: List<Agent>): RecyclerView.Adapter<AgentAdapter.AgentVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AgentViewBinding.inflate(inflater)
        return AgentVH(binding)
    }

    override fun onBindViewHolder(holder: AgentVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AgentVH(private val binding: AgentViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(agent: Agent) {
            binding.name.text = agent.name
            binding.username.text = agent.username
        }
    }
}