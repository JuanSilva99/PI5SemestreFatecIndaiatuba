package com.example.loginpi5s;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper{

    //declaração de variáveis
    private static final String DB_NAME = "pi";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME_1 = "user";
    private static final String TABLE_NAME_2 = "transacoes";
    private static final String ID_COL_1 = "id_user";
    private static final String ID_COL_2 = "id_transacao";
    private static final String TIPO_COL = "tipo";
    private static final String NAME_COL = "nome";
    private static final String EMAIL_COL = "email";
    private static final String PSWRD_COL = "senha";
    private static final String CATEG_COL = "categoria";
    private static final String VALOR_COL = "valor";
    private static final String DATA_COL = "data";
    private static final String OBS_COL = "observacao";

    //construtor
    public Conexao(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //cria a tabela de usuários
        db.execSQL("CREATE TABLE " + TABLE_NAME_1 + " ("
                + ID_COL_1 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " VARCHAR(50) NOT NULL,"
                + EMAIL_COL + " VARCHAR(20) NOT NULL UNIQUE,"
                + PSWRD_COL + " VARCHAR(10) NOT NULL)"
        );

        //cria a tabela de transações
        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + " ("
                + ID_COL_2 + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
                + ID_COL_1 + " INTEGER NOT NULL,"
                + TIPO_COL + " VARCHAR(7) NOT NULL,"
                + CATEG_COL + " TEXT NOT NULL,"
                + VALOR_COL + " REAL NOT NULL,"
                + DATA_COL + " DATETIME NOT NULL,"
                + OBS_COL + " TEXT,"
                + "FOREIGN KEY (" + ID_COL_1 + ") REFERENCES " + TABLE_NAME_1 + " (" + ID_COL_1 + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //checa se as tabelas já existem
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }
}