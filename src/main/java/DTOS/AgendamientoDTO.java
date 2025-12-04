package DTOS;

import java.time.LocalDateTime;

public class AgendamientoDTO {
    
    private Long idCita; // Solo se usar para editar o Programar Cita
    private Long idDoctor;
    private Long idPaciente;
    private LocalDateTime fechaHora;

    public AgendamientoDTO() {
    }

    public AgendamientoDTO(Long idDoctor, Long idPaciente, LocalDateTime fechaHora) {
        this.idDoctor = idDoctor;
        this.idPaciente = idPaciente;
        this.fechaHora = fechaHora;
    }

    // Getters y Setters
    public Long getIdCita() { 
        return idCita; 
    }
    public void setIdCita(Long idCita) {
        this.idCita = idCita; 
    }

    public Long getIdDoctor() {
        return idDoctor; 
    }
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor; 
    }

    public Long getIdPaciente() {
        return idPaciente; 
    }
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente; 
    }

    public LocalDateTime getFechaHora() {
        return fechaHora; 
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora; 
    }
}
