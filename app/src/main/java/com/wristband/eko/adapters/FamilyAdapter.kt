package com.wristband.eko.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.FamilyFragment
import com.wristband.eko.FamilyLinkActivity
import com.wristband.eko.databinding.FamilyViewBinding
import com.wristband.eko.entities.Family

class FamilyAdapter(val context: Context): PagedListAdapter<Family, FamilyAdapter.FamilyVH>(DIFF_CALLBACK) {

    companion object {
        private val  DIFF_CALLBACK = object : DiffUtil.ItemCallback<Family>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: Family, newItem: Family) = oldItem.fid == newItem.fid

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(oldItem: Family, newItem: Family) = oldItem.firstname == newItem.firstname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FamilyViewBinding.inflate(inflater)
        return FamilyVH(binding)
    }

    override fun onBindViewHolder(holder: FamilyVH, position: Int) {
        holder.bind(getItem(position))
    }

    class FamilyVH(val binding: FamilyViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(family: Family?) {
            if (family != null) {
                binding.name.text = family.firstname
                binding.category.text = family.packageType
                binding.size.text = family.familySize.toString()
                binding.linked.text = if (family.linked) "Yes" else "No"

                binding.btn.isEnabled = !family.linked
                binding.btn.setOnClickListener {v ->
                    val intent = Intent(v.context, FamilyLinkActivity::class.java)
                    intent.putExtra(FamilyFragment.FAMILY_ID, family.fid)
                    v.context.startActivity(intent)
                }
            }
        }
    }
}