package Interfaces;

import Entidades.Cita; // Importado de la dependencia
import java.time.LocalDateTime;

public interface IAgendamientoDAO {

    /**
     * Guarda una nueva cita en la base de datos.
     * Requiere una transacción activa.
     */
    void agendarCita(Cita cita) throws Exception;

    /**
     * Actualiza una cita existente (reprogramación o cambio de estado).
     */
    void actualizarCita(Cita cita) throws Exception;
    
    /**
     * Busca una cita por su ID (útil para cargarla antes de editar).
     */
    Cita buscarPorId(Long id);

    /**
     * Método CRÍTICO para el negocio:
     * Verifica si un doctor ya tiene una cita activa en esa fecha y hora.
     * @return true si el horario está ocupado.
     */
    boolean existeCitaEnHorario(Long idDoctor, LocalDateTime fechaHora);
}
