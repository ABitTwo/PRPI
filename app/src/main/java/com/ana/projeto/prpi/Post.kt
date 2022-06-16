package com.ana.projeto.prpi

import com.google.firebase.firestore.ServerTimestamp
import java.sql.Date


data class Post(
    var title : String? = "",
    var descricao: String? = "",
    var foto: String? = "",
    var userid : String? = "",
    var userFoto : String? = "",
    var postKey : String? = null,
    var name : String? = ""
)
