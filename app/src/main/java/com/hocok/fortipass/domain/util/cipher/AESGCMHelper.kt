package com.hocok.fortipass.domain.util.cipher


import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import java.util.Base64

class AESGCMHelper(private val secretKey: SecretKey) {

    companion object {
        private const val AES_MODE = "AES/GCM/NoPadding"
        private const val TAG_LENGTH = 128
        private const val IV_LENGTH = 12
    }

    fun encrypt(plaintext: String): Pair<String, String> {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ByteArray(IV_LENGTH).also { SecureRandom().nextBytes(it) }

        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKey,
            GCMParameterSpec(TAG_LENGTH, iv)
        )

        val ciphertext = cipher.doFinal(plaintext.toByteArray())

        val returnCipher = ciphertext.toBase64String()
        val returnIv = iv.toBase64String()


        return Pair(
            returnCipher,
            returnIv
        )
    }

    fun decrypt(ciphertextBase64: String, ivBase64: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val iv = ivBase64.toBase64ByteArray()
        val ciphertext = ciphertextBase64.toBase64ByteArray()
        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(TAG_LENGTH, iv)
        )

        val plaintext = cipher.doFinal(ciphertext)

        return String(plaintext, Charsets.UTF_8)
    }
}


private fun ByteArray.toBase64String(): String{
    return Base64.getEncoder().encodeToString(this)
}

private fun String.toBase64ByteArray(): ByteArray{
    return Base64.getDecoder().decode(this)
}