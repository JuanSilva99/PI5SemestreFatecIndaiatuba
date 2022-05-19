package com.example.loginpi5s;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Transacao implements Serializable {

    //declaração de atributos
    private String tipo;
    private String categoria;
    private String valor;
    private String data;
    private String observacao;

    //construtores
    public Transacao(String categoria, String valor, String data, String observacao) {
        this.tipo = "";
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.observacao = observacao;
    }

    public Transacao(String tipo, String categoria, String valor, String data, String observacao) {
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.observacao = observacao;
    }

    //getters e setters
    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void save(String tipo, String uId, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if (key.equals("")) {
            reference.child("Usuarios").child(uId).child(tipo).push().setValue(this);
        }
        else {
            reference.child("Usuarios").child(uId).child(tipo).child(key).child("valor").setValue(valor);
            reference.child("Usuarios").child(uId).child(tipo).child(key).child("data").setValue(data);
            reference.child("Usuarios").child(uId).child(tipo).child(key).child("observacao").setValue(observacao);
            reference.child("Usuarios").child(uId).child(tipo).child(key).child("categoria").setValue(categoria);
        }
    }
}
