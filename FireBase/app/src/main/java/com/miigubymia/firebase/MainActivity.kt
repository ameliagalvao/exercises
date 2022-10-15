package com.miigubymia.firebase

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.miigubymia.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    val database:FirebaseDatabase = FirebaseDatabase.getInstance()
    // se parássemos no reference iríamos acessar apenas o ramo principal
    // como queremos qacessar o MyUsers precisamo usar o child e dizer o que queremos acessar
    val reference:DatabaseReference = database.reference.child("MyUsers")
    val firebaseStorage:FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference:StorageReference = firebaseStorage.reference
    var userList = ArrayList<Users>()
    lateinit var adapter: UsersAdapter
    val imageNameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        // Vai permitir utilizar o movimento de deslizar dos dedos
        // Trata-se de uma classe abstrata e por isso não podemos criar uma instância dela.
        // por isso vamos usar a palavra object que é da classe anônima
        // no callback devemos informar a direção para qual vamos arrastar
        // nesse caso o 0 é para o grab que não vamos usar.
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            // esse método é usado para segurar, arrastar e soltar no android
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }
            //método para o deslizar o dedo
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // precisamo do id do usuário aqui, mas não da posição dele na lista, já
                // que o processo de apagar vai depender apenas do id. Para isso vamos escrever uma
                // função na classe adapter
                val id = adapter.getUserId(viewHolder.adapterPosition)
                // passamos o parâmetro acima (o id) para que o correspondente seja apagado
                reference.child(id).removeValue()
                // Passando a posição para função da imagem
                val imageName = adapter.getImageName(viewHolder.adapterPosition)
                // variável que vai referenciar a imgm que deve ser deletada
                val imageReference = storageReference.child("images").child(imageName)
                imageReference.delete()

                Toast.makeText(applicationContext, "Deleted.", Toast.LENGTH_SHORT).show()
            }
        // vamos conectar o itemTouch ao recyclerView
        }).attachToRecyclerView(binding.rvData)

        retrieveData()
    }

    fun retrieveData(){
        reference.addValueEventListener(object : ValueEventListener{
            //Monitora o bd para quando houver uma mudança nele refletí-la aqui.
            // é armazenado como json então é preciso converter.
            // Quando usamos o ValueEventListener, a gente deve limpar o arrayList antes de mostrar ele,
            // se não os itens vão ficar repetidos.
            // Entretando se usarmos o ChildEventListener não será necessário apagar. Inclusive torna
            // possível passar os dados ao ArrayList sem precisar do ForEach.
            // No caso de usar o child, o que for escrito no onChildAdded só permite ler os dados quando eles
            // forem adicionados. Para ler mudanças nos dados precisamo usar o onChildChange. No caso de apagar
            // devemos usar o onChildRemoved.
            override fun onDataChange(snapshot: DataSnapshot) {

                // deve ficar antes de tudo para resetar a lista
                userList.clear()

                //vamos usar um Loop para pegar os dados de cada usuário e colocá-los num arrayList
                // Cada usuário no bd será transferido para o objeto eachUser
                // vai rodar enquanto tiver dados no database e cada usuário vai ser passado para a variável user.
                for (eachUser in  snapshot.children){
                    // precisamos especificar o tipo de dado que vamos pegar
                    val user = eachUser.getValue(Users::class.java)
                    if (user != null){
                        userList.add(user)
                    }
                    adapter = UsersAdapter(this@MainActivity, userList)
                    // como os dados serão exibidos:
                    binding.rvData.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvData.adapter = adapter
                }
            }
            //Se não houver o que fazer ou no caso de algum erro, definimos o que irá acontecer.
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    // Design do menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // inflamos o novo layout e mudamos o return para true.
        menuInflater.inflate(R.menu.menu_delete_all, menu)
        return true
    }

    // Vamos definir o que acontece ao clicar em cada item do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // se o item clicado for o da lixeira
        if (item.itemId == R.id.deleteAll){
            showDialogMessage()
        }else if(item.itemId == R.id.btnSignOut){
            //vai deslogar o usuário do firebase
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Users")
        dialogMessage.setMessage("If Yes, all users will be deleted.")
        dialogMessage.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
            dialogInterface.cancel()
        })
        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->

            reference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (eachUser in snapshot.children){
                        // passa cada usuário para a variável user
                        val user = eachUser.getValue(Users::class.java)
                        if (user != null){
                            imageNameList.add(user.imageName)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            reference.removeValue().addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    for (imageName in imageNameList){
                        val imageReference = storageReference.child("images").child(imageName)
                        imageReference.delete()
                    }

                    adapter.notifyDataSetChanged()

                    Toast.makeText(applicationContext, "Deleted.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        dialogMessage.create().show()
    }

}