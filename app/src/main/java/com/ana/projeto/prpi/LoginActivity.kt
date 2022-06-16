package com.ana.projeto.prpi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ana.projeto.prpi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

lateinit var auth : FirebaseAuth
lateinit var botao : Button
lateinit var progresso: ProgressBar
lateinit var intentHomeActivity : Intent
lateinit var userFoto : ImageView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        botao = findViewById(R.id.lgButton)
        progresso = findViewById(R.id.lgProgresso)
        auth = FirebaseAuth.getInstance()
        progresso.visibility= View.INVISIBLE

        intentHomeActivity = Intent(this, Home2Activity::class.java)
        userFoto = findViewById(R.id.lgImage)

        userFoto.setOnClickListener{
            var registroIntent = Intent(this, RegistroActivity::class.java)
            startActivity(registroIntent)
            finish()
        }


        botao.setOnClickListener{
            botao.visibility = View.INVISIBLE
            progresso.visibility = View.VISIBLE

            var email = findViewById<EditText>(R.id.lgEmail).text.toString()
            var senha = findViewById<EditText>(R.id.lgSenha).text.toString()

            if(email.isEmpty() || senha.isEmpty()){
                showMessage("Por favor informe os campos")
                botao.visibility = View.VISIBLE
                progresso.visibility = View.INVISIBLE
            }else{
                loginUser(email,senha)
            }
        }
    }

    private fun loginUser(email: String, senha: String) {

        auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{task ->

            if (task.isSuccessful){
                progresso.visibility = View.INVISIBLE
                botao.visibility = View.VISIBLE
                atualizarUser()
            }else{

                if(task.exception?.message!!.contains("The email address is badly formatted.")){
                    showMessage("Email no formato errado")
                }else if(task.exception?.message!!.contains("There is no user record corresponding")){
                    showMessage("Para se cadastrar clique na imagem")
                }else{
                    showMessage(task.exception?.message.toString())
                }

                botao.visibility = View.VISIBLE
                progresso.visibility = View.INVISIBLE
            }

        }
    }

    private fun atualizarUser() {

        startActivity(intentHomeActivity)
        finish()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()

        var user = auth.currentUser

        if(user != null){
            atualizarUser()
        }
    }
}