package com.miigubymia.trackdelivery.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.trackdelivery.R

class ListFragment : Fragment() {

    lateinit var listAdapter: ListAdapter
    lateinit var recyclerView:RecyclerView
    var listViewModel = ListViewModel()
    var registers = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        if (registers.isEmpty()){
            val list = listViewModel.getListFromSharedPref(requireActivity())
            if (list.isNotEmpty()){
                registers.addAll(list)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recViewRegister)
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAdapter = ListAdapter(registers)
        recyclerView.adapter = listAdapter
    }
}
