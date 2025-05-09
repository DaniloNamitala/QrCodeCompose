package com.nepreconsultintg.edigital.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nepreconsultintg.edigital.ScannerScreens
import com.nepreconsultintg.edigital.repository.LoginRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun Login(navHostController: NavHostController) {
    var cpf by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val repository = koinInject<LoginRepository>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cpf,
            onValueChange = { newValue ->
                cpf = newValue.filter { it.isDigit() }.take(11) // Aceita até 11 dígitos numéricos
            },
            label = { Text("CPF") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                println("Clicou!")
                coroutineScope.launch {
                    isLoading = true

                    val isSuccess = repository.login(cpf, password)

                    if (isSuccess){
                        println("success")

                        navHostController.navigate(ScannerScreens.Start.name)
                    } else {
                        println("unsuccess")
                    }

                    isLoading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            // TODO: Navegar para recuperação de senha
        }) {
            Text("Esqueci minha senha")
        }
    }
}