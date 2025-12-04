package Interfaces;

import Entidades.Cita;

public interface IGestorAgendamiento {

    /**
     * Procesa el registro de una nueva cita validando todas las reglas de negocio.
     * @param cita La entidad con los datos seleccionados en la vista.
     * @throws Exception Si falla alguna validación o la persistencia.
     */
    void registrarNuevaCita(Cita cita) throws Exception;

    /**
     * Permite reprogramar una cita existente.
     * @param cita La entidad con la nueva fecha.
     */
    void reprogramarCita(Cita cita) throws Exception;
    
    /**
     * Cancela una cita.
     */
    void cancelarCita(Cita cita) throws Exception;
}
