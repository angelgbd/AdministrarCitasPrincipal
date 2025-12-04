package Interfaces;

import DTOS.AgendamientoDTO;

public interface IGestorAgendamiento {

    void registrarNuevaCita(AgendamientoDTO datosCita) throws Exception;

    void reprogramarCita(AgendamientoDTO datosCita) throws Exception;
    
    void cancelarCita(Long idCita) throws Exception;
}
