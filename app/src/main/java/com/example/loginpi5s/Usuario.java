package com.example.loginpi5s;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    //declaração de atributos
    private String nome;
    private String email;

    //construtor
    public Usuario(String nome, String email){
        this.nome = nome;
        this.email = email;
    }

    //getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void save(String uId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios").child(uId).setValue(this);
    }
}