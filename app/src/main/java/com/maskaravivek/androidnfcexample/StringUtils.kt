package com.maskaravivek.androidnfcexample

class StringUtils {
    companion object {
        fun randomString(length: Int): String {
            val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            var str = ""
            for (i in 0..length) {
                str += chars[Math.floor(Math.random() * chars.length).toInt()]
            }
            return str
        }
    }
}
