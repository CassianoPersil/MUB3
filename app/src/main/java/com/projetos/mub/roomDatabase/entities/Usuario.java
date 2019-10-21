package com.projetos.mub.roomDatabase.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    @NonNull()
    private Long id;
    private Long idUsuarioAPI;
    private String nome;
    private String email;
    private boolean manterLogado;
    private int nvAcesso;

    public Usuario(){

    }

    public Usuario(Long id, Long idUsuarioAPI, String nome, String email, int nvAcesso, boolean manterLogado) {
        this.id = id;
        this.idUsuarioAPI = idUsuarioAPI;
        this.nome = nome;
        this.email = email;
        this.nvAcesso = nvAcesso;
        this.manterLogado = manterLogado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuarioAPI() {
        return idUsuarioAPI;
    }

    public void setIdUsuarioAPI(Long idUsuarioAPI) {
        this.idUsuarioAPI = idUsuarioAPI;
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

    public int getNvAcesso() {
        return nvAcesso;
    }

    public void setNvAcesso(int nvAcesso) {
        this.nvAcesso = nvAcesso;
    }

    public boolean isManterLogado() {
        return manterLogado;
    }

    public void setManterLogado(boolean manterLogado) {
        this.manterLogado = manterLogado;
    }
}
