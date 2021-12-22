package com.example.simpletodo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val KEY_PRIORITY = "key_priority"
private const val TAG = "PriorityDF"
/**
 * A simple [Fragment] subclass.
 * Use the [PriorityDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PriorityDialogFragment : DialogFragment() {

    lateinit var priorityDialogListener: PriorityDialogListener

    // Pass in selected priority(->color) through interface PriorityDialogListener
    // Reusability of fragment
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
        @JvmStatic
//        fun newInstance() = PriorityDialogFragment()
        fun newInstance(priority: Priority) = PriorityDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_PRIORITY, priority)
            }
        }

        fun showPriorityDialog(appCompatActivity: AppCompatActivity, priority: Priority) {
            val priorityDialog = newInstance(priority)
            priorityDialog.show(appCompatActivity.supportFragmentManager, "Priority picker")
        }
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
        val btnPriorityOK = view.findViewById<Button>(R.id.btnPriorityOK)

        // TODO: remember what was selected
        var newPriority = arguments?.getSerializable(KEY_PRIORITY) as Priority
        rgPriorities.check(Priority.findRadioButtonId(newPriority))
        Log.d(TAG + "onViewCreated", "newPriority is " + newPriority)

        // (Re)Select priority via radioGroup
        rgPriorities.setOnCheckedChangeListener { radioGroup, checkedId ->

            newPriority = when (checkedId) {
                R.id.rbP1 -> Priority.ONE
                R.id.rbP2 -> Priority.TWO
                R.id.rbP3 -> Priority.THREE
                else -> Priority.DEFAULT
            }
        }

        btnPriorityOK.setOnClickListener {
            priorityDialogListener.onPrioritySelected(newPriority)
//            Log.d("PriorityDF", "newPriority is " + newPriority)
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

//    override fun onDetach() {
//        super.onDetach()
//        priorityDialogListener = null
//    }
}