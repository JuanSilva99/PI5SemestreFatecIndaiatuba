package com.example.loginpi5s;

import java.io.Serializable;

public class Transacao implements Serializable {

    //declaração de atributos
    private String id;
    private String id_user;
    private String tipo;
    private String categoria;
    private String valor;
    private String data;
    private String observacao;

    //construtores
    public Transacao(){

    }

    public Transacao(String id, String id_user, String tipo, String categoria, String valor, String data, String observacao) {
        this.id = id;
        this.id_user = id_user;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.observacao = observacao;
    }

    public Transacao(String id, String categoria, String valor, String data, String observacao) {
        this.id = id;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
        this.observacao = observacao;
    }

    //getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
}
