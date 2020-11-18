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
    suspend fun get(@HeaderMap headers: Map<String, String>, @Url url: String): BaseResponse

    @POST
    suspend fun post(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): BaseResponse

    @PUT
    suspend fun put(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): BaseResponse

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun delete(@HeaderMap headers: Map<String, String>, @Url url: String, @Body o: Any?): BaseResponse

    @Multipart
    @POST
    suspend fun uploadFile(@Url url: String, @Part file: MultipartBody.Part?): BaseResponse
}