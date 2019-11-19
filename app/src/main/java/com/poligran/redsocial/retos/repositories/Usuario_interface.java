package com.poligran.redsocial.retos.repositories;
import com.poligran.redsocial.retos.entities.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Usuario_interface {

    @GET("posts")
    Call<List<Usuario>> getPosts();
}