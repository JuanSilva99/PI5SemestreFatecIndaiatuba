package com.example.loginpi5s.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.loginpi5s.Conexao;
import com.example.loginpi5s.Transacao;

import java.util.ArrayList;

public class TransacaoDAO {

    //declaração de variáveis
    private final SQLiteDatabase DB;
    private static final String TABLE_NAME = "transacoes";
    private static final String ID_COL = "id_transacao";
    private static final String FK_COL = "id_user";
    private static final String TIPO_COL = "tipo";
    private static final String CAT_COL = "categoria";
    private static final String VALOR_COL = "valor";
    private static final String DATA_COL = "data";
    private static final String OBS_COL = "observacao";

    public TransacaoDAO(Context context){
        Conexao conexao = new Conexao(context);
        DB = conexao.getWritableDatabase();
    }

    //insere uma nova transação
    public Boolean inserir(Transacao transacao){
        ContentValues values = new ContentValues();

        //coleta todos os dados da transação
        values.put(FK_COL, transacao.getId_user());
        values.put(TIPO_COL, transacao.getTipo());
        values.put(CAT_COL, transacao.getCategoria());
        values.put(VALOR_COL, transacao.getValor());
        values.put(DATA_COL, transacao.getData());
        values.put(OBS_COL, transacao.getObservacao());

        //grava no banco de dados e retorna o ID
        long result = DB.insert(TABLE_NAME, null, values);

        //se o ID retornado for maior de zero, foi um sucesso
        return result > 0;
    }

    //edita uma transação
    public Boolean atualizar(Transacao transacao) {
        ContentValues values = new ContentValues();

        //coleta todos os dados relevantes da transação
        values.put(CAT_COL, transacao.getCategoria());
        values.put(VALOR_COL, transacao.getValor());
        values.put(DATA_COL, transacao.getData());
        values.put(OBS_COL, transacao.getObservacao());

        //atualiza no banco de dados
        int result = DB.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(transacao.getId())});

        //se retornou maior que zero, a atualização foi um sucesso
        return result > 0;
    }

    //apaga uma transação
    public Boolean deleteTransacao(Integer id) {
        //faz o delete no banco de dados
        int result = DB.delete(TABLE_NAME, ID_COL + " = ?", new String[]{ String.valueOf(id) });

        //se retornou maior que zero, a transação foi apagada com sucesso
        return result > 0;
    }

    //recupera todas as transações
    public ArrayList<Transacao> getAllHistorico(Integer id){
        ArrayList<Transacao> registro = new ArrayList<>();
        String[] id_user = {id.toString()};
        String[] colunas = {"*"};
        Cursor cursor = DB.query(TABLE_NAME, colunas, FK_COL + " = ?", id_user, null, null, "data DESC");

        if(cursor.moveToFirst())
        {
            do{
                Integer id2 = cursor.getInt(0);
                Integer id_user2 = cursor.getInt(1);
                String tipo = cursor.getString(2);
                String categoria = cursor.getString(3);
                String valor = cursor.getString(4);
                String data = cursor.getString(5);
                String obs = cursor.getString(6);

                Transacao s = new Transacao(id2, id_user2, tipo, categoria, valor, data, obs);
                registro.add(s);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return registro;
    }
}
