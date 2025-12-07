package Presentacion;

import Negocio.GestorAgendamiento;
import Interfaces.IGestorAgendamiento;
import DTOS.AgendamientoDTO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FrmAgendamiento extends JFrame {

    private final IGestorAgendamiento gestor;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    // Componentes Nueva Cita
    private JTextField txtNewIdDoctor, txtNewIdPaciente, txtNewFecha;
    // Componentes Gestión (Reprogramar/Cancelar)
    private JTextField txtEditIdCita, txtEditFecha;

    public FrmAgendamiento() {
        this.gestor = new GestorAgendamiento();
        configurarVentana();
        iniciarComponentes();
    }

    private void configurarVentana() {
        setTitle("Módulo de Administración de Citas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void iniciarComponentes() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña 1: Agendar
        tabbedPane.addTab("Nueva Cita", crearPanelNuevaCita());

        // Pestaña 2: Reprogramar / Cancelar
        tabbedPane.addTab("Gestión de Citas", crearPanelGestion());

        add(tabbedPane);
    }

    private JPanel crearPanelNuevaCita() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("ID Doctor:"));
        txtNewIdDoctor = new JTextField();
        panel.add(txtNewIdDoctor);

        panel.add(new JLabel("ID Paciente:"));
        txtNewIdPaciente = new JTextField();
        panel.add(txtNewIdPaciente);

        panel.add(new JLabel("Fecha (dd/MM/yyyy HH:mm):"));
        txtNewFecha = new JTextField();
        txtNewFecha.setToolTipText("Ejemplo: 25/12/2023 15:30");
        panel.add(txtNewFecha);

        panel.add(new JLabel("")); // un espacio vacio
        
        JButton btnGuardar = new JButton("Agendar Cita");
        btnGuardar.addActionListener(e -> accionAgendar());
        panel.add(btnGuardar);

        return panel;
    }

    private JPanel crearPanelGestion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Sub-panel formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("ID Cita a Gestionar:"));
        txtEditIdCita = new JTextField();
        formPanel.add(txtEditIdCita);

        formPanel.add(new JLabel("Nueva Fecha (Solo Reprogramar):"));
        txtEditFecha = new JTextField();
        formPanel.add(txtEditFecha);

        panel.add(formPanel, BorderLayout.CENTER);

        // Sub-panel botones
        JPanel btnPanel = new JPanel(new FlowLayout());
        
        JButton btnReprogramar = new JButton("Reprogramar");
        btnReprogramar.addActionListener(e -> accionReprogramar());
        
        JButton btnCancelar = new JButton("Cancelar Cita");
        btnCancelar.setBackground(new java.awt.Color(255, 100, 100)); // Rojo suave
        btnCancelar.setForeground(java.awt.Color.WHITE);
        btnCancelar.addActionListener(e -> accionCancelar());

        btnPanel.add(btnReprogramar);
        btnPanel.add(btnCancelar);
        
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // --- ACCIONES ---

    private void accionAgendar() {
        try {
            Long idDoc = Long.parseLong(txtNewIdDoctor.getText());
            Long idPac = Long.parseLong(txtNewIdPaciente.getText());
            LocalDateTime fecha = LocalDateTime.parse(txtNewFecha.getText(), formatter);

            AgendamientoDTO dto = new AgendamientoDTO(idDoc, idPac, fecha);
            
            gestor.registrarNuevaCita(dto);
            
            JOptionPane.showMessageDialog(this, "¡Cita agendada con éxito!");
            limpiarCamposNueva();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Los IDs deben ser numéricos.", "Validación", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Error: Formato de fecha inválido. Use dd/MM/yyyy HH:mm", "Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accionReprogramar() {
        try {
            Long idCita = Long.parseLong(txtEditIdCita.getText());
            LocalDateTime nuevaFecha = LocalDateTime.parse(txtEditFecha.getText(), formatter);

            AgendamientoDTO dto = new AgendamientoDTO();
            dto.setIdCita(idCita);
            dto.setFechaHora(nuevaFecha);

            gestor.reprogramarCita(dto);

            JOptionPane.showMessageDialog(this, "Cita reprogramada correctamente.");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de la cita debe ser numérico.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void accionCancelar() {
        try {
            String input = txtEditIdCita.getText();
            if (input.isEmpty()) {
                throw new IllegalArgumentException("Ingrese el ID de la cita a cancelar.");
            }
            Long idCita = Long.parseLong(input);

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de cancelar la cita " + idCita + "?", 
                    "Confirmar Cancelación", 
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                gestor.cancelarCita(idCita);
                JOptionPane.showMessageDialog(this, "Cita cancelada.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void limpiarCamposNueva() {
        txtNewIdDoctor.setText("");
        txtNewIdPaciente.setText("");
        txtNewFecha.setText("");
    }
    
    // Método main para probar este módulo independientemente
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new FrmAgendamiento().setVisible(true));
    }
}
