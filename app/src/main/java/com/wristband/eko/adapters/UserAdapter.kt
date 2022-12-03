package com.wristband.eko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.UserViewBinding
import com.wristband.eko.entities.User

class UserAdapter(context: Context): PagedListAdapter<User, UserAdapter.UserVH>(DIFF_CALLBACK) {

    companion object {
        private val  DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.uid == newItem.uid

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem.code == newItem.code
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: UserViewBinding = UserViewBinding.inflate(inflater)
        return UserVH(binding)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(getItem(position))
    }

    class UserVH(val binding: UserViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User?) {
            binding.name.text = user?.name
            binding.code.text = user?.code
            binding.category.text = user?.category
        }
    }
}