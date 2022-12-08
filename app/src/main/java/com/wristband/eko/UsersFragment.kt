package com.wristband.eko

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.AgentAdapter
import com.wristband.eko.adapters.UserAdapter
import com.wristband.eko.databinding.FragmentUsersBinding
import com.wristband.eko.entities.User
import com.wristband.eko.vm.SharedViewModel
import com.wristband.eko.vm.UserViewModel
import es.dmoral.toasty.Toasty

/**
 * A simple [Fragment] subclass.
 * Use the [AttendanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {

    lateinit var adapter: UserAdapter
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = UsersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        adapter = UserAdapter(requireActivity().applicationContext)

        //
        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        //
        viewModel.loadUsers().observe(requireActivity()) { response ->
            if(response == null) return@observe
            adapter.submitList(response)

            if(adapter.itemCount > 0) {
                binding.empty.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
        }

        //
        viewModel.export.observe(requireActivity()) { response ->
            if(response == null) return@observe
            if(response.status) {
                Toasty.success(requireContext(), response.message).show()
            }
            else {
                Toasty.error(requireContext(), response.message).show()
            }
        }
    }
}