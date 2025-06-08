package GUI;

import BL.ScoreManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class HistorialFrame extends JFrame {
    private JTable scoreTable;
    private JComboBox<String> filterComboBox;
    private DefaultTableModel tableModel;
    private List<String> scoreData;
    private JTextField searchField;
    private JButton searchButton;

    public HistorialFrame() {
        setTitle("Historial de Partidas");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Historial", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        String[] filters = { "Ordenar por puntaje ganador", "Eliminar última partida" };
        filterComboBox = new JComboBox<>(filters);
        searchField = new JTextField(15);
        searchButton = new JButton("Buscar");

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.add(new JLabel("Buscar:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(filterComboBox);

        JButton backButton = new JButton("Regresar");
        backButton.addActionListener(e -> dispose());
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        String[] columnNames = { "Jugador 1", "Pts", "Jugador 2", "Pts", "Resultado", "Fecha" };
        tableModel = new DefaultTableModel(columnNames, 0);
        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(25);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        puntuacion();
        if (ScoreManager.mensajeCola != null) {
            JOptionPane.showMessageDialog(this, ScoreManager.mensajeCola, "Historial lleno",
                    JOptionPane.INFORMATION_MESSAGE);
            ScoreManager.mensajeCola = null;
        }

        filterComboBox.addActionListener(e -> accionesComboBox());
        searchButton.addActionListener(e -> buscarPartidas());
        setVisible(true);
    }

    private void puntuacion() {
        tableModel.setRowCount(0);
        scoreData = ScoreManager.loadScores();
        Collections.reverse(scoreData);
        for (String line : scoreData) {
            String[] parts = line.split(",", -1);
            if (parts.length == 6) {
                tableModel.addRow(parts);
            }
        }
    }

    private void accionesComboBox() {
        String selected = (String) filterComboBox.getSelectedItem();
        if (selected == null)
            return;

        switch (selected) {
            case "Ordenar por puntaje ganador":
                ordenarPorPuntosGanadores();
                break;
            case "Eliminar última partida":
                int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar la última partida?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ScoreManager.deleteLastScore();
                    puntuacion(); // Actualiza la tabla
                    JOptionPane.showMessageDialog(this, "Última partida eliminada.");
                }
                filterComboBox.setSelectedIndex(0);
                break;
        }
    }

    private void buscarPartidas() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            puntuacion();
            return;
        }

        tableModel.setRowCount(0);
        for (String line : scoreData) {
            String[] parts = line.split(",", -1);
            if (parts.length == 6) {
                String jugador1 = parts[0].toLowerCase();
                String jugador2 = parts[2].toLowerCase();
                String fecha = parts[5].toLowerCase();
                if (jugador1.contains(query) || jugador2.contains(query) ||
                        fecha.contains(query)) {
                    tableModel.addRow(parts);
                }
            }
        }
    }

    private void ordenarPorPuntosGanadores() {
        List<Vector> rows = new Vector<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Vector row = (Vector) tableModel.getDataVector().elementAt(i);
            rows.add((Vector) row.clone());
        }
        for (int i = 1; i < rows.size(); i++) {
            Vector key = rows.get(i);
            int keyScore1 = Integer.parseInt((String) key.get(1));
            int keyScore2 = Integer.parseInt((String) key.get(3));
            int keyMax = Math.max(keyScore1, keyScore2);

            int j = i - 1;
            while (j >= 0) {
                Vector current = rows.get(j);
                int currentMax = Math.max(
                        Integer.parseInt((String) current.get(1)),
                        Integer.parseInt((String) current.get(3)));
                if (keyMax > currentMax) {
                    rows.set(j + 1, rows.get(j));
                    j--;
                } else {
                    break;
                }
            }
            rows.set(j + 1, key);
        }

        tableModel.setRowCount(0);
        for (Vector row : rows) {
            tableModel.addRow(row);
        }
    }
}
