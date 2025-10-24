package com.tuempresa.gestionestudiantes.data.repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteCreateRequest
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteResponse
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteUpdateRequest

/**
 * Repositorio que maneja todas las operaciones de datos de estudiantes.
 *
 * Analogía: El repositorio es como un "asistente personal" que sabe
 * exactamente cómo obtener, guardar y modificar información.
 */
class EstudianteRepository {
    private val apiService = RetrofitClient.apiService

    // ----------------------------------------------------------------------
    // READ (Listar)
    // ----------------------------------------------------------------------
    /**
     * Obtiene la lista de todos los estudiantes
     * @return Result con lista de estudiantes o excepción en caso de error
     */
    suspend fun obtenerEstudiantes(): Result<List<EstudianteResponse>> { // ⚠️ CORRECCIÓN 2: Usar EstudianteResponse
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerEstudiantes()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    // Mejor manejo del error HTTP
                    Result.failure(Exception("Error al obtener lista: ${response.code()} ${response.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un estudiante específico por ID
     */
    suspend fun obtenerEstudiante(id: Int): Result<EstudianteResponse> { // ⚠️ CORRECCIÓN 2: Usar EstudianteResponse
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerEstudiante(id)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    // Error 404 de Retrofit
                    Result.failure(Exception("Estudiante no encontrado. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // ----------------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------------
    /**
     * Crea un nuevo estudiante
     * ⚠️ CORRECCIÓN 3: Usar el modelo específico de request
     */
    suspend fun crearEstudiante(estudiante: EstudianteCreateRequest): Result<EstudianteResponse> { // ⚠️ CORRECCIÓN 2 y 3
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.crearEstudiante(estudiante)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    // Mejor manejo del error de validación 422
                    Result.failure(Exception("Error al crear estudiante. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // ----------------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------------
    /**
     * Actualiza un estudiante existente
     * ⚠️ CORRECCIÓN 3: Usar el modelo específico de request para actualización
     */
    suspend fun actualizarEstudiante(id: Int, estudiante: EstudianteUpdateRequest): // ⚠️ CORRECCIÓN 3
            Result<EstudianteResponse> { // ⚠️ CORRECCIÓN 2
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.actualizarEstudiante(id, estudiante)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al actualizar estudiante. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    // ----------------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------------
    /**
     * Elimina un estudiante
     * Nota: DELETE devuelve 204 No Content, la validación isSuccessful es suficiente.
     */
    suspend fun eliminarEstudiante(id: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.eliminarEstudiante(id)
                if (response.isSuccessful) {
                    Result.success(Unit) // Devuelve Unit si la eliminación fue 200/204
                } else {
                    // Puede ser 404 Not Found si el ID no existe
                    Result.failure(Exception("Error al eliminar estudiante. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

annotation class EstudianteRequest
