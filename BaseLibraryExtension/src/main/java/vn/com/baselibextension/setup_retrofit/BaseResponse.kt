package vn.com.baselibextension.setup_retrofit

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 22/03/2019
 * Time: 12:34 PM
 */
class BaseResponse {
    @SerializedName("data")
    val data: JsonElement? = null
    @SerializedName("messages")
    val message: String = ""
    @SerializedName("code")
    val code: String = ""
    @SerializedName("status")
    val status = 0

    fun data(): String {
        return Objects.toString(data)
    }

    val dataObject: JsonObject
        get() = data!!.asJsonObject

    val dataArray: JsonArray
        get() = data!!.asJsonArray

}