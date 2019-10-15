package com.projetos.mub.roomDatabase.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.projetos.mub.roomDatabase.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDAO {
    @Query("SELECT * FROM usuario WHERE id=:id")
    Usuario getUserById(Long id);

    @Query("SELECT * FROM usuario")
    List<Usuario> getAllUsuarios();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insert(Usuario... usuarios);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Usuario usuario);

    @Update
    void update(Usuario... usuarios);

    @Delete
    void delete(Usuario... usuarios);
}
