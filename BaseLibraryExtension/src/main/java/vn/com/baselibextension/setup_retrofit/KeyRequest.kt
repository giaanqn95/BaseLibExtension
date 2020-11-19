package vn.com.baselibextension.setup_retrofit

import vn.com.baselibextension.utils.Constants.BUILD_ENVIRONMENT
import java.util.*

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 */
enum class KeyRequest(val url: String) {

    PUT_IDENTITY("pin"),
    LOGIN("auth"),
    SUBMIT_OTP("user/detail"),
    CHANGE_PIN("pin"),
    OUT_APP("pin"),
    PROCESS_TRANSACTION("process_transaction"),
    CREATE_IDENTITY_LOGIN("create_identity"),
    CREATE_IDENTITY_QR("create_identity_qr"),
    UNKNOW("");

    fun collationId(): String {
        return UUID.randomUUID().toString()
    }

    companion object {
        fun find(key: KeyRequest): KeyRequest {
            for (type in values()) {
                if (type == key)
                    return type
            }
            return UNKNOW
        }

        fun createURL(key: KeyRequest): String {
            for (type in values()) {
                if (type == key)
                    return "$BUILD_ENVIRONMENT/${type.url}"
            }
            return BUILD_ENVIRONMENT
        }
    }
}