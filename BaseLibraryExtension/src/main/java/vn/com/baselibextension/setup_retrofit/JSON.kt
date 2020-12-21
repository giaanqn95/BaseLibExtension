package vn.com.baselibextension.setup_retrofit

import com.google.gson.GsonBuilder
import org.json.JSONObject
import vn.com.baselibextension.utils.LogCat

/**
 * Created by giaan on 11/25/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
object JSON {
    private val parseGson = GsonBuilder().disableHtmlEscaping().create()

    @Throws(Exception::class)
    fun encode(obj: Any?): String {
        return try {
            parseGson.toJson(obj)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }


    fun <T> decodeToList(jsonElement: String?, clazz: Class<Array<T>>): List<T> {
        return try {
            val array: Array<T> = parseGson.fromJson(jsonElement, clazz)
            array.toList()
        } catch (e: Exception) {
            ArrayList()
        }
    }


    fun <T> decode(json: String?, tClass: Class<T>?): T? {
        return try {
            parseGson.fromJson(json, tClass)
        } catch (e: Exception) {
            LogCat.e(e.message)
            null
        }
    }

    fun evertStringToJSON(json: String?): JSONObject? {
        return try {
            JSONObject(json)
        } catch (e: Exception) {
            LogCat.e(e.message)
            null
        }
    }
}