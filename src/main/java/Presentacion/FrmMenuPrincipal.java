package Presentacion;

// Importaciones de los 3 Módulos
import Vistas.FrameReportes;               // Módulo 1: Reportes (Librería Base)
import Presentacion.FrmAgendamiento; // Módulo 2: Citas (Local)
import Presentacion.FrmGestionPacientes;  // Módulo 3: Pacientes (Externo)
import Presentacion.FrmGestionDoctores;    // Módulo 4: Doctores (Nuevo Externo)

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FrmMenuPrincipal extends JFrame {

    public FrmMenuPrincipal() {
        configurarVentana();
        iniciarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema Hospitalario - Menú Principal");
        setSize(700, 500); // Un poco más grande para acomodar más botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void iniciarComponentes() {
        // --- ENCABEZADO ---
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(40, 60, 100));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("Sistema de Gestión Hospitalaria");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelHeader.add(lblTitulo);
        
        add(panelHeader, BorderLayout.NORTH);

        // --- PANEL DE BOTONES (GRILLA) ---
        // GridLayout dinámico: 0 filas (auto), 2 columnas
        JPanel panelMenu = new JPanel(new GridLayout(0, 2, 20, 20)); 
        panelMenu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panelMenu.setBackground(new Color(245, 245, 245));

        // 1. Reportes
        panelMenu.add(crearBotonMenu("Consultar Historial", "Ver reportes y agenda", e -> abrirModuloConsultas()));

        // 2. Citas
        panelMenu.add(crearBotonMenu("Administrar Citas", "Agendar, reprogramar o cancelar", e -> abrirModuloAgendamiento()));
        
        // 3. Pacientes
        panelMenu.add(crearBotonMenu("Gestión Pacientes", "Registrar altas y bajas", e -> abrirModuloPacientes()));

        // 4. Doctores (NUEVO)
        panelMenu.add(crearBotonMenu("Gestión Doctores", "Contrataciones y perfiles", e -> abrirModuloDoctores()));

        // 5. Salir
        JButton btnSalir = crearBotonMenu("Salir del Sistema", "Cerrar sesión", e -> System.exit(0));
        btnSalir.setBackground(new Color(255, 200, 200));
        panelMenu.add(btnSalir);

        add(panelMenu, BorderLayout.CENTER);
        
        // --- PIE ---
        JLabel lblFooter = new JLabel("Versión 1.1 - Sistema Completo", SwingConstants.CENTER);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    private JButton crearBotonMenu(String titulo, String subtitulo, ActionListener accion) {
        String textoHTML = String.format("<html><center><font size='5'>%s</font><br><font size='3' color='#555555'>%s</font></center></html>", titulo, subtitulo);
        JButton btn = new JButton(textoHTML);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        btn.addActionListener(accion);
        return btn;
    }

    // --- MÉTODOS DE NAVEGACIÓN ---

    private void abrirModuloConsultas() {
        FrameReportes frm = new FrameReportes();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frm.setVisible(true);
    }

    private void abrirModuloAgendamiento() {
        FrmAgendamiento frm = new FrmAgendamiento();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
    }

    private void abrirModuloPacientes() {
        FrmGestionPacientes frm = new FrmGestionPacientes();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
    }

    private void abrirModuloDoctores() {
        FrmGestionDoctores frm = new FrmGestionDoctores();
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new FrmMenuPrincipal().setVisible(true));
    }
}
