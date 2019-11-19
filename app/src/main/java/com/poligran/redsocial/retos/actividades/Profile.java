package com.poligran.redsocial.retos.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poligran.redsocial.retos.R;
import com.poligran.redsocial.retos.entities.Usuario;
import com.poligran.redsocial.retos.repositories.Usuario_interface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    TextView IdNick;
    ImageView Photo;
    TextView Name;
    TextView Last_Name;
    TextView Description;
    TextView Gender;
    TextView Location;
    ImageView PhotoLed;
    //Boolean Locations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUserAPI();

    }
    public void getUserAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.paul9834.engineer/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Usuario_interface usuariointerface = retrofit.create(Usuario_interface.class);
        Call<List<Usuario>> call = usuariointerface.getPosts();


        // Crear los atributos que iran en los campos //


        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> usuarios = response.body();

               int id=0;
               String name="";
               String last_name="";
               String description="";
               String on_location="";
               int gender_id=0;
               String urlpicture = "";

                for (Usuario usuario : usuarios) {
                    String content = "";
                    //id
                    id =  usuario.getId();
                    Log.e("id", Integer.toString(id));
                   //name //
                    name = usuario.getName();
                    Log.e("name",name);
                    //Last Name//
                    last_name = usuario.getLast_name();
                    Log.e("last name",last_name);
                    //Description
                    description = usuario.getDescription();
                    Log.e("Description",description);
                    //Location
                    //Genero
                    gender_id = usuario.getGender_id();
                    Log.e("Gender", String.valueOf(gender_id));
                    //url Photo
                    urlpicture = usuario.getUrlprofile();
                    Log.e("Imagen(url)",urlpicture);

                    on_location=usuario.getOn_location();
                    Log.e("Location Type",on_location);




                }

                IdNick = findViewById(R.id.Id_Name_Profile);
                Photo = findViewById(R.id.Id_Image_Profile);
                Name = findViewById(R.id.Id_Name);
                Last_Name = findViewById(R.id.Id_Last_Name);
                Description = findViewById(R.id.Id_Description);
                Gender = findViewById(R.id.Id_Genero);
                Location = findViewById(R.id.Id_Location);
                PhotoLed = findViewById(R.id.Id_location_Image);


                IdNick.setText(id + "Este es el ID");
                Name.setText(name);
                Last_Name.setText((last_name));
                Description.setText(description);
                Location.setText(String.valueOf(on_location));
                Gender.setText(gender_id+ "Genero");
                Location.setText(on_location);




            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
            }
        });
    }
}


