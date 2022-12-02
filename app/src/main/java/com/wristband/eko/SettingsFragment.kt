package com.wristband.eko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.UserAdapter
import com.wristband.eko.databinding.FragmentSettingsBinding
import com.wristband.eko.databinding.FragmentUsersBinding
import com.wristband.eko.vm.UserViewModel
import es.dmoral.toasty.Toasty

class SettingsFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        //
        viewModel.getCount()
        viewModel.total.observe(requireActivity()) { response ->
            if(response == null) return@observe
            binding.users.text = "$response"
        }

        //
        binding.reset.setOnClickListener {
            Toasty.info(requireContext(), "Data cleared successfully").show()
        }
    }
}