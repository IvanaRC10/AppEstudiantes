package mx.edu.utng.irc.appestudiantes.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Modelo de datos que representa la RESPUESTA de un estudiante de la API.
 * * Usamos @Serializable para que Kotlinx Serialization pueda mapear el JSON.
 * Usamos @SerialName para mapear los nombres de snake_case (de la API) a
 * camelCase (en Kotlin).
 */
@Serializable
data class EstudianteResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("nombre")
    val nombre: String = "",
    @SerialName("edad")
    val edad: Int = 0,
    @SerialName("carrera")
    val carrera: String = "",
    @SerialName("promedio")
    val promedio: Double = 0.0,
    // La fecha de registro de la API es un String con formato ISO
    @SerialName("fecha_registro")
    val fechaRegistro: String = ""
    // Nota: Para usar LocalDateTime aquí, se necesitaría un Serializer personalizado
)

/**
 * Modelo para crear un estudiante (REQUEST)
 * * Nota: Los campos deben ser no nulos si son obligatorios en el POST (como en FastAPI).
 */
@Serializable
data class EstudianteCreateRequest(
    val nombre: String,
    val edad: Int,
    val carrera: String,
    val promedio: Double
)

/**
 * Modelo para actualizar un estudiante (REQUEST PUT/PATCH)
 * * Usamos propiedades nulas para indicar que un campo es opcional en la actualización.
 */
@Serializable
data class EstudianteUpdateRequest(
    val nombre: String? = null,
    val edad: Int? = null,
    val carrera: String? = null,
    val promedio: Double? = null
)