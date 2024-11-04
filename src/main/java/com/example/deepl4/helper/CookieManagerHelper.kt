package com.example.deepl4.helper

import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView

class CookieManagerHelper {
    companion object {
        private fun getCookieManager(): CookieManager {
            return CookieManager.getInstance()
        }
    }

    fun saveCookies(context: Context, webView: WebView) {
        val cookieManager = getCookieManager()
        val isCookieAutoDeleted = false

        if (isCookieAutoDeleted) {
            clearCookies()
        } else {
            cookieManager.acceptCookie()
            cookieManager.setAcceptThirdPartyCookies(webView, true)
            cookieManager.flush()
        }
    }


    // Disable cookie values in SharedPreferences according to the bug fix for cookie expiration in v8.5
    fun migrateCookie(context: Context) {
        val sharedPreferences = context.getSharedPreferences("DeepLCookies", Context.MODE_PRIVATE)
        val savedCookie = sharedPreferences.getString("cookie", null)

        if (savedCookie != null) {
            clearCookies()
            sharedPreferences.edit().clear().apply()
        }
    }

    private fun clearCookies() {
        getCookieManager().removeAllCookies { success ->
            if (success) {
                Log.d("CookieManagerHelper", "Cookies successfully removed.")
            } else {
                Log.e("CookieManagerHelper", "Failed to remove cookies.")
            }
        }
    }
}