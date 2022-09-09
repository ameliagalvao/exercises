package com.miigubymia.perfildeinvestidor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.Navigation

class FifthFragment : Fragment() {

    var answer = 0
    var data = SharedPreferencesFunctions()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        view.findViewById<TextView>(R.id.tvQuestion1).text = getString(R.string.question5)
        view.findViewById<TextView>(R.id.tvTitleQuestion1).text = "Questão 5"

        var letterA = view.findViewById<RadioButton>(R.id.radioA1)
        var letterB = view.findViewById<RadioButton>(R.id.radioB1)
        var letterC = view.findViewById<RadioButton>(R.id.radioC1)
        var letterD = view.findViewById<RadioButton>(R.id.radioD1)
        letterA.text = "Resgataria toda a aplicação e aplicaria na poupança "
        letterB.text = "Manteria aplicação aguardando uma melhora do mercado"
        letterC.text = "Aumentaria a aplicação para aproveitar as oportunidades do mercado"
        letterD.isVisible = false

        var radioGroup = view.findViewById<RadioGroup>(R.id.radiogQuestion1)
        val nextBtn = view.findViewById<Button>(R.id.btnNext1)
        nextBtn.setOnClickListener {
            if (radioGroup.checkedRadioButtonId == -1){
                Toast.makeText(context,"Por favor, selecione uma opção.", Toast.LENGTH_SHORT).show()
            }else{
                Navigation.findNavController(view).navigate(R.id.action_fifthFragment_to_sixthFragment)
                when(radioGroup.checkedRadioButtonId){
                    R.id.radioA1 -> answer = 0
                    R.id.radioB1 -> answer = 2
                    R.id.radioC1 -> answer = 4
                    R.id.radioD1 -> answer = 0
                    R.id.radioE1 -> answer = 0
                }
            }
        }

        return view
    }
    override fun onPause() {
        super.onPause()
        context?.let { data.saveInt(it,answer,"question5") }
    }
}