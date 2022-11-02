package com.miigubymia.trackdelivery.tracking

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.miigubymia.trackdelivery.R
import com.miigubymia.trackdelivery.storage.ListViewModel
import java.io.File
import java.io.FileWriter
import java.util.*

class TrackingFragment : Fragment() {

    val listViewModel = ListViewModel()
    val registers : MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tracking, container, false)
        val btnStart = view.findViewById<Button>(R.id.btnStartTrack)
        val btnStop = view.findViewById<Button>(R.id.btnStopTrack)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        var currentTime = ""
        var fileName = "exemplo.crd"
        val textViewLocation = view.findViewById<TextView>(R.id.tvLocation)

        btnRegister.setOnClickListener {
                // Verifica se o serviço está rodando
                if (isMyServiceRunning(LocationService::class.java, requireContext())){
                    // Pega a data
                    val simpleFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    currentTime = simpleFormat.format(Date())
                    //Localização pega no sharedPref
                    val currentLocation = requireActivity().application.getSharedPreferences("SaveLocation",
                        Service.MODE_PRIVATE
                    ).getString("CurrentLocation", "...")
                    // Salva o arquivo
                    if (currentLocation != "..."){
                        fileName = "$currentTime.crd"
                        context?.let { it1 ->
                            if (currentLocation != null) {
                                write(it1, requireActivity(), fileName, currentLocation)
                            }
                        }
                        // Toast
                        Toast.makeText(context, currentLocation, Toast.LENGTH_SHORT).show()
                        // Tentativa lista
                        listViewModel.addToList(fileName, registers)
                        listViewModel.updateListInSharedPref(requireActivity(), registers)
                    }
                    }
            }

        btnStart.setOnClickListener {
            if (!requireActivity().applicationContext.hasLocationPermission()) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Permissão Necessária")
                builder.setMessage("Para que o aplicativo possa acompanhar a sua localização, você precisa aceitar a permissão de rastreamento.")
                builder.setPositiveButton("Conceder permissão") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                        ),
                        0
                    )
                }
                builder.setNegativeButton("Cancelar") { dialog, which ->
                    Toast.makeText(context, "Permissão NÃO concedida", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            } else {
                Intent(requireActivity().applicationContext, LocationService::class.java).apply {
                    action = LocationService.ACTION_START
                    requireActivity().startService(this)
                }
            }
        }

        btnStop.setOnClickListener {
            Intent(requireActivity().applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                // para enviar o intent ao serviço, tem nada a ver com começar a trackear
                requireActivity().startService(this)
            }
        }


        return view
    }

    fun write(context:Context, activity: Activity, fileName:String, content:String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // podemos usar qualquer número para o requestCode. Ele é só um identificador único do request
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }else{
            val externalDir = activity.applicationContext.getExternalFilesDir(null)
            val file = File(externalDir, fileName)
            try {
                if (!file.exists()) {
                    file.createNewFile()
                }
                val writer = FileWriter(file, true)
                writer.use { it.write(content) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
