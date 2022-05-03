package ca.doophie.passwordpopper.crypto

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncrypt {

    var iv: ByteArray = ByteArray(16) { it.toByte() }

    fun encrypt(key: ByteArray, plainText: String): ByteArray {
        val plainTextB = plainText.toByteArray(Charsets.UTF_8)

        val originalKey: SecretKey = SecretKeySpec(key, 0, key.size, "AES")

        val ivParams = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, originalKey, ivParams)

        return cipher.doFinal(plainTextB)
    }
}
