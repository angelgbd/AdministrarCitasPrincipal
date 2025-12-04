package com.hospital.administracion.negocio;

import DTOS.AgendamientoDTO;
import Validaciones.ValidadorAgendamiento;
import DAOS.AgendamientoDAO;
import Interfaces.IAgendamientoDAO;
import Entidades.Cita;
import Entidades.Doctor;
import Entidades.EstadoCita;
import Entidades.Paciente;
import Interfaces.IGestorAgendamiento;
import Negocio.AgendamientoMapper;

public class GestorAgendamiento implements IGestorAgendamiento {

    private final IAgendamientoDAO agendamientoDAO;
    private final ValidadorAgendamiento validador;
    private final AgendamientoMapper mapper; // Nueva dependencia

    public GestorAgendamiento() {
        this.agendamientoDAO = new AgendamientoDAO();
        this.validador = new ValidadorAgendamiento();
        this.mapper = new AgendamientoMapper(); // Inicialización
    }

    @Override
    public void registrarNuevaCita(AgendamientoDTO dto) throws Exception {
        // 1. Usamos el Mapper para obtener la entidad
        Cita cita = mapper.toEntity(dto);

        // 2. Validaciones
        validador.validarDatosCita(cita);
        validador.validarFechaFutura(cita.getFechaHora());
        validador.validarHorarioLaboral(cita.getFechaHora());

        // 3. Verificación de conflicto
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                dto.getIdDoctor(), 
                dto.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El doctor seleccionado ya tiene una cita agendada en ese horario.");
        }

        // 4. Configurar estado y guardar
        cita.setEstado(EstadoCita.PROGRAMADA);
        agendamientoDAO.agendarCita(cita);
    }

    @Override
    public void reprogramarCita(AgendamientoDTO dto) throws Exception {
        // Buscamos la cita original para asegurarnos que existe
        Cita citaExistente = agendamientoDAO.buscarPorId(dto.getIdCita());
        if (citaExistente == null) {
            throw new IllegalArgumentException("La cita que intenta reprogramar no existe.");
        }

        // Actualizamos solo la fecha en la entidad existente
        citaExistente.setFechaHora(dto.getFechaHora());

        // Validamos la nueva fecha
        validador.validarFechaFutura(citaExistente.getFechaHora());

        // Verificamos conflicto con la nueva hora
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                citaExistente.getDoctor().getId(), 
                citaExistente.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El horario destino ya está ocupado.");
        }
        
        // Reactivamos si estaba cancelada
        citaExistente.setEstado(EstadoCita.PROGRAMADA);

        agendamientoDAO.actualizarCita(citaExistente);
    }

    @Override
    public void cancelarCita(Long idCita) throws Exception {
        if (idCita == null) {
            throw new IllegalArgumentException("ID de cita inválido.");
        }
        
        Cita cita = agendamientoDAO.buscarPorId(idCita);
        if (cita == null) {
            throw new IllegalArgumentException("No se puede cancelar una cita inexistente.");
        }
        
        cita.setEstado(EstadoCita.CANCELADA);
        agendamientoDAO.actualizarCita(cita);
    }
}
