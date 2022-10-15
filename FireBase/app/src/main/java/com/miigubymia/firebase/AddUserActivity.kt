package com.miigubymia.firebase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.miigubymia.firebase.databinding.ActivityAddUserBinding
import com.squareup.picasso.Picasso
import java.util.*

class AddUserActivity : AppCompatActivity() {

    lateinit var activityAddUserBinding:ActivityAddUserBinding
    // dois objetos necessário para acessarmos o cloud storage do firebase
    val firebaseStorage:FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference:StorageReference = firebaseStorage.reference
    // necessária para salvar o endereço da imagem
    var imageURI: Uri? = null
    // o tipo de dado retornado pelo resultLauncher vai ser intent
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = activityAddUserBinding.root
        setContentView(view)

        //Quando usamos o activity result launcher é necessário registrar ele no
        // onCreate, se não o aplicativo não vai funcionar. Para isso vamos criar uma função.
        registerActivityForResult()

        // título para a action bar caso ela exista
        supportActionBar?.title = "Add User"

        activityAddUserBinding.btnAdd.setOnClickListener {
            uploadPhoto()
        }

        activityAddUserBinding.ivProfilePicture.setOnClickListener {
            chooseImage()
        }

    }

    fun registerActivityForResult(){
        // o primeiro método é para registrar o objeto.
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        , ActivityResultCallback { result ->
                // vamos exibir a imagem escolhida pelo usuário
                val resultCode = result.resultCode
                val imageData = result.data
                // se o usuário escolheu uma imagem...
                if (resultCode == RESULT_OK && imageData != null){
                    // precisamos descobrir o caminho do arquivo escolhido pelo usuário
                    // e o colocar em um container. Para isso vamos criar um obj da classe URI na
                    // parte global. Associamos o caminho ao objeto URI:
                    imageURI = imageData.data
                    // Precisamo adicionar a biblioteca Picasso para exibir a imagem através do URI num imageView.
                    // Como o URI pode ser nulo, devemos usar o let
                    imageURI?.let {
                        // podemos usar o it ao invés do imageURI
                        Picasso.get().load(it).into(activityAddUserBinding.ivProfilePicture)
                    }
                }
            })
    }

    fun chooseImage(){
        // Se o usuário não deu permissão
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else{
            // Vamos usar o intent para fazer o processo de seleção da imagem
            val intent = Intent()
            // Vamos definir o tipo de intent para escolher quais os tipos de arquivo que
            // serão mostrados ao usuário
            intent.type = "image/*"
            // Vai abrir o sistema de arquivos do usuário para que ele escolha uma imagem
            intent.action = Intent.ACTION_GET_CONTENT
            // precisamos usar algum método para iniciar o intent. Nesse caso como não vamos mudar
            // de activity iremos utilizar o activity result launcher
            activityResultLauncher.launch(intent)
        }
    }
    // Essa função vai controlar a escolha do usuário
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    fun addUserToDatabase(url:String, imageName:String){
        val name = activityAddUserBinding.etNameAdd.text.toString()
        val age = activityAddUserBinding.etNumberAdd.text.toString().toInt()
        val email = activityAddUserBinding.etMailAdd.text.toString()
        // o método push vai criar uma chave única para cada usuário
        val id = myReference.push().key.toString()
        val user = Users(id, name, age, email, url, imageName)
        //Adicionando o objeto usuário ao db
        // vamos usar o id para registrar cada usuário pelo seu id
        // a função setValue é para escrever no db
        myReference.child(id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Added.", Toast.LENGTH_SHORT).show()
                activityAddUserBinding.btnAdd.isClickable = true
                activityAddUserBinding.pbarAddUser.visibility = View.INVISIBLE
                finish()
            } else{
                Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        // É possível adicionar observadores para verificar se tudo ocorreu bem
    }

    fun uploadPhoto(){
        // o usuário só vai poder clicar o botão de upload uma única vez
        // se não ele poderia enviar várias vezes o mesmo arquivo para nuvem
        activityAddUserBinding.btnAdd.isClickable = false
        // tornar a progress bar visível
        activityAddUserBinding.pbarAddUser.visibility = View.VISIBLE
        // para poder guardar os arquivos numa pasta diferente precisamos criar um objeto para acessar essa
        // pasta especificamente
        // Para que cada imagem seja salva com um nome diferente, vamos usar um UUID que gera randomicamente
        // chaves únicas para cada imagem.
        val imageName = UUID.randomUUID().toString()
        val imageReference = storageReference.child("images").child(imageName)
        imageURI?.let {
            imageReference.putFile(it).addOnSuccessListener {
                Toast.makeText(applicationContext, "Success.", Toast.LENGTH_SHORT).show()
                // Vamos gerar o URL baixável e salvar no real time db
                // para isso vamos criar uma variavel que aponte para o nome da imagem
                val myUploadedImageReference = storageReference.child("images").child(imageName)
                // aqui geramos o url
                myUploadedImageReference.downloadUrl.addOnSuccessListener {url ->
                    val imageURL = url.toString()
                    addUserToDatabase(imageURL, imageName)
                }
            }.addOnFailureListener {ex ->
                Toast.makeText(applicationContext, ex.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }

}