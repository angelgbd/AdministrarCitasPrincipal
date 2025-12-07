package Validaciones;

import Entidades.Cita;
import java.time.LocalDateTime;

/**
 * Responsabilidad Única: Validar las reglas de negocio para citas.
 * @author AngelBeltran
 */
public class ValidadorAgendamiento {

    public void validarDatosCita(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La información de la cita no puede ser nula.");
        }
        if (cita.getDoctor() == null || cita.getDoctor().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un doctor válido.");
        }
        if (cita.getPaciente() == null || cita.getPaciente().getId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un paciente válido.");
        }
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora son obligatorias.");
        }
    }

    /**
     * Regla de Negocio: No se pueden agendar citas en el pasado.
     */
    public void validarFechaFutura(LocalDateTime fechaHora) {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede agendar una cita en una fecha u hora pasada.");
        }
    }
    
    /**
     * Regla de Negocio: Las citas deben ser en horario laboral 
     */
    public void validarHorarioLaboral(LocalDateTime fechaHora) {
        int hora = fechaHora.getHour();
        if (hora < 8 || hora > 20) {
            throw new IllegalArgumentException("La cita debe estar dentro del horario laboral (08:00 - 20:00).");
        }
    }
}
