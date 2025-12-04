package Negocio;

import DTOS.AgendamientoDTO;
import Entidades.Cita;
import Entidades.Doctor;
import Entidades.Paciente;

/**
 * Clase utilitaria encargada de transformar objetos de transferencia (DTO)
 * a entidades de dominio y viceversa.
 */
public class AgendamientoMapper {

    /**
     * Convierte un DTO de entrada en una Entidad Cita lista para ser procesada.
     */
    public Cita toEntity(AgendamientoDTO dto) {
        if (dto == null) {
            return null;
        }

        Cita cita = new Cita();
        cita.setId(dto.getIdCita()); // Puede ser nulo si es nueva
        cita.setFechaHora(dto.getFechaHora());

        // Mapeo de referencias (Foreign Keys)
        if (dto.getIdDoctor() != null) {
            Doctor doc = new Doctor();
            doc.setId(dto.getIdDoctor());
            cita.setDoctor(doc);
        }

        if (dto.getIdPaciente() != null) {
            Paciente pac = new Paciente();
            pac.setId(dto.getIdPaciente());
            cita.setPaciente(pac);
        }

        return cita;
    }
    
    /**
     * Mapeo Inverso: Convierte una Entidad completa a un DTO simple.
     * Útil para cargar datos en la interfaz gráfica al editar.
     */
    public AgendamientoDTO toDTO(Cita cita) {
        if (cita == null) {
            return null;
        }
        
        AgendamientoDTO dto = new AgendamientoDTO();
        dto.setIdCita(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        
        if (cita.getDoctor() != null) {
            dto.setIdDoctor(cita.getDoctor().getId());
        }
        
        if (cita.getPaciente() != null) {
            dto.setIdPaciente(cita.getPaciente().getId());
        }
        
        return dto;
    }

}
