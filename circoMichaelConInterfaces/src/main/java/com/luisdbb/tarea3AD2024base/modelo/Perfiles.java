package com.luisdbb.tarea3AD2024base.modelo;

/**
 * Enum que representa los diferentes perfiles o roles que puede tener un
 * usuario dentro del sistema del circo.
 *
 * Cada perfil determina los permisos y las pantallas a las que puede acceder un
 * usuario. Estos roles se utilizan durante el proceso de autenticación y en la
 * gestión de personas.
 *
 * Perfiles disponibles: - COORDINACION: usuarios encargados de gestionar
 * espectáculos y artistas. - ARTISTA: usuarios que participan en números
 * circenses. - ADMIN: administrador del sistema con acceso completo. -
 * INVITADO: usuario con acceso limitado solo a consultas.
 *
 * Este enum forma parte del modelo de dominio.
 *
 * @author MichaelQP
 * @since 2026
 * @version 1.1
 */
public enum Perfiles {
	COORDINACION, ARTISTA, ADMIN, INVITADO
}
