package com.wristband.eko.data

data class Result<T>(
    val body: T, val status: Boolean, val message: String)