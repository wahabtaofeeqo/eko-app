package com.wristband.eko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.wristband.eko.databinding.FragmentAddFamilyBinding
import com.wristband.eko.databinding.FragmentFamilyBinding
import com.wristband.eko.entities.Family
import com.wristband.eko.vm.SharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AddFamiltyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFamilyFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentAddFamilyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddFamilyFragment()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddFamilyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.verify.setOnClickListener {
            val name = binding.name.text.toString()
            val plan = binding.plan.text.toString()
            val size = binding.size.text.toString().toInt()

            val family = Family(fullname = name, packageType = plan, familySize = size)
            binding.progress.isVisible = true
            viewModel.createFamily(family)
        }

        viewModel.addFamily.observe(requireActivity()) {
            val result = it ?: return@observe
            binding.progress.isVisible = false
            Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
        }
    }
}