package mx.edu.utng.irc.appestudiantes.data.model.remote

import mx.edu.utng.irc.appestudiantes.data.model.EstudianteCreateRequest
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteResponse
import mx.edu.utng.irc.appestudiantes.data.model.EstudianteUpdateRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz que define todos los endpoints de nuestra API.
 *
 * Analogía: Esta interfaz es como un menú de restaurante.
 * Lista todas las opciones disponibles (endpoints) y qué necesitas
 * proporcionar para cada una (parámetros).
 */
interface ApiService {

    // ------------------------------------------------------------------
    // READ (Listar)
    // ------------------------------------------------------------------
    /**
     * Obtiene todos los estudiantes.
     * GET /estudiantes/
     */
    @GET("estudiantes/")
    suspend fun obtenerEstudiantes(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): Response<List<EstudianteResponse>> // Usamos EstudianteResponse como tipo de retorno

    /**
     * Obtiene un estudiante específico por ID.
     * GET /estudiantes/{id}
     */
    @GET("estudiantes/{id}")
    suspend fun obtenerEstudiante(
        @Path("id") id: Int
    ): Response<EstudianteResponse> // Usamos EstudianteResponse como tipo de retorno

    // ------------------------------------------------------------------
    // CREATE
    // ------------------------------------------------------------------
    /**
     * Crea un nuevo estudiante.
     * POST /estudiantes/
     */
    @POST("estudiantes/")
    suspend fun crearEstudiante(
        @Body estudiante: EstudianteCreateRequest // Usamos el modelo específico para CREAR
    ): Response<EstudianteResponse>

    // ------------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------------
    /**
     * Actualiza un estudiante existente.
     * PUT /estudiantes/{id}
     */
    @PUT("estudiantes/{id}")
    suspend fun actualizarEstudiante(
        @Path("id") id: Int,
        @Body estudiante: EstudianteUpdateRequest // Usamos el modelo específico para ACTUALIZAR (con campos nulos)
    ): Response<EstudianteResponse>

    // ------------------------------------------------------------------
    // DELETE
    // ------------------------------------------------------------------
    /**
     * Elimina un estudiante.
     * DELETE /estudiantes/{id}
     * La API devuelve 204 No Content, por lo que el cuerpo es nulo (Unit).
     */
    @DELETE("estudiantes/{id}")
    suspend fun eliminarEstudiante(
        @Path("id") id: Int
    ): Response<Unit>
}