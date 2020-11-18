package vn.com.baselibextension.setup_retrofit

enum class ResponseFilter(val code: String, val description: String) {

    UNKNOWN("UNKNOWN", "");

    companion object {
        fun getResponse(code: String): ResponseFilter {
            for (result in values()) {
                if (result.code == code)
                    return result
            }
            return UNKNOWN
        }
    }
}