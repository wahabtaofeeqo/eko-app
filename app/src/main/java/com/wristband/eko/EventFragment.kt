package com.wristband.eko

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.wristband.eko.databinding.FragmentEventBinding
import com.wristband.eko.entities.Event
import com.wristband.eko.vm.SharedViewModel
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [EventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventFragment : Fragment() {

    private var event: Event? = null
    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.getEvent()

        //
        binding.update.setOnClickListener {
            val input: String = binding.date.text.toString().trim()
            if(input.isNotEmpty()) {
                val date = SimpleDateFormat("dd-MM-yyyy").parse(input)
                if (date != null) {
                    viewModel.updateOrCreate(date)
                }
            }
        }

        //
        binding.date.setOnClickListener {
            val calendar = Calendar.getInstance()
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                binding.date.setText("${day}-${month + 1}-${year}")
            }

            val dialog = DatePickerDialog(requireContext(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }

        //
        viewModel.event.observe(requireActivity()) { response ->
            if(response == null) return@observe
            this.event = response.body
            response.let {
                if (it.status) {
                    val format = SimpleDateFormat("dd-MM-yyyy")
                    it.body?.let { body ->
                        binding.dateTxt.text = body.date?.let { it1 -> format.format(it1) }
                    }
                    binding.date.text?.clear()
                    Toasty.success(requireContext(), it.message).show()
                }
                else {
                    Toasty.error(requireContext(), it.message).show()
                }
            }
        }
    }
}