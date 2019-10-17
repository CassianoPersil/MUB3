package com.projetos.mub.roomDatabase.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    @NonNull()
    private Long id;
    private String nome;
    private String email;
    private boolean agente;
    private boolean manterLogado;


    public Usuario(){

    }

    public Usuario(Long id, String nome, String email, boolean agente, boolean manterLogado) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.agente = agente;
        this.manterLogado = manterLogado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAgente() {
        return agente;
    }

    public void setAgente(boolean agente) {
        this.agente = agente;
    }

    public boolean isManterLogado() {
        return manterLogado;
    }

    public void setManterLogado(boolean manterLogado) {
        this.manterLogado = manterLogado;
    }
}
