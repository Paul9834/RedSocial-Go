package com.poligran.redsocial.retos.actividades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.poligran.redsocial.retos.repositories.Usuario_interface;
import com.poligran.redsocial.retos.R;
import com.poligran.redsocial.retos.entities.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Usuario_interface usuariointerface = retrofit.create(Usuario_interface.class);

        Call<List<Usuario>> call = usuariointerface.getPosts();

        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Usuario> usuarios = response.body();

                for (Usuario usuario : usuarios) {
                    String content = "";

                    content += "ID: " + usuario.getId() + "\n";
                    content += "User ID: " + usuario.getUserId() + "\n";
                    content += "Title: " + usuario.getTitle() + "\n";
                    content += "Text: " + usuario.getText() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
