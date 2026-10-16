
package com.example.androidfirebase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfirebase.model.Produto;
import com.example.androidfirebase.model.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference referencia;
    private DatabaseReference usuarios;
    private DatabaseReference produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);

        if (firebaseApp == null) {
            Log.e("FIREBASE", "Erro ao inicializar Firebase!");
            return;
        }

        Log.i("FIREBASE", "Firebase inicializado!");

        referencia = FirebaseDatabase.getInstance().getReference();

        usuarios = referencia.child("usuarios");
        produtos = referencia.child("produtos");

        buscarUsuario();

        salvarDados();
        listarUsuarios();
        atualizarNome();
        atualizarUsuario();
        //removerUsuario();
    }

    private void salvarDados() {

        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setSobrenome("Silva");
        usuario.setIdade(45);

        Produto produto = new Produto();
        produto.setDescricao("Acer Aspire");
        produto.setMarca("Acer");
        produto.setPreco(999.99);

        usuarios.child("002").setValue(usuario)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.i("FIREBASE", "Usuário salvo com sucesso!");
                    } else {
                        Log.e("FIREBASE", "Erro ao salvar usuário",
                                task.getException());
                    }
                });

        produtos.child("002").setValue(produto)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Log.i("FIREBASE", "Produto salvo com sucesso!");
                    } else {
                        Log.e("FIREBASE", "Erro ao salvar produto",
                                task.getException());
                    }
                });
    }

    private void listarUsuarios() {

        usuarios.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(
                    @NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Log.i("FIREBASE",
                            String.valueOf(dataSnapshot.getValue()));
                } else {
                    Log.i("FIREBASE", "Nenhum usuário cadastrado!");
                }
            }

            @Override
            public void onCancelled(
                    @NonNull DatabaseError databaseError) {

                Log.e("FIREBASE",
                        "Erro ao listar usuários: "
                                + databaseError.getMessage());
            }
        });
    }

    private void buscarUsuario() {

        usuarios.child("002")
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    Usuario usuario =
                                            snapshot.getValue(Usuario.class);

                                    if (usuario != null) {
                                        Log.i("FIREBASE",
                                                "Usuário encontrado: "
                                                        + usuario.getNome());
                                    }

                                } else {
                                    Log.i("FIREBASE",
                                            "Usuário não encontrado!");
                                }
                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error) {

                                Log.e("FIREBASE",
                                        "Erro ao buscar usuário: "
                                                + error.getMessage());
                            }
                        });
    }

    private void atualizarNome() {

        usuarios.child("002")
                .child("nome")
                .setValue("Maria Atualizada");
    }

    private void atualizarUsuario() {

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Maria Atualizada");
        usuarioAtualizado.setSobrenome("Mendes");
        usuarioAtualizado.setIdade(46);

        usuarios.child("002").setValue(usuarioAtualizado);
    }

    private void removerUsuario() {

        usuarios.child("002").removeValue();
    }
}
