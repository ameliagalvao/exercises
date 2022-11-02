package com.miigubymia.trackdelivery.storage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miigubymia.trackdelivery.R
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

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


        listAdapter.setOnFileClickListener(object : ListAdapter.onFileClickListener {
            override fun onFileClick(position: Int) {
                var texto = readClickedFile(registers[position], requireContext(), requireActivity())

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Localização em ${registers[position]}")
                builder.setMessage("$texto")
                builder.setNegativeButton("Ok") { dialog, which ->
                }
                builder.show()

                //Toast.makeText(context, texto.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun readClickedFile(fileName: String, context: Context, activity: Activity):String{
        var line = "Localização: "
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // podemos usar qualquer número para o requestCode. Ele é só um identificador único do request
            ActivityCompat.requestPermissions(activity, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }else{
            val externalDir = activity.applicationContext.getExternalFilesDir(null)
            val file = File(externalDir, fileName)
            val stringBuilder = StringBuilder()
            try {
                if (file.exists()) {
                    val br = BufferedReader(FileReader(file))
                    line = br.readLine()
                    while (line != null){
                        stringBuilder.append(line)
                        stringBuilder.append("\n")
                        line = br.readLine()
                    }
                    br.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return line
    }
}