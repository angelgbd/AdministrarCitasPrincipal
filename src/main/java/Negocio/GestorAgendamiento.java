package Negocio;

import Validaciones.ValidadorAgendamiento;
import DAOS.AgendamientoDAO;
import Interfaces.IAgendamientoDAO;
import Entidades.Cita;
import Entidades.EstadoCita;
import Interfaces.IGestorAgendamiento;

public class GestorAgendamiento implements IGestorAgendamiento {

    private final IAgendamientoDAO agendamientoDAO;
    private final ValidadorAgendamiento validador;

    public GestorAgendamiento() {
        this.agendamientoDAO = new AgendamientoDAO();
        this.validador = new ValidadorAgendamiento();
    }

    @Override
    public void registrarNuevaCita(Cita cita) throws Exception {
        // 1. Validaciones básicas (Campos nulos, integridad)
        validador.validarDatosCita(cita);

        // 2. Validaciones de tiempo (No pasado, horario laboral)
        validador.validarFechaFutura(cita.getFechaHora());
        validador.validarHorarioLaboral(cita.getFechaHora());

        // 3. Validación de CONFLICTO (Regla crítica de negocio)
        // ¿El doctor ya está ocupado a esa hora?
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                cita.getDoctor().getId(), 
                cita.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El doctor seleccionado ya tiene una cita agendada en ese horario. Por favor seleccione otra hora.");
        }

        // 4. Configuración inicial
        cita.setEstado(EstadoCita.PROGRAMADA); // Aseguramos estado inicial correcto

        // 5. Persistencia
        agendamientoDAO.agendarCita(cita);
    }

    @Override
    public void reprogramarCita(Cita cita) throws Exception {
        // Validamos la nueva fecha
        validador.validarDatosCita(cita);
        validador.validarFechaFutura(cita.getFechaHora());

        // Verificamos conflicto nuevamente (excepto con la misma cita si no cambió hora, 
        // pero asumiremos que reprogramar implica cambio de hora)
        boolean ocupado = agendamientoDAO.existeCitaEnHorario(
                cita.getDoctor().getId(), 
                cita.getFechaHora()
        );

        if (ocupado) {
            throw new IllegalStateException("El horario destino ya está ocupado.");
        }

        // Mantenemos estado o cambiamos a PROGRAMADA si estaba cancelada
        cita.setEstado(EstadoCita.PROGRAMADA);
        
        agendamientoDAO.actualizarCita(cita);
    }

    @Override
    public void cancelarCita(Cita cita) throws Exception {
        if (cita == null || cita.getId() == null) {
            throw new IllegalArgumentException("No se puede cancelar una cita inexistente.");
        }
        
        // Simplemente cambiamos el estado
        cita.setEstado(EstadoCita.CANCELADA);
        
        // Usamos actualizar
        agendamientoDAO.actualizarCita(cita);
    }
}
