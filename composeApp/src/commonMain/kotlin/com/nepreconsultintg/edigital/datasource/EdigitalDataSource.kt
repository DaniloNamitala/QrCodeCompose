package com.nepreconsultintg.edigital.datasource

import com.nepreconsultintg.edigital.configureForPlatform
import com.nepreconsultintg.edigital.models.LoginRequest
import com.nepreconsultintg.edigital.models.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class EdigitalDataSource{

    // Client http
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        configureForPlatform()
    }

    // Função que fará a requisição de login para a API
    suspend fun login(cpf: String, password: String): LoginResponse?{

        return try{
            val response: HttpResponse = client.post("http://192.168.179.250/Mina/ApiCadastro/Login"){
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(cpf, password))
            }

            val responseLogin = response.body<LoginResponse>()

            println("TOMA: ${responseLogin}")

            responseLogin
        } catch (e: Exception){
            println("Erro ao fazer login: ${e.message}")
            null
        }
    }
}