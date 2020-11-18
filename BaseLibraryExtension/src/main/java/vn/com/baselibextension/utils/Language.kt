package vn.com.baselibextension.utils

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 * Date: 08/12/2018
 * Time: 2:24 PM
 */
enum class Language(val code: String, val res: String, val send: String) {
    VIETNAM("vi", "VN", "VI"),
    ENGLISH("en", "EN", "EN");

    companion object {
        fun find(filter: String): Language {
            for (ci in values()) {
                if (ci.code == filter) {
                    return ci
                }
            }
            return VIETNAM
        }
    }
}