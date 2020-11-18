package vn.com.baselibextension.setup_retrofit

import android.content.Context
import vn.com.baselibextension.R
import vn.com.baselibextension.setup_retrofit.HandleError.FilterError
import vn.com.baselibextension.utils.Constants
import java.util.*

/**
 * Created by giaan on 10/18/20.
 * Company: Intelin
 * Email: antranit95@gmail.com
 */
enum class ErrorType(val code: String) {
    UNKNOW_ERROR_FROM_SERVER("5000"),
    NO_INTERNET("0000"),
    ERROR_FORM_SERVER("500"),
    ERROR_FORM_CLIENT("400"),
    UNKNOW("");

    companion object {
        fun find(code: String): ErrorType {
            for (type in values()) {
                if (type.code == code)
                    return type
            }
            return UNKNOW
        }
    }
}

object HandleError {

    private val codeHashMap: HashMap<String, FilterError> = HashMap()

    fun interface FilterError {
        fun stateError(key: KeyRequest, context: Context): Pair<String, String>
    }
}