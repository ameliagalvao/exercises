package com.example.todolist

import android.content.Context
import java.io.*

class FileHelper {

    val FILENAME = "listinfo.dat"

    fun writeData (item:ArrayList<String>, context:Context){
        // A classe FileOutputStream é usada para gravar dados em um arquivo.
        // O método openFileOutput vai criar um arquivo na memória do dispositivo e o abrir.
        // Ele recebe dois parâmetros o nome do arquivo e o contexto, que nesse caso vai ser privado para
        // que outros aplicativos não tenham acesso ao arquivo. O recomendado é sempre salvar como privado.
        val fos:FileOutputStream = context.openFileOutput(FILENAME,Context.MODE_PRIVATE)
        // esse método junto com anterior vai gravar os dados nos arquivo
        val oas = ObjectOutputStream(fos)
        oas.writeObject(item)
        // para fechar o arquivo:
        oas.close()
    }

    fun readData(context: Context):ArrayList<String>{
        var itemList:ArrayList<String>
        try {
            //abre o arquivo
            val fis:FileInputStream = context.openFileInput(FILENAME)
            //lê o arquivo
            val ois = ObjectInputStream(fis)
            //salva os dados na lista:
            // converte os dados que são do tipo objeto em string
            itemList = ois.readObject() as ArrayList<String>
        }catch (e: FileNotFoundException){
            itemList = ArrayList()
        }

        return itemList
    }
}