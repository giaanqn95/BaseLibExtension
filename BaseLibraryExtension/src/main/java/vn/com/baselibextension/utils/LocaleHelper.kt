@file:Suppress("DEPRECATION")

package vn.com.baselibextension.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.*

/**
 * Copyright by Intelin.
 * Creator: Tran Do Gia An
 */
object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    fun onAttach(context: Context): Context? {
        val lang = getPersistedData(
            context,
            Language.VIETNAM.code
        )
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(
            context,
            Language.VIETNAM.code
        )
    }

    fun setLocale(context: Context, language: String?): Context? {
        language ?: return null
        persist(context, language)
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(
            context,
            language
        )
    }

    private fun updateLocale(context: Context): Context? {
        return setLocale(
            context,
            getLanguage(context)
        )
    }

    fun getResourcesByContext(context: Context): Resources? {
        return updateLocale(context)?.resources
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        return try {
            val preferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            preferences.getString(
                SELECTED_LANGUAGE,
                defaultLanguage
            )
        } catch (e: Exception) {
            defaultLanguage
        }
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String?): Context? {
        language ?: return null
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration =
            context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        context.resources
            .updateConfiguration(configuration, context.resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String?): Context? {
        language ?: return null
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}