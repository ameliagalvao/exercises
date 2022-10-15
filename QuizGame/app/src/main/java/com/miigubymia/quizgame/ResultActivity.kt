package com.miigubymia.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.miigubymia.quizgame.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    val database = FirebaseDatabase.getInstance()
    val reference = database.reference.child("scores")
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var userCorrect = ""
    var userWrong = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let {
                    val userUID = it.uid
                    userCorrect = snapshot.child(userUID).child("correct").value.toString()
                    userWrong = snapshot.child(userUID).child("wrong").value.toString()

                    binding.tvCorrectTotal.text = userCorrect
                    binding.tvWrongTotal.text = userWrong
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnExit.setOnClickListener {
            finish()
        }

        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
            // Damos o finish para que essa atividade seja removida do backstack e o usuário não
            // possa voltar para ela.
            finish()
        }

    }
}