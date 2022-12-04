package com.example.nmedia.api

import com.example.nmedia.BuildConfig
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.dto.Media
import com.example.nmedia.dto.Post
import com.example.nmedia.dto.PushToken
import com.example.nmedia.dto.Token
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = BuildConfig.BASE_URL

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val authInterceptor =  Interceptor{ chain->
    val request = AppAuth.getInstance().data.value?.token?.let {
        chain.request()
            .newBuilder()
            .addHeader("Authorization", it)
            .build()
    } ?: chain.request()

    chain.proceed(request)
}

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor(authInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @POST("users/push-tokens")
    suspend fun sendPushToken(@Body token: PushToken): Response<Unit>

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>

    @Multipart
    @POST("media")
    suspend fun uploadPic(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST ("users/authentication")
    suspend fun updateUser(@Field("login") login: String, @Field("pass") pass:String): Response<Token>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(@Field("login") login: String, @Field("pass") pass: String, @Field("name") name: String): Response<Token>
}


object Api {
    val service: ApiService by lazy{ retrofit.create(ApiService::class.java)}
}