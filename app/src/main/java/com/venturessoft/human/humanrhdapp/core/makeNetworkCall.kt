package com.venturessoft.human.humanrhdapp.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> makeNetworkCall(call: suspend () -> T): ApiResponceStatus<T> = withContext(Dispatchers.IO){
    try {
        ApiResponceStatus.Success(call())
    }catch (exception:Exception){
        ApiResponceStatus.Error(exception.message.toString())
    }
}

fun evaluateResponce(codigo: String, errorMessage: String? = null) {
    if (codigo != "ET000" && codigo != "OK" && codigo != "0" && codigo != "RHD000") {
        if (codigo.isNotEmpty()){
            throw Exception(codigo)
        }else{
            if (!errorMessage.isNullOrEmpty()){
                throw Exception(errorMessage)
            }else{
                throw Exception("")
            }
        }
    }
}