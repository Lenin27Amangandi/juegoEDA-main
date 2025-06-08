package GUI;

import BL.ScoreManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class HistorialPanel extends JPanel {
    private JTable scoreTable;
    private JComboBox<String> filterComboBox;
    private DefaultTableModel tableModel;
    private List<String> scoreData;
    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;

    public HistorialPanel(Runnable onBack) {
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

        backButton = new JButton("Regresar");
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        String[] columnNames = { "N°", "Jugador 1", "Pts", "Jugador 2", "Pts", "Resultado", "Fecha" };
        tableModel = new DefaultTableModel(columnNames, 0);
        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(25);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        puntuacion();
        if (ScoreManager.mensajeCola != null) {
            JOptionPane.showMessageDialog(this, ScoreManager.mensajeCola, "Historial lleno", JOptionPane.INFORMATION_MESSAGE);
            ScoreManager.mensajeCola = null;
        }

        filterComboBox.addActionListener(e -> accionesComboBox());
        searchButton.addActionListener(e -> buscarPartidas());
        backButton.addActionListener(e -> onBack.run());
    }

    private void puntuacion() {
        tableModel.setRowCount(0);
        scoreData = ScoreManager.loadScores();
        Collections.reverse(scoreData);
        int index = 1;
        for (String line : scoreData) {
            String[] parts = line.split(",", -1);
            if (parts.length == 6) {
                Object[] rowWithIndex = new Object[7];
                rowWithIndex[0] = index++;
                System.arraycopy(parts, 0, rowWithIndex, 1, parts.length);
                tableModel.addRow(rowWithIndex);
            }
        }
    }

    private void accionesComboBox() {
        String selected = (String) filterComboBox.getSelectedItem();
        if (selected == null) return;

        switch (selected) {
            case "Ordenar por puntaje ganador":
                ordenarPorPuntosGanadores();
                break;
            case "Eliminar última partida":
                int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar la última partida?",
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ScoreManager.deleteLastScore();
                    puntuacion();
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
        int index = 1;
        for (String line : scoreData) {
            String[] parts = line.split(",", -1);
            if (parts.length == 6) {
                String jugador1 = parts[0].toLowerCase();
                String jugador2 = parts[2].toLowerCase();
                String fecha = parts[5].toLowerCase();
                if (jugador1.contains(query) || jugador2.contains(query) || fecha.contains(query)) {
                    Object[] rowWithIndex = new Object[7];
                    rowWithIndex[0] = index++;
                    System.arraycopy(parts, 0, rowWithIndex, 1, parts.length);
                    tableModel.addRow(rowWithIndex);
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

        rows.sort((a, b) -> {
            int maxA = Math.max(Integer.parseInt((String) a.get(2)), Integer.parseInt((String) a.get(4)));
            int maxB = Math.max(Integer.parseInt((String) b.get(2)), Integer.parseInt((String) b.get(4)));
            return Integer.compare(maxB, maxA);
        });

        tableModel.setRowCount(0);
        int index = 1;
        for (Vector row : rows) {
            row.set(0, index++);
            tableModel.addRow(row);
        }
    }
}
