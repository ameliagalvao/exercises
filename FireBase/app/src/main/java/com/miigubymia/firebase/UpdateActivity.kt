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
import com.miigubymia.firebase.databinding.ActivityUpdateBinding
import com.squareup.picasso.Picasso
import java.util.*

class UpdateActivity : AppCompatActivity() {
    lateinit var activityUpdateBinding: ActivityUpdateBinding
    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference: StorageReference = firebaseStorage.reference
    var imageURI: Uri? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUpdateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = activityUpdateBinding.root
        setContentView(view)

        supportActionBar?.title = "Update user"
        getAndSetData()

        activityUpdateBinding.btnUpdate.setOnClickListener {
            uploadPhoto()
        }

        registerActivityForResult()

        activityUpdateBinding.ivProfilePictureUpdate.setOnClickListener {
            chooseImage()
        }

    }

    fun uploadPhoto(){
        // o usuário só vai poder clicar o botão de upload uma única vez
        // se não ele poderia enviar várias vezes o mesmo arquivo para nuvem
        activityUpdateBinding.btnUpdate.isClickable = false
        // tornar a progress bar visível
        activityUpdateBinding.pbarAddUserUpdate.visibility = View.VISIBLE
        // como já temos o nome da imagem armazenado, vamos pegar pelo intent.
        val imageName = intent.getStringExtra("imageName").toString()
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
                    updateData(imageURL, imageName)
                }
            }.addOnFailureListener {ex ->
                Toast.makeText(applicationContext, ex.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun registerActivityForResult(){
        // o primeiro método é para registrar o objeto.
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
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
                        Picasso.get().load(it).into(activityUpdateBinding.ivProfilePictureUpdate)
                    }
                }
            })
    }

    fun chooseImage(){
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

    fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0).toString()
        val email = intent.getStringExtra("email")
        val imageUrl = intent.getStringExtra("imageUrl").toString()


        activityUpdateBinding.etNameUpdate.setText(name)
        activityUpdateBinding.etNumberUpdate.setText(age)
        activityUpdateBinding.etMailUpdate.setText(email)
        Picasso.get().load(imageUrl).into(activityUpdateBinding.ivProfilePictureUpdate)
    }

    fun updateData(imageUrl:String, imageName:String){
        val updatedName = activityUpdateBinding.etNameUpdate.text.toString()
        val updatedAge = activityUpdateBinding.etNumberUpdate.text.toString().toInt()
        val updatedEmail = activityUpdateBinding.etMailUpdate.text.toString()
        val userId = intent.getStringExtra("UserID").toString()
        // como os dados são guardados no firebase como pares chave-valor, precisamos
        // transformas os dados acima num map.
        val userMap = mutableMapOf<String, Any>()
        userMap["userId"] = userId
        userMap["userName"] = updatedName
        userMap["userAge"] = updatedAge
        userMap["userEmail"] = updatedEmail
        userMap["url"] = imageUrl
        userMap["imageName"] = imageName
        // determinamos quem vamos atualizar pelo ID
        // colocamos um listener para acompanhar o update
        myReference.child(userId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Updated.", Toast.LENGTH_SHORT).show()
                activityUpdateBinding.btnUpdate.isClickable = true
                activityUpdateBinding.pbarAddUserUpdate.visibility = View.INVISIBLE
                // fecha essa atividade e chama a main
                finish()
            }
        }
    }

}