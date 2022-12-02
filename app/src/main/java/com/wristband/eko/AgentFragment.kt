package com.wristband.eko

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.AgentAdapter
import com.wristband.eko.databinding.FragmentAgentBinding
import com.wristband.eko.entities.Agent
import com.wristband.eko.vm.AgentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AgentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgentFragment : Fragment() {

    lateinit var viewModel: AgentViewModel
    private var list = mutableListOf<Agent>()

    lateinit var adapter: AgentAdapter
    private lateinit var binding: FragmentAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AgentViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = AgentFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAgentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.loadAgents()
        adapter = AgentAdapter(requireActivity().applicationContext, list)

        //
        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        //
        binding.add.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }

        //
        viewModel.agents.observe(requireActivity()) { response ->
            if(response == null) return@observe
            list.addAll(response.body)
            if(list.size > 0) {
                binding.empty.visibility = View.GONE
                binding.recycler.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        }
    }
}