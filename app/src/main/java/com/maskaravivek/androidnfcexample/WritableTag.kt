package com.maskaravivek.androidnfcexample

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import java.io.IOException
import java.util.*

class WritableTag @Throws(FormatException::class) constructor(tag: Tag) {
    private val NDEF = Ndef::class.java.canonicalName
    private val NDEF_FORMATABLE = NdefFormatable::class.java.canonicalName

    private val ndef: Ndef?
    private val ndefFormatable: NdefFormatable?

    val tagId: String?
        get() {
            if (ndef != null) {
                return bytesToHexString(ndef.tag.id)
            } else if (ndefFormatable != null) {
                return bytesToHexString(ndefFormatable.tag.id)
            }
            return null
        }

    init {
        val technologies = tag.techList
        val tagTechs = Arrays.asList(*technologies)
        if (tagTechs.contains(NDEF)) {
            Log.i("WritableTag", "contains ndef")
            ndef = Ndef.get(tag)
            ndefFormatable = null
        } else if (tagTechs.contains(NDEF_FORMATABLE)) {
            Log.i("WritableTag", "contains ndef_formatable")
            ndefFormatable = NdefFormatable.get(tag)
            ndef = null
        } else {
            throw FormatException("Tag doesn't support ndef")
        }
    }

    @Throws(IOException::class, FormatException::class)
    fun writeData(tagId: String,
                  message: NdefMessage): Boolean {
        if (tagId != tagId) {
            return false
        }
        if (ndef != null) {
            ndef.connect()
            if (ndef.isConnected) {
                ndef.writeNdefMessage(message)
                return true
            }
        } else if (ndefFormatable != null) {
            ndefFormatable.connect()
            if (ndefFormatable.isConnected) {
                ndefFormatable.format(message)
                return true
            }
        }
        return false
    }

    @Throws(IOException::class)
    private fun close() {
        ndef?.close() ?: ndefFormatable?.close()
    }

    companion object {
        fun bytesToHexString(src: ByteArray): String? {
            if (ByteUtils.isNullOrEmpty(src)) {
                return null
            }
            val sb = StringBuilder()
            for (b in src) {
                sb.append(String.format("%02X", b))
            }
            return sb.toString()
        }
    }
}