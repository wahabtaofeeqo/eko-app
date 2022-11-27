package com.wristband.eko

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.wristband.eko.adapters.AgentAdapter
import com.wristband.eko.databinding.AgentViewBinding
import com.wristband.eko.databinding.FragmentAgentBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AgentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgentFragment : Fragment() {

    lateinit var adapter: AgentAdapter
    private lateinit var binding: FragmentAgentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AgentAdapter()
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
        binding.recycler.adapter = adapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(itemDecoration)

        binding.add.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}