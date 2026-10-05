package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public abstract class VentanaCrud<T> extends JFrame {
    private static final Color COLOR_EXITO = new Color(0, 110, 0);
    private final VentanaPrincipal principal;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JLabel lblEstado = new JLabel(" ");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnActualizar = new JButton("Actualizar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JButton btnLimpiar = new JButton("Limpiar");
    private final JButton btnCerrar = new JButton("Cerrar");
    private List<T> mostrados = List.of();
    private boolean cargandoTabla = false;

    protected VentanaCrud(String titulo, VentanaPrincipal principal, String... columnas) {
        super(titulo);
        this.principal = principal;
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false;}
        };
        this.tabla = new JTable(modeloTabla);
    }

    protected abstract JPanel crearFormulario();
    protected JPanel crearFiltros() { return null;}
    protected abstract List<T> consultar() throws SQLException;
    protected abstract Object[] fila(T item);
    protected abstract int idDe(T item);
    protected abstract String descripcion(T item);
    protected abstract void mostrarEnFormulario(T item);
    protected abstract void limpiarFormulario();
    protected abstract T leerFormulario(T existente);
    protected abstract void insertar(T item) throws SQLException;
    protected abstract void modificar(T item) throws SQLException;
    protected abstract void borrar(T item) throws SQLException;
    protected void antesDeRefrescar() { }
    protected void alActivar() { }

    protected final void iniciar() {
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { volverInicio();}
            @Override
            public void windowActivated(WindowEvent e) { alActivar();}
        });

        JPanel norte = new JPanel(new BorderLayout(0, 5));
        norte.add(crearFormulario(), BorderLayout.CENTER);
        JPanel filtros = crearFiltros();
        if (filtros != null) {
            norte.add(filtros, BorderLayout.SOUTH);
        }

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnCerrar);

        lblEstado.setForeground(COLOR_EXITO);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        JPanel sur = new JPanel(new BorderLayout());
        sur.add(lblEstado, BorderLayout.NORTH);
        sur.add(botones, BorderLayout.CENTER);

        setLayout(new BorderLayout(10, 10));
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(70); // la primera columna es siempre el ID
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !cargandoTabla) {
                T item = seleccionado();
                if (item == null) {
                    limpiarFormulario();
                } else {
                    mostrarEnFormulario(item);
                }
                actualizarBotones();
            }
        });

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnCerrar.addActionListener(e -> volverInicio());

        limpiarFormulario();
        refrescar(null);
        setVisible(true);
    }


    private void guardar() {
        T nuevo = leerFormulario(null);
        if (nuevo == null) return;
        try {
            insertar(nuevo);
        } catch (SQLException e) {
            mostrarError("No fue posible guardar", e);
            return;
        }
        refrescar(idDe(nuevo));
        exito("Guardado correctamente (" + descripcion(nuevo) + ").");
    }

    private void actualizar() {
        T seleccionado = seleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.");
            return;
        }
        T modificado = leerFormulario(seleccionado);
        if (modificado == null) return;
        try {
            modificar(modificado);
        } catch (SQLException e) {
            mostrarError("No fue posible actualizar", e);
            return;
        }
        refrescar(idDe(modificado));
        exito("Actualizado correctamente (" + descripcion(modificado) + ").");
    }

    private void eliminar() {
        T seleccionado = seleccionado();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar " + descripcion(seleccionado) + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            borrar(seleccionado);
        } catch (SQLException e) {
            mostrarError("No fue posible eliminar", e);
            return;
        }
        refrescar(null);
        exito("Eliminado correctamente (" + descripcion(seleccionado) + ").");
    }

    private void limpiar() {
        tabla.clearSelection();
        limpiarFormulario();
        actualizarBotones();
        lblEstado.setText(" ");
    }

    private void volverInicio() {
        dispose();
        principal.setVisible(true);
    }

    protected final void refrescar() {
        T actual = seleccionado();
        refrescar(actual == null ? null : idDe(actual));
    }

    protected final void refrescar(Integer idASeleccionar) {
        boolean habiaSeleccion = tabla.getSelectedRow() >= 0;
        int filaASeleccionar = -1;
        antesDeRefrescar();
        cargandoTabla = true;
        try {
            mostrados = List.copyOf(consultar());
            modeloTabla.setRowCount(0);
            for (int i = 0; i < mostrados.size(); i++) {
                T item = mostrados.get(i);
                modeloTabla.addRow(fila(item));
                if (idASeleccionar != null && idDe(item) == idASeleccionar) {
                    filaASeleccionar = i;
                }
            }
            if (filaASeleccionar >= 0) {
                tabla.setRowSelectionInterval(filaASeleccionar, filaASeleccionar);
                tabla.scrollRectToVisible(tabla.getCellRect(filaASeleccionar, 0, true));
            }
        } catch (SQLException e) {
            mostrados = List.of();
            modeloTabla.setRowCount(0);
            mostrarError("No fue posible cargar los datos", e);
        } finally {
            cargandoTabla = false;
        }

        if (filaASeleccionar >= 0) {
            mostrarEnFormulario(mostrados.get(filaASeleccionar));
        } else if (habiaSeleccion || idASeleccionar != null) {
            limpiarFormulario();
        }
        actualizarBotones();
    }

    protected final T seleccionado() {
        int fila = tabla.getSelectedRow();
        return (fila >= 0 && fila < mostrados.size()) ? mostrados.get(fila) : null;
    }

    protected final T invalido(String mensaje, JComponent foco) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        if (foco != null) {
            foco.requestFocusInWindow();
        }
        return null;
    }

    protected final void mostrarError(String titulo, SQLException e) {
        System.err.println(titulo + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, titulo + ":\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void exito(String mensaje) { lblEstado.setText(mensaje);}

    private void actualizarBotones() {
        btnGuardar.setEnabled(seleccionado() == null);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }
}
