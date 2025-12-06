package Presentacion;

import Vistas.FrameReportes; 
import Presentacion.FrmAgendamiento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FrmMenuPrincipal extends JFrame {

    public FrmMenuPrincipal() {
        configurarVentana();
        iniciarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema Hospitalario - Menú Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar
        setLayout(new BorderLayout());
    }

    private void iniciarComponentes() {
        // ENCABEZADO 
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(40, 60, 100)); // Azul oscuro institucional
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("Sistema de Gestión Hospitalaria");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelHeader.add(lblTitulo);
        
        add(panelHeader, BorderLayout.NORTH);

        
        JPanel panelMenu = new JPanel(new GridLayout(0, 2, 20, 20)); 
        panelMenu.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panelMenu.setBackground(new Color(245, 245, 245));

        JButton btnConsultas = crearBotonMenu(
                "Consultar Historial", 
                "Ver reportes y agenda", 
                e -> abrirModuloConsultas()
        );

        JButton btnAgendamiento = crearBotonMenu(
                "Administrar Citas", 
                "Agendar, reprogramar o cancelar", 
                e -> abrirModuloAgendamiento()
        );
        
        JButton btnPacientes = crearBotonMenu(
                "Gestión Pacientes", 
                "Registrar altas y bajas (Próximamente)", 
                e -> mostrarMensajeConstruccion()
        );
        btnPacientes.setEnabled(false); 

        // Botón 4: Salir
        JButton btnSalir = crearBotonMenu(
                "Salir del Sistema", 
                "Cerrar sesión", 
                e -> System.exit(0)
        );
        btnSalir.setBackground(new Color(255, 200, 200)); // Un tono rojizo suave

        panelMenu.add(btnConsultas);
        panelMenu.add(btnAgendamiento);
        panelMenu.add(btnPacientes);
        panelMenu.add(btnSalir);

        add(panelMenu, BorderLayout.CENTER);
        
        // --- PIE DE PÁGINA ---
        JLabel lblFooter = new JLabel("Versión 1.0 - Conexión: ReporteConsultasHospital", SwingConstants.CENTER);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        lblFooter.setFont(new Font("SansSerif", Font.PLAIN, 10));
        add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Método fábrica para crear botones con estilo consistente.
     */
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

    // --- ACCIONES DE NAVEGACIÓN ---

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

    private void mostrarMensajeConstruccion() {
        JOptionPane.showMessageDialog(this, "Este módulo está en construcción.");
    }
}
