package com.hocok.fortipass.domain.util.cipher

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode
import javax.crypto.spec.SecretKeySpec

// Пользователь вводит пароль при регистрации
// Он используется для получения key для алгоритма AES
// Так же сам пароль превращается в хэш и храниться в DataStore

object CipherManager {
    private val argon2Kt = Argon2Kt()

    private lateinit var cipherHelper: AESGCMHelper

    fun passwordToHash(password: String): Int{
        return password.hashCode()
    }

    fun checkPassword(password: String, hash: Int): Boolean{
        return passwordToHash(password) == hash
    }

    // Длеаем ключ с помощью Argon2_DI и создаем helper
    fun createHelper(passwordByteArray: ByteArray, saltByteArray: ByteArray){
        val key = argon2Kt.hash(
            mode = Argon2Mode.ARGON2_ID,
            password = passwordByteArray,
            salt = saltByteArray,
            tCostInIterations = 3,
            mCostInKibibyte = 16384,
            parallelism = 2,
        )

        cipherHelper =AESGCMHelper(SecretKeySpec(key.rawHashAsByteArray(), "AES"))
    }

    fun encrypt(password: String): Pair<String, String>{
        val encryptData = cipherHelper.encrypt(password)
        return encryptData
    }

    fun decrypt(password: String, iv: String): String{
        val decryptText = cipherHelper.decrypt(password, iv)
        return decryptText
    }
}