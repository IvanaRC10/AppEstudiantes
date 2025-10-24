package com.tuempresa.gestionestudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // Usaremos Scaffold para integrar el SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.tuempresa.gestionestudiantes.ui.theme.GestionEstudiantesTheme // Importación correcta del tema
import com.tuempresa.gestionestudiantes.ui.screens.DetalleEstudianteScreen
import com.tuempresa.gestionestudiantes.ui.screens.FormularioScreen
import com.tuempresa.gestionestudiantes.ui.screens.ListaEstudiantesScreen
import com.tuempresa.gestionestudiantes.ui.viewmodel.EstudianteViewModel
import kotlinx.coroutines.launch


/**
 * Activity principal de la aplicación.
 */
class MainActivity : ComponentActivity() {
    // ViewModel compartido entre todas las pantallas
    private val viewModel: EstudianteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ⚠️ La función del tema debe ser importada o definida, no anotada.
            GestionEstudiantesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(viewModel)
                }
            }
        }
    }
}

// ⚠️ Se eliminó la definición incorrecta: annotation class GestionEstudiantesTheme...

/**
 * Sistema de navegación de la aplicación con SnackbarHost integrado.
 */
@Composable
fun AppNavigation(viewModel: EstudianteViewModel) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ⚠️ CORRECCIÓN 1: Observación de estados.
    // Usamos 'by viewModel.estado.collectAsState()' si fueran Flow/StateFlow.
    // Usamos 'by viewModel.estado' si son State<T> (que es como se definieron en el ViewModel).

    val estudiantes by viewModel.estudiantes
    val estudianteSeleccionado by viewModel.estudianteSeleccionado
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val operacionExitosa by viewModel.operacionExitosa

    // --- Efectos laterales ---

    // Efecto para mostrar errores
    LaunchedEffect(error) {
        error?.let { errorMessage -> // Renombramos 'it' a 'errorMessage' para claridad
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = "Cerrar"
                )
                viewModel.limpiarError()
            }
        }
    }

    // Efecto para navegar después de operación exitosa
    LaunchedEffect(operacionExitosa) {
        if (operacionExitosa) {
            // Si hay una pantalla encima del 'lista', la elimina
            navController.popBackStack()
            viewModel.resetearOperacionExitosa()
        }
    }

    // ⚠️ CORRECCIÓN 2: Uso de Scaffold para alojar NavHost y SnackbarHost
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "lista", // Pantalla inicial
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplicar el padding del Scaffold
        ) {
            // ========== RUTA: Lista de estudiantes ==========
            composable("lista") {
                ListaEstudiantesScreen(
                    estudiantes = estudiantes,
                    isLoading = isLoading,
                    error = error,
                    onEstudianteClick = { id ->
                        navController.navigate("detalle/$id")
                    },
                    onAgregarClick = {
                        viewModel.limpiarEstudianteSeleccionado()
                        navController.navigate("nuevo")
                    },
                    onEliminarClick = { id ->
                        viewModel.eliminarEstudiante(id)
                    },
                    onRecargarClick = {
                        viewModel.cargarEstudiantes()
                    }
                )
            }

            // ========== RUTA: Detalle de estudiante ==========
            composable(
                route = "detalle/{estudianteId}",
                arguments = listOf(
                    navArgument("estudianteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                // Null-safety para obtener el ID
                val estudianteId = backStackEntry.arguments?.getInt("estudianteId")

                // Cargar el estudiante solo si el ID es válido
                LaunchedEffect(estudianteId) {
                    if (estudianteId != null && estudianteId != 0) {
                        viewModel.cargarEstudiante(estudianteId)
                    }
                }

                DetalleEstudianteScreen(
                    estudiante = estudianteSeleccionado,
                    isLoading = isLoading,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditarClick = { id ->
                        navController.navigate("editar/$id")
                    }
                )
            }

            // ========== RUTA: Nuevo estudiante ==========
            composable("nuevo") {
                FormularioScreen(
                    estudiante = null, // Sin datos previos
                    isLoading = isLoading,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onGuardarClick = { nombre, edad, carrera, promedio ->
                        viewModel.crearEstudiante(nombre, edad, carrera, promedio)
                    }
                )
            }

            // ========== RUTA: Editar estudiante ==========
            composable(
                route = "editar/{estudianteId}",
                arguments = listOf(
                    navArgument("estudianteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                // Null-safety para obtener el ID
                val estudianteId = backStackEntry.arguments?.getInt("estudianteId")

                // Cargar el estudiante para editar
                LaunchedEffect(estudianteId) {
                    if (estudianteId != null && estudianteId != 0) {
                        viewModel.cargarEstudiante(estudianteId)
                    }
                }

                FormularioScreen(
                    estudiante = estudianteSeleccionado, // Con datos previos
                    isLoading = isLoading,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onGuardarClick = { nombre, edad, carrera, promedio ->
                        // ⚠️ Asumimos que estudianteId no será null aquí si la navegación fue correcta
                        if (estudianteId != null) {
                            viewModel.actualizarEstudiante(
                                estudianteId,
                                nombre,
                                edad,
                                carrera,
                                promedio
                            )
                        } else {
                            // Manejo de error si el ID es nulo
                            viewModel.error.let {
                                // Esto es solo un ejemplo, el error debe ser manejado por el ViewModel/LaunchedEffect
                                scope.launch { snackbarHostState.showSnackbar("Error: ID de estudiante no disponible.") }
                            }
                        }
                    }
                )
            }
        } // Fin de NavHost
    } // Fin de Scaffold
}

// ⚠️ Se eliminaron las definiciones de funciones y anotaciones auxiliares incorrectas al final del archivo.