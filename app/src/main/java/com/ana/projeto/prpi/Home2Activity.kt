package com.ana.projeto.prpi

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.ana.projeto.prpi.databinding.ActivityHome2Binding
import com.ana.projeto.prpi.fragments.ConfiguracaoFragment
import com.ana.projeto.prpi.fragments.HomeFragment
import com.ana.projeto.prpi.fragments.ProfileFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Home2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var auth : FirebaseAuth
    lateinit var user : FirebaseUser
    lateinit var drawerLayout : DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHome2Binding
    lateinit var dialog : Dialog
    lateinit var popImageUser : ImageView
    lateinit var popImagePost : ImageView
    lateinit var popButonPost : ImageView
    lateinit var textTitle : TextView
    lateinit var textDescrip : TextView
    lateinit var progreBar : ProgressBar
    var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome2.toolbar)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        iniPop()
        configurarPopImage()

        binding.appBarHome2.fab.setOnClickListener { view ->
            dialog.show()
        }
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_perfil, R.id.nav_tools, R.id.nav_sair
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        updateNavHeader()

        binding.navView.setNavigationItemSelectedListener(this)
        trocarFragmento(HomeFragment())

        supportFragmentManager.beginTransaction().replace(R.id.fragment, HomeFragment()).commit()


    }

    private fun configurarPopImage() {

        popImagePost.setOnClickListener{

            checarsolicitarPermissao()
        }
    }

    private fun checarsolicitarPermissao() {

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this,"Por favor aceite a permissao", Toast.LENGTH_LONG).show()
            }else{

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2)
            }
        }else {
            abrirGaleria()
        }


    }

    private fun abrirGaleria(){

        val galeria = Intent(Intent.ACTION_GET_CONTENT)
        galeria.setType("image/*")
        startActivityForResult(galeria,2)

    }

    private fun iniPop() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.pop_add_post)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.gravity = Gravity.TOP

        popImageUser = dialog.findViewById(R.id.pop_imageuser)
        popImagePost = dialog.findViewById(R.id.pop_image)
        textTitle = dialog.findViewById(R.id.pop_title)
        textDescrip = dialog.findViewById(R.id.pop_descricao)
        popButonPost = dialog.findViewById(R.id.pop_Edit)
        progreBar = dialog.findViewById(R.id.pop_progres)

        if(user.photoUrl != null){
            Glide.with(this).load(user.photoUrl).into(popImageUser)
        }else{
            Glide.with(this).load(R.drawable.userphoto).into(popImageUser)
        }


        popButonPost.setOnClickListener{
            popButonPost.visibility = View.INVISIBLE
            progreBar.visibility = View.VISIBLE

            if(!textTitle.text.toString().isEmpty() &&
                !textDescrip.text.toString().isEmpty() && imageUri != null){

                var storage = FirebaseStorage.getInstance().getReference().child("blog_images")
                var imageFile = storage.child(imageUri!!.lastPathSegment.toString())
                imageFile.putFile(imageUri!!).addOnSuccessListener { task ->
                    imageFile.downloadUrl.addOnSuccessListener { uri ->
                        var imageLink = uri.toString()

                        if(user.photoUrl !=null){
                            var post = Post(textTitle.text.toString(), textDescrip.text.toString(),imageLink
                                ,user.uid,user.photoUrl.toString())

                            addPost(post)
                        }else{
                            var post = Post(textTitle.text.toString(), textDescrip.text.toString(),imageLink
                                ,user.uid,null)

                            addPost(post)
                        }


                    }
                }.addOnFailureListener{ exception ->
                    showMessage(exception.message.toString())
                    popButonPost.visibility = View.VISIBLE
                    progreBar.visibility = View.INVISIBLE
                }

            }else{
                showMessage("Por favor verifique os campos")
                popButonPost.visibility = View.VISIBLE
                progreBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun addPost(post: Post) {

        var fireBase = FirebaseDatabase.getInstance()
        var newNode = fireBase.reference.child("post").push()
        post.postKey = newNode.key

        newNode.setValue(post).addOnSuccessListener {
            showMessage("Post Adicionado")
            popButonPost.visibility = View.VISIBLE
            progreBar.visibility = View.INVISIBLE
            dialog.dismiss()

            textTitle.setText("")
            textDescrip.setText("")
            popImagePost.setImageResource(0)

        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home2)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun updateNavHeader(){
        var navView: NavigationView = binding.navView
        var header = navView.getHeaderView(0)
        var userName : TextView = header.findViewById(R.id.nav_user_name)
        var userEmail : TextView = header.findViewById(R.id.nav_user_email)
        var imageUser : ImageView = header.findViewById(R.id.nav_user_photo)

        userName.setText(user.displayName)
        userEmail.setText(user.email)

        if(user.photoUrl != null){
            Glide.with(this).load(user.photoUrl).into(imageUser)
        }else{
            Glide.with(this).load(R.drawable.userphoto).into(imageUser)
        }



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawerLayout.closeDrawer(GravityCompat.START)

        when(item.itemId){
            R.id.nav_home ->{
                setTitle("Home")
                trocarFragmento(HomeFragment())
            }
            R.id.nav_perfil ->{
                setTitle("Perfil")
                trocarFragmento(ProfileFragment())
            }
            R.id.nav_tools ->{
                setTitle("Configuração")
                trocarFragmento(ConfiguracaoFragment())
            }
            R.id.nav_sair ->{
                FirebaseAuth.getInstance().signOut()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        return true
    }

    fun setTitle(title : String){
        supportActionBar?.title = title
    }

    fun trocarFragmento(frag : Fragment){
        var fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment,frag).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == 2 && data != null)
            imageUri = data.data!!
            popImagePost.setImageURI(imageUri)
    }

}