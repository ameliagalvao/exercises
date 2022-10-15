package com.miigubymia.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.miigubymia.quizgame.databinding.ActivityQuizBinding
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    val database = FirebaseDatabase.getInstance()
    val reference = database.reference.child("Questions")
    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    // o numero dela deve ser zero porque a contagem do index começa no zero
    var questionNumber = 0
    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0
    lateinit var timer: CountDownTimer
    // o tempo deve ser indicado em ms
    private val totalTime = 25000L
    // para acompanhar quando o timer estiver rodando ou não
    var timerContinue = false
    // criamos essa variavél para que ela mude, enquanto a totalTime não vai mudar
    var leftTime = totalTime
    val auth = FirebaseAuth.getInstance()
    // Permite que saibamos o uid do usuário
    val user = auth.currentUser
    val scoreRef = database.reference

    // vamos criar um obj do HashSet porque nele não podemos ter valores repetidos, assim caso
    // o random forneça números iguais, o hash simplesmente vai ignorar o que for repetido.
    val questions = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Enquanto o tamanho do hash fr menor que 3 ele vai rodar o loop
        do {
            // vai gerar números de 1 à 4
            val number = Random.nextInt(1, 5)
            // Vamos usar um log para ver os números geardos
            Log.d("number", number.toString())
            questions.add(number)
        }while (questions.size < 3)
        Log.d("NumberOfQuestions", questions.toString())
        gameLogic()

        binding.btnFinish.setOnClickListener {
            sendScore()
        }

        binding.btnNext.setOnClickListener {
            resetTimer()
            gameLogic()
        }

        binding.tvLetterA.setOnClickListener {
            pauseTimer()
            userAnswer = "a"
            checkAnswer(binding.tvLetterA)
            disableClickableOfOptions()
        }

        binding.tvLetterB.setOnClickListener {
            pauseTimer()
            userAnswer = "b"
            checkAnswer(binding.tvLetterB)
            disableClickableOfOptions()
        }

        binding.tvLetterC.setOnClickListener {
            pauseTimer()
            userAnswer = "c"
            checkAnswer(binding.tvLetterC)
            disableClickableOfOptions()
        }

        binding.tvLetterD.setOnClickListener {
            pauseTimer()
            userAnswer = "d"
            checkAnswer(binding.tvLetterD)
            disableClickableOfOptions()
        }

    }

    private fun startTimer(){
        // como countDownTimer é uma classe abstrata, precisamos usar o object para criar um objeto.
        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(timeUntilFinish: Long) {
                // o método onTick vai funcionar até que o parâmetro leftTime (tempo total) seja
                // igual ao tempo que resta.
                leftTime = timeUntilFinish
                // Função para atualizar o texto que mostra o tempo a cada segundo
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickableOfOptions()
                // vai resetar o contador
                resetTimer()
                // Vai atualizar o texto
                updateCountDownText()
                binding.tvQuestion.text = "Sorry, Time is up!"
                timerContinue = false
            }

        }.start()
        timerContinue = true
    }

    private fun disableClickableOfOptions(){
        binding.tvLetterA.isClickable = false
        binding.tvLetterB.isClickable = false
        binding.tvLetterC.isClickable = false
        binding.tvLetterD.isClickable = false

    }

    private fun checkAnswer(textView: TextView){
        if(correctAnswer == userAnswer){
            textView.setBackgroundColor(Color.GREEN)
            userCorrect++
            binding.tvNumberCorrect.text = userCorrect.toString()
        }else{
            textView.setBackgroundColor(Color.RED)
            userWrong++
            binding.tvNumberWrong.text = userWrong.toString()
            findAnswer()
        }
    }

    private fun findAnswer(){
        when(correctAnswer){
            "a" -> binding.tvLetterA.setBackgroundColor(Color.GREEN)
            "b" -> binding.tvLetterA.setBackgroundColor(Color.GREEN)
            "c" -> binding.tvLetterA.setBackgroundColor(Color.GREEN)
            "d" -> binding.tvLetterA.setBackgroundColor(Color.GREEN)
        }
    }

    private fun gameLogic(){

        restoreOptions()

        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Esse método vai nos informar quantos elementos existem como filhos do ramo principal do db
                // Precisamos converter para inteiro porque por padrão ele é do tipo Long, já que podem ter números
                // demais que não caibam no inteiro.
                questionCount = snapshot.childrenCount.toInt()

                // Rodará enquanto o no for menor que o tamanho do hash
                if (questionNumber < questions.size){
                    // o snapshot acessa a parte de questões no db, mas como queremos acessar os valores que estão nos
                    // filhos, devemos usar o child. Usamos o HashList questions.elementAt para acessar cada elemento do hash
                    // através do index, que nesse caso é o questionNumber. Precisamos converter para String,
                    // porque o nome da chave é uma string.
                    question = snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()

                    binding.tvQuestion.text = question
                    binding.tvLetterA.text = answerA
                    binding.tvLetterB.text = answerB
                    binding.tvLetterC.text = answerC
                    binding.tvLetterD.text = answerD

                    binding.pbarQuestion.visibility = View.INVISIBLE
                    binding.linearScore.visibility = View.VISIBLE
                    binding.linearQuestion.visibility = View.VISIBLE
                    binding.linearButtons.visibility = View.VISIBLE

                    startTimer()

                }else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!! You answered all the questions.")
                    dialogMessage.setPositiveButton("See Result"){dialogMessage, position ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play again"){dialogMessage, position ->
                        val intent = Intent(this@QuizActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()
                }
                questionNumber++
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun restoreOptions(){
        binding.tvLetterA.setBackgroundColor(Color.WHITE)
        binding.tvLetterB.setBackgroundColor(Color.WHITE)
        binding.tvLetterC.setBackgroundColor(Color.WHITE)
        binding.tvLetterD.setBackgroundColor(Color.WHITE)

        binding.tvLetterA.isClickable = true
        binding.tvLetterB.isClickable = true
        binding.tvLetterC.isClickable = true
        binding.tvLetterD.isClickable = true
    }

    private fun updateCountDownText(){
        // a cada segundo o texto será atualizado
        val remainingTime:Int = (leftTime/1000).toInt()
        binding.tvSeconds.text = remainingTime.toString()
    }

    private fun pauseTimer(){
        timer.cancel()
        timerContinue = false
    }

    private fun resetTimer(){
        pauseTimer()
        // coloca o valor do timer para o inicial
        leftTime = totalTime
        updateCountDownText()
    }

    fun sendScore(){
        user?.let {
            val userUID = it.uid
            //vai armazenar a qunatidade de respostas corretas do usuário
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnCompleteListener {
                Toast.makeText(applicationContext, "Scores sent successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}