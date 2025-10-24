package com.tuempresa.gestionestudiantes.ui.screens // ⚠️ Ajuste del paquete si es necesario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteResponse
import java.util.Locale

/**
 * Pantalla con formulario para crear o editar estudiantes.
 *
 * Esta pantalla maneja dos modos:
 * - Crear: Todos los campos vacíos
 * - Editar: Campos pre-llenados con datos existentes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    // ⚠️ CORRECCIÓN 2: Usar EstudianteResponse como tipo
    estudiante: EstudianteResponse? = null, // null = crear, con datos = editar
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onGuardarClick: (String, Int, String, Double) -> Unit
) {
    // Para limpiar el foco al guardar
    val focusManager = LocalFocusManager.current

    // Estados locales para los campos del formulario
    var nombre by remember { mutableStateOf(estudiante?.nombre ?: "") }
    var edad by remember { mutableStateOf(estudiante?.edad?.toString() ?: "") }
    var carrera by remember { mutableStateOf(estudiante?.carrera ?: "") }
    var promedio by remember {
        mutableStateOf(
            estudiante?.promedio?.let { String.format(Locale.ROOT, "%.2f", it) } ?: ""
        )
    }

    // Estados de validación
    var nombreError by remember { mutableStateOf(false) }
    var edadError by remember { mutableStateOf(false) }
    var carreraError by remember { mutableStateOf(false) }
    var promedioError by remember { mutableStateOf(false) }

    val esEdicion = estudiante != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar Estudiante" else "Nuevo Estudiante") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo: Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = it.isBlank()
                },
                label = { Text("Nombre Completo *") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError,
                supportingText = {
                    if (nombreError) {
                        Text("El nombre es obligatorio")
                    }
                },
                singleLine = true,
                enabled = !isLoading
            )
            // Campo: Edad
            OutlinedTextField(
                value = edad,
                onValueChange = {
                    edad = it
                    // Validación en tiempo real
                    edadError = it.toIntOrNull() == null ||
                            (it.toIntOrNull() ?: 0) < 15 ||
                            (it.toIntOrNull() ?: 0) > 100
                },
                label = { Text("Edad *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = edadError,
                supportingText = {
                    if (edadError) {
                        Text("Edad inválida (15-100)")
                    }
                },
                singleLine = true,
                enabled = !isLoading
            )
            // Campo: Carrera
            OutlinedTextField(
                value = carrera,
                onValueChange = {
                    carrera = it
                    carreraError = it.isBlank()
                },
                label = { Text("Carrera *") },
                modifier = Modifier.fillMaxWidth(),
                isError = carreraError,
                supportingText = {
                    if (carreraError) {
                        Text("La carrera es obligatoria")
                    }
                },
                singleLine = true,
                enabled = !isLoading
            )
            // Campo: Promedio
            OutlinedTextField(
                value = promedio,
                onValueChange = {
                    promedio = it
                    // Validación en tiempo real
                    promedioError = it.toDoubleOrNull() == null ||
                            (it.toDoubleOrNull() ?: 0.0) < 0 ||
                            (it.toDoubleOrNull() ?: 0.0) > 100
                },
                label = { Text("Promedio Académico *") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = promedioError,
                supportingText = {
                    if (promedioError) {
                        Text("Promedio inválido (0-100)")
                    } else {
                        Text("Usa punto como separador decimal (ej: 85.5)")
                    }
                },
                singleLine = true,
                enabled = !isLoading
            )

            // Espacio flexible para empujar el botón al final
            Spacer(modifier = Modifier.weight(1f))

            // Botón de guardar
            Button(
                onClick = {
                    focusManager.clearFocus() // Ocultar teclado

                    // Re-validación final antes de la acción
                    val nombreValido = nombre.isNotBlank()
                    val carreraValida = carrera.isNotBlank()
                    val edadInt = edad.toIntOrNull()
                    val edadValida = edadInt != null && edadInt in 15..100
                    val promedioDouble = promedio.toDoubleOrNull()
                    val promedioValido = promedioDouble != null && promedioDouble in 0.0..100.0

                    // Actualizar los estados de error para mostrar feedback visual
                    nombreError = !nombreValido
                    carreraError = !carreraValida
                    edadError = !edadValida
                    val promedioValida = false
                    promedioError = !promedioValida

                    if (nombreValido && edadValida && carreraValida && promedioValido) {
                        // ⚠️ Lógica COMPLETA para llamar al callback
                        onGuardarClick(
                            nombre,
                            edadInt!!,
                            carrera,
                            promedioDouble!!
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading // Deshabilitar el botón mientras se carga
            ) {
                // Mostrar spinner o texto
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(if (esEdicion) "Guardar Cambios" else "Crear Estudiante")
                }
            }
        }
    }
}