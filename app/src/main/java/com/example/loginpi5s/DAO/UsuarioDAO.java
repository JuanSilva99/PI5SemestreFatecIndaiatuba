package com.example.loginpi5s.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.loginpi5s.Conexao;
import com.example.loginpi5s.Usuario;

public class UsuarioDAO {

    //declaração de variáveis
    private final SQLiteDatabase DB;
    private static final String TABLE_NAME = "user";
    private static final String ID_COL = "id_user";
    private static final String NAME_COL = "nome";
    private static final String EMAIL_COL = "email";
    private static final String PSWRD_COL = "senha";

    public UsuarioDAO(Context context){
        Conexao conexao = new Conexao(context);
        DB = conexao.getWritableDatabase();
    }

    //insere um novo usuário
    public Boolean inserir(Usuario user){
        ContentValues values = new ContentValues();

        //coleta todos os dados da transação
        values.put(NAME_COL, user.getNome());
        values.put(EMAIL_COL, user.getEmail());
        values.put(PSWRD_COL, user.getSenha());

        //grava no banco de dados e retorna o ID
        long result = DB.insert(TABLE_NAME, null, values);

        //se o ID retornado for maior de zero, foi um sucesso
        return result > 0;
    }

    //checa se o usuário existe no banco através do email
    public Boolean checkEmail(String emailUsuario){
        //procura o email no banco
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME
                        + " WHERE email = ?", new String[] {emailUsuario}
        );

        //se retornou maior que zero, o usuário existe
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    //checa se o login é permitido se ouver um usuário com tal email e senha
    public Boolean checkLogin(String emailUsuario, String senhaUsuario) {
        //procura um usuário com tal email e senha
        Cursor cursor = DB.rawQuery("SELECT * FROM " + TABLE_NAME
                        + " WHERE " + EMAIL_COL + " = ? and " + PSWRD_COL +" = ?",
                        new String[] {emailUsuario,senhaUsuario}
        );

        //se retornou maior que zero, um usuário com essa senha existe
        if (cursor.getCount() > 0){
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    //procura qual o ID do usuário com tal email
    public int recuperarId(String emailUsuario){
        //procura o email no banco e retorna o ID
        String result = DatabaseUtils.stringForQuery(DB,
                "SELECT "+ID_COL+" FROM "+TABLE_NAME+" WHERE "+EMAIL_COL+" = ?", new String[] {emailUsuario});

        return Integer.parseInt(result);
    }

    //procura qual o nome do usuário com tal email
    public String recuperarNome(String emailUsuario){
        //procura o email no banco e retorna o nome
        return DatabaseUtils.stringForQuery(DB,
                "SELECT "+NAME_COL+" FROM "+TABLE_NAME+" WHERE "+EMAIL_COL+" = ?", new String[] {emailUsuario});
    }

    //procura qual a senha do usuário com tal email
    public String recuperarSenha(String emailUsuario){
        //procura o email no banco e retorna a senha
        return DatabaseUtils.stringForQuery(DB,
                "SELECT "+PSWRD_COL+" FROM "+TABLE_NAME+" WHERE "+EMAIL_COL+" = ?", new String[] {emailUsuario});
    }

    //altera o nome do usuário no banco
    public Boolean mudarNome(String emailUsuario, String nome){
        ContentValues values = new ContentValues();
        values.put(NAME_COL, nome);

        //atualiza o nome e retorna sucesso ou falha
        int result = DB.update(TABLE_NAME, values, EMAIL_COL + " = ?", new String[] {emailUsuario});

        //se sucesso, ele terá retornado um numero maior que zero
        return result > 0;
    }

    //altera a senha do usuário no banco
    public Boolean mudarSenha(String emailUsuario, String senha){
        ContentValues values = new ContentValues();
        values.put(PSWRD_COL, senha);

        //atualiza a senha e retorna sucesso ou falha
        int result = DB.update(TABLE_NAME, values, EMAIL_COL + " = ?", new String[] {emailUsuario});

        //se sucesso, ele terá retornado um numero maior que zero
        return result > 0;
    }
}
