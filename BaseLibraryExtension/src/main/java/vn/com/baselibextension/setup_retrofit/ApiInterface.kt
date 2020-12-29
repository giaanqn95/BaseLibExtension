package vn.com.baselibextension.setup_retrofit

import okhttp3.MultipartBody
import retrofit2.http.*


/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 10:33 AM
 */
interface ApiInterface {
    @GET
    suspend fun <T> get(@HeaderMap headers: Map<String, String>, @Url url: String): T

    @POST
    suspend fun <T> post(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): T

    @PUT
    suspend fun <T> put(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): T

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun <T> delete(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): T

    @Multipart
    @POST
    suspend fun <T> uploadFile(@HeaderMap headers: Map<String, String>, @Url url: String, @Part file: MultipartBody.Part?): T
}