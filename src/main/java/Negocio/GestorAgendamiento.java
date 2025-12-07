package Negocio;

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
    private final AgendamientoMapper mapper; 

    public GestorAgendamiento() {
        this.agendamientoDAO = new AgendamientoDAO();
        this.validador = new ValidadorAgendamiento();
        this.mapper = new AgendamientoMapper(); 
    }

    @Override
    public void registrarNuevaCita(AgendamientoDTO dto) throws Exception {
        // Se usa el mapper para obtener la entidad
        Cita cita = mapper.toEntity(dto);

        // Se valida
        validador.validarDatosCita(cita);
        validador.validarFechaFutura(cita.getFechaHora());
        validador.validarHorarioLaboral(cita.getFechaHora());

        // Se verifica el conflicto
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                dto.getIdDoctor(), 
                dto.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El doctor seleccionado ya tiene una cita agendada en ese horario.");
        }

        // Se configura el estado y guardar
        cita.setEstado(EstadoCita.PROGRAMADA);
        agendamientoDAO.agendarCita(cita);
    }

    @Override
    public void reprogramarCita(AgendamientoDTO dto) throws Exception {
        // Se busca la cita original para verificar que existe
        Cita citaExistente = agendamientoDAO.buscarPorId(dto.getIdCita());
        if (citaExistente == null) {
            throw new IllegalArgumentException("La cita que intenta reprogramar no existe.");
        }

        // Se actualiza la fecha en la entidad
        citaExistente.setFechaHora(dto.getFechaHora());

        // Se valida la nueva fecha
        validador.validarFechaFutura(citaExistente.getFechaHora());

        // Se verifica con la nueva hora
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                citaExistente.getDoctor().getId(), 
                citaExistente.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El horario destino ya está ocupado.");
        }
        
        // Se reactiva si estaba cancelada
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
