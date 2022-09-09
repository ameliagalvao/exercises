package com.miigubymia.perfildeinvestidor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation

class SecondFragment : Fragment() {

    var answer = 0
    var data = SharedPreferencesFunctions()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        view.findViewById<TextView>(R.id.tvQuestion1).text = getString(R.string.question2)
        view.findViewById<TextView>(R.id.tvTitleQuestion1).text = "Questão 2"

        var letterA = view.findViewById<RadioButton>(R.id.radioA1)
        var letterB = view.findViewById<RadioButton>(R.id.radioB1)
        var letterC = view.findViewById<RadioButton>(R.id.radioC1)
        var letterD = view.findViewById<RadioButton>(R.id.radioD1)
        letterA.text = "Preservação do capital para não perder valor ao longo do tempo, assumindo baixos riscos de perdas"
        letterB.text = "Aumento gradual do capital ao longo do tempo, assumindo riscos moderados"
        letterC.text = "Aumento do capital acima da taxa de retorno média do mercado, mesmo que isso implique assumir riscos de perdas elevadas"
        letterD.text = "Obter no curto prazo retornos elevados e significativamente acima da taxa de retorno média do mercado, assumindo riscos elevados"

        var radioGroup = view.findViewById<RadioGroup>(R.id.radiogQuestion1)
        val nextBtn = view.findViewById<Button>(R.id.btnNext1)
        nextBtn.setOnClickListener {
            if (radioGroup.checkedRadioButtonId == -1){
                Toast.makeText(context,"Por favor, selecione uma opção.", Toast.LENGTH_SHORT).show()
            }else{
                Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_thirdFragment)
                when(radioGroup.checkedRadioButtonId){
                    R.id.radioA1 -> answer = 0
                    R.id.radioB1 -> answer = 2
                    R.id.radioC1 -> answer = 4
                    R.id.radioD1 -> answer = 5
                    R.id.radioE1 -> answer = 0
                }
            }
        }
        return view
    }
    override fun onPause() {
        super.onPause()
        context?.let { data.saveInt(it,answer,"question2") }
    }
}