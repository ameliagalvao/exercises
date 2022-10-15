package com.example.todolist

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBind:ActivityMainBinding
    var itemList = ArrayList<String>()
    val fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBind = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBind.root
        setContentView(view)

        itemList = fileHelper.readData(this)
        // adapter da lista
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,itemList)
        mainBind.listView.adapter = arrayAdapter
        mainBind.buttonADD.setOnClickListener {
            //pega o texto do widget de digitar e coloca no array
            itemList.add(mainBind.addToDo.text.toString())
            //limpa o widget de digitar
            mainBind.addToDo.setText("")
            //salva o conteúdo do array no arquivo
            fileHelper.writeData(itemList,applicationContext)
            //notifica o adapter das mudanças
            arrayAdapter.notifyDataSetChanged()
        }
        mainBind.listView.setOnItemClickListener { adapterView, view, position, l ->
            var alert = AlertDialog.Builder(this)
            alert.setTitle("Apagar")
            alert.setMessage("Tem certeza que deseja apagar este item")
            // não pode clicar fora do alertapara fechar
            alert.setCancelable(false)
            alert.setNegativeButton("Não", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            alert.setPositiveButton("Sim", DialogInterface.OnClickListener { dialogInterface, i ->
                itemList.removeAt(position)
                arrayAdapter.notifyDataSetChanged()
                fileHelper.writeData(itemList,applicationContext)
            })
            alert.create().show()
        }
    }
}