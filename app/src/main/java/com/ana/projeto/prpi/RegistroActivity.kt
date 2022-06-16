package com.ana.projeto.prpi

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ana.projeto.prpi.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    lateinit var userFoto : ImageView
    var imageUri: Uri? = null
    lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var botao = binding.rgButton
        var progresso = binding.progressBar2
        progresso.visibility = View.INVISIBLE



        auth = FirebaseAuth.getInstance()

        binding.rgButton.setOnClickListener{

            botao.visibility = View.INVISIBLE
            progresso.visibility = View.VISIBLE

            var email = binding.editEmail.text.toString()
            var senha = binding.editSenha.text.toString()
            var senhaDois = binding.editSenha2.text.toString()
            var nome = binding.editName.text.toString()

            if(email.isEmpty() || senha.isEmpty() || senha.length < 6 || !senhaDois.equals(senha) || nome.isEmpty()){
                if(senha.length < 6){
                    showMessage("Senha não pode ser menor que 6 digitos")
                }else{
                    showMessage("Por favor verifique os campos")
                }

                botao.visibility = View.VISIBLE
                progresso.visibility = View.INVISIBLE
            }else{

                criarUsuario(email,nome,senha)
            }

        }

        userFoto = binding.rgImage

        userFoto.setOnClickListener(){

            if(Build.VERSION.SDK_INT >= 22){

                checarsolicitarPermissao()
            }else{
                abrirGaleria()
            }

        }

    }

    private fun showMessage(message : String) {

        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    private fun criarUsuario(email: String, nome: String, senha: String) {

        auth.createUserWithEmailAndPassword(email,senha)
            .addOnCompleteListener{task ->


                if(task.isSuccessful){
                    showMessage("Conta Criada")

                    if(imageUri != null) {
                        atualizarUsuario(nome, imageUri!!, auth.currentUser)
                    }else{
                        atualizarUsuarioSemFoto(nome,auth.currentUser)
                    }
                }

            }.addOnFailureListener{ exception->
            exception.message
            showMessage("Conta não foi criada")

        }


    }

    private fun atualizarUsuario(nome: String, imageUri: Uri, currentUser: FirebaseUser?) {

        var storage = FirebaseStorage.getInstance().getReference().child("users_photos")
        var imagePath = storage.child(imageUri.lastPathSegment!!)
        imagePath.putFile(imageUri).addOnSuccessListener {
            imagePath.downloadUrl.addOnSuccessListener {

                var profile = UserProfileChangeRequest.Builder().setDisplayName(nome).setPhotoUri(it).build()

                currentUser!!.updateProfile(profile).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        showMessage("Registro Completo")
                        atualizarUI()
                    }
                }
            }
        }


    }

    private fun atualizarUI() {

        val intent = Intent(this, Home2Activity::class.java)
        startActivity(intent)
        finish()

    }

    private fun abrirGaleria(){

        val galeria = Intent(Intent.ACTION_GET_CONTENT)
        galeria.setType("image/*")
        startActivityForResult(galeria,1)

    }

    private fun atualizarUsuarioSemFoto(nome: String, currentUser: FirebaseUser?) {

        var profile = UserProfileChangeRequest.Builder().setDisplayName(nome).build()

        currentUser!!.updateProfile(profile).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                showMessage("Registro Completo")
                atualizarUI()
            }
        }
    }


    private fun checarsolicitarPermissao() {

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"Por favor aceite a permissao", Toast.LENGTH_LONG).show()
            }else{

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
        }else {
            abrirGaleria()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 1 && data != null)
            imageUri = data.data!!
            userFoto.setImageURI(imageUri)
    }



}