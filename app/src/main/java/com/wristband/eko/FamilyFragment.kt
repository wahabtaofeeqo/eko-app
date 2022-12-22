package com.wristband.eko

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.FamilyAdapter
import com.wristband.eko.adapters.UserAdapter
import com.wristband.eko.databinding.FragmentFamilyBinding
import com.wristband.eko.databinding.FragmentUsersBinding
import com.wristband.eko.vm.SharedViewModel
import com.wristband.eko.vm.UserViewModel
import es.dmoral.toasty.Toasty

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FamilyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FamilyFragment : Fragment() {

    lateinit var adapter: FamilyAdapter
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentFamilyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFamilyBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        const val FAMILY_ID = "familyID"

        @JvmStatic
        fun newInstance() = FamilyFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        adapter = FamilyAdapter(requireActivity().applicationContext)

        //
        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        //
        viewModel.families.observe(requireActivity()) { response ->
            if(response == null) return@observe
            adapter.submitList(response)

            if(adapter.itemCount > 0) {
                binding.empty.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
        }
    }
}