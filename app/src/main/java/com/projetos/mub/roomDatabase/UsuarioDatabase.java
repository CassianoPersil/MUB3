package com.projetos.mub.roomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.projetos.mub.roomDatabase.DAOs.UsuarioDAO;
import com.projetos.mub.roomDatabase.entities.Usuario;

@Database(entities = {Usuario.class}, version = 2, exportSchema = false)
public abstract class UsuarioDatabase extends RoomDatabase {

    private static final String DB_NAME = "usuarioV1.db";
    private static volatile UsuarioDatabase instace;

    public static synchronized UsuarioDatabase getInstance(Context context){
        if(instace == null){
            instace = create(context);
        }
        return instace;
    }

    private static UsuarioDatabase create(final Context context){
        return Room.databaseBuilder(
                context.getApplicationContext(),
                UsuarioDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public abstract UsuarioDAO getUsuarioDAO();
}
