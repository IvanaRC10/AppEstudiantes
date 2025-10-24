package com.tuempresa.gestionestudiantes.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuempresa.gestionestudiantes.data.repository.EstudianteRepository
import kotlinx.coroutines.launch
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteCreateRequest
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteResponse
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteUpdateRequest

// ⚠️ Se eliminaron las definiciones de 'annotation class EstudianteRequest' y 'Estudiante'
// ya que deben ser importadas de 'com.tuempresa.gestionestudiantes.data.model'

class EstudianteViewModel : ViewModel() {
    private val repository = EstudianteRepository()

    // ========== ESTADOS ==========
    // Estados privados que solo el ViewModel puede modificar
    // ⚠️ CORRECCIÓN 1: Usar EstudianteResponse
    private val _estudiantes = mutableStateOf<List<EstudianteResponse>>(emptyList())
    private val _estudianteSeleccionado = mutableStateOf<EstudianteResponse?>(null)

    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf<String?>(null)
    private val _operacionExitosa = mutableStateOf(false)

    // Estados públicos (solo lectura)
    // ⚠️ CORRECCIÓN 1: Usar EstudianteResponse
    val estudiantes: State<List<EstudianteResponse>> = _estudiantes
    val estudianteSeleccionado: State<EstudianteResponse?> = _estudianteSeleccionado

    val isLoading: State<Boolean> = _isLoading
    val error: State<String?> = _error
    val operacionExitosa: State<Boolean> = _operacionExitosa

    init {
        cargarEstudiantes()
    }

    fun cargarEstudiantes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // ⚠️ CORRECCIÓN 2: Simplificar la llamada y eliminar asignación innecesaria a onFailure
            repository.obtenerEstudiantes()
                .onSuccess { lista ->
                    _estudiantes.value = lista // Asignación directa
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al cargar estudiantes"
                }

            _isLoading.value = false
        }
    }

    fun cargarEstudiante(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // ⚠️ CORRECCIÓN 2: Simplificar la llamada y eliminar asignación/lambdas innecesarias
            repository.obtenerEstudiante(id)
                .onSuccess { estudiante ->
                    _estudianteSeleccionado.value = estudiante // Asignación directa
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al cargar estudiante"
                }

            _isLoading.value = false
        }
    }

    fun crearEstudiante(nombre: String, edad: Int, carrera: String, promedio: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _operacionExitosa.value = false

            // ⚠️ CORRECCIÓN 3: Usar EstudianteCreateRequest
            val nuevoEstudiante = EstudianteCreateRequest(
                nombre = nombre,
                edad = edad,
                carrera = carrera,
                promedio = promedio
            )

            repository.crearEstudiante(nuevoEstudiante)
                .onSuccess {
                    _operacionExitosa.value = true
                    cargarEstudiantes() // Recargar lista
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al crear estudiante"
                }

            _isLoading.value = false
        }
    }

    fun actualizarEstudiante(id: Int, nombre: String, edad: Int, carrera: String, promedio: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _operacionExitosa.value = false

            // ⚠️ CORRECCIÓN 3: Usar EstudianteUpdateRequest
            val estudianteActualizado = EstudianteUpdateRequest(
                nombre = nombre,
                edad = edad,
                carrera = carrera,
                promedio = promedio
            )

            repository.actualizarEstudiante(id, estudianteActualizado)
                .onSuccess {
                    _operacionExitosa.value = true
                    cargarEstudiantes() // Recargar lista
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al actualizar estudiante"
                }

            _isLoading.value = false
        }
    }

    fun eliminarEstudiante(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.eliminarEstudiante(id)
                .onSuccess {
                    cargarEstudiantes() // Recargar lista
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Error al eliminar estudiante"
                }

            _isLoading.value = false
        }
    }

    // Métodos auxiliares
    fun limpiarError() {
        _error.value = null
    }

    fun resetearOperacionExitosa() {
        _operacionExitosa.value = false
    }

    fun limpiarEstudianteSeleccionado() {
        _estudianteSeleccionado.value = null
    }
}