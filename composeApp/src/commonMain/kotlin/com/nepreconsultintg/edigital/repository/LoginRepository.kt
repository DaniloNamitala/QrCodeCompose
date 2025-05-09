package com.nepreconsultintg.edigital.repository

import com.nepreconsultintg.edigital.datasource.EdigitalDataSource
import com.nepreconsultintg.edigital.models.LoginResponse
import com.nepreconsultintg.edigital.storage

class LoginRepository(private val datasource : EdigitalDataSource = EdigitalDataSource()) { // recebe como parametro a classe do datasource

    // Função para validar o cpf
    fun isValidCPF(cpf: String): Boolean{
        if (cpf.length != 11 || cpf.all { it == cpf[0] }) return false

        val numbers = cpf.map { it.toString().toInt() }
        val dv1 = (0..8).map { (10 - it) * numbers[it] }.sum() * 10 % 11 % 10
        val dv2 = (0..9).map { (11 - it) * numbers[it] }.sum() * 10 % 11 % 10

        return numbers[9] == dv1 && numbers[10] == dv2
    }

    suspend fun login(cpf: String, password: String): Boolean{
        println("login.LoginRepository")

        val response = datasource.login(cpf, password)

        response?.let {
            storage.setUser(it.user, it.token)
        }

        return response != null
    }
}