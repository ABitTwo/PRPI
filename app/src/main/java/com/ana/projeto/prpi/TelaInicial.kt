package com.ana.projeto.prpi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.ana.projeto.prpi.databinding.ActivityTelaInicialBinding

class TelaInicial : AppCompatActivity() {

    lateinit var binding: ActivityTelaInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarSpinner()

        binding.button.setOnClickListener{
            if(binding.spinner.selectedItemPosition != 0){
                 abrirLista()
            }else{
                alert("Erro", "Selecione uma Região", this)
            }
        }

    }

    private fun abrirLista() {
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("spinner", binding.spinner.selectedItemPosition.toString())
        startActivity(intent)
    }

    private fun alert(titulo: String, msg: String, ctx: Context) {

        AlertDialog.Builder(ctx).setTitle(titulo).setMessage(msg)
            .setPositiveButton("ok",null)
            .create()
            .show()
    }

    private fun carregarSpinner() {

        var adapter = ArrayAdapter.createFromResource(
            this,
            R.array.regioes_Arrays,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        )
        binding.spinner.adapter = adapter

    }
}