package com.rekognizevote.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object NetworkUtils {
    
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
    
    suspend fun createImageMultipart(file: File, paramName: String = "image"): MultipartBody.Part {
        return withContext(Dispatchers.IO) {
            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            MultipartBody.Part.createFormData(paramName, file.name, requestFile)
        }
    }
    
    fun getErrorMessage(throwable: Throwable): String {
        return when {
            throwable.message?.contains("timeout", ignoreCase = true) == true -> 
                "Tempo limite excedido. Verifique sua conexão."
            throwable.message?.contains("network", ignoreCase = true) == true -> 
                "Erro de rede. Verifique sua conexão."
            throwable.message?.contains("404") == true -> 
                "Recurso não encontrado."
            throwable.message?.contains("401") == true -> 
                "Não autorizado. Faça login novamente."
            throwable.message?.contains("500") == true -> 
                "Erro interno do servidor. Tente novamente."
            else -> throwable.message ?: "Erro desconhecido"
        }
    }
}