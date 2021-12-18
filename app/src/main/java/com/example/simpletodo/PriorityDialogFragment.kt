package com.example.simpletodo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val KEY_PRIORITY = "key_priority"

/**
 * A simple [Fragment] subclass.
 * Use the [PriorityDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriorityDialogFragment : DialogFragment() {

    lateinit var priorityDialogListener: PriorityDialogListener
    interface PriorityDialogListener{
        fun onPrioritySelected(priority: Priority)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param priority: Priority that corresponds to a color
         * @return A new instance of fragment PriorityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PriorityDialogFragment()
//        fun newInstance(priority: Priority) = PriorityDialogFragment().apply {
//            arguments = Bundle().apply {
//                putString(KEY_PRIORITY, priority.name)
//            }
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_priority, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgPriorities = view.findViewById<RadioGroup>(R.id.rgPriorities)
        val btnPrioritySave = view.findViewById<Button>(R.id.btnPrioritySave)
//        val rbP1 = view.findViewById<RadioGroup>(R.id.rbP1)
//        val rbP2 = view.findViewById<RadioGroup>(R.id.rbP2)
//        val rbP3 = view.findViewById<RadioGroup>(R.id.rbP3)
//        val rbP4 = view.findViewById<RadioGroup>(R.id.rbP4)

        rgPriorities.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.rbP1 -> priorityDialogListener.onPrioritySelected(Priority.ONE)
                R.id.rbP2 -> priorityDialogListener.onPrioritySelected(Priority.TWO)
                R.id.rbP3 -> priorityDialogListener.onPrioritySelected(Priority.THREE)
                else -> priorityDialogListener.onPrioritySelected(Priority.DEFAULT)
            }
        }

        btnPrioritySave.setOnClickListener {
            dismiss()
        }


    }

    // Where to send "priority" as input?
        // to an activity that implements the interface
    // called when fragment attached to activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
//         if (context is PriorityDialogListener)
        try {
            this.priorityDialogListener = context as PriorityDialogListener
        } catch (e: ClassCastException) {
            e.printStackTrace()
            throw ClassCastException(context.toString()
                    + "MainActivity must implement PriorityDialogListener")
        }
    }

}