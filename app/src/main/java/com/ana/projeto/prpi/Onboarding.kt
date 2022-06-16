package com.ana.projeto.prpi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Onboarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        var botao = findViewById<Button>(R.id.bt_skip)
        botao.setBackgroundResource(0)

        botao.setOnClickListener{
            var intent = Intent(this, TelaInicial::class.java)
            startActivity(intent)
        }
    }
}