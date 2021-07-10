package com.example.features.auth.domain

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

fun getHashWithSalt(password: String, saltLength: Int = 32): String {
    val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
    val saltAsHex = Hex.encodeHexString(salt)
    val hash = DigestUtils.sha256Hex("$saltAsHex$password")
    return "$saltAsHex:$hash"
}

fun checkHashForPassword(password: String, hashWithSalt: String): Boolean {
    val hashAndSalt = hashWithSalt.split(":")
    if(hashAndSalt.size != 2) {
        return false
    }
    val salt = hashAndSalt[0]
    val hash = hashAndSalt[1]
    val passwordHash = DigestUtils.sha256Hex("$salt$password")
    return hash == passwordHash
}