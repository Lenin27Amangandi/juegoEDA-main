package GUI;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {

    private JPanel startPanel;
    private JPanel inputPanel;

    private JTextField player1Field;
    private JTextField player2Field;
    private JButton playButton;
    private JButton backButton;

    private JButton startButton;
    private JButton scoreButton;

    public MenuFrame() {
        setTitle("Ping Pong Menu");
        setSize(1000, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        createStartPanel();
        createInputPanel();
        setContentPane(startPanel);
    }

    private void createStartPanel() {
        startPanel = new ImagePanel("Java-Ping-Pong-Game-main/Utilies/FondoGame.jpg");

        startPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Bienvenido al Ping Pong", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(250, 50, 500, 50);
        startPanel.add(welcomeLabel);

        startButton = new JButton("Iniciar Juego");
        startButton.setBounds(300, 300, 130, 50);
        startPanel.add(startButton);

        scoreButton = new JButton("Ver Puntajes");
        scoreButton.setBounds(470, 300, 150, 50);
        startPanel.add(scoreButton);

        startButton.addActionListener(e -> {
            setContentPane(inputPanel);
            revalidate();
            repaint();
        });

        scoreButton.addActionListener(e -> showScores());
    }

    private void createInputPanel() {
    inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

    JLabel label1 = new JLabel("Jugador 1:");
    label1.setAlignmentX(Component.CENTER_ALIGNMENT);
    player1Field = new JTextField(15);
    player1Field.setMaximumSize(player1Field.getPreferredSize());
    inputPanel.add(label1);
    inputPanel.add(Box.createVerticalStrut(5));
    inputPanel.add(player1Field);
    inputPanel.add(Box.createVerticalStrut(10));

    JLabel label2 = new JLabel("Jugador 2:");
    label2.setAlignmentX(Component.CENTER_ALIGNMENT);
    player2Field = new JTextField(15);
    player2Field.setMaximumSize(player2Field.getPreferredSize());
    inputPanel.add(label2);
    inputPanel.add(Box.createVerticalStrut(5));
    inputPanel.add(player2Field);
    inputPanel.add(Box.createVerticalStrut(20));

    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    playButton = new JButton("Jugar");
    backButton = new JButton("Volver");
    btnPanel.add(playButton);
    btnPanel.add(backButton);
    inputPanel.add(btnPanel);

    playButton.addActionListener(e -> {
        String p1 = player1Field.getText().trim();
        String p2 = player2Field.getText().trim();
        if (!p1.isEmpty() && !p2.isEmpty()) {
            this.dispose();
            GameFrame game = new GameFrame();
            game.setPlayerNames(p1, p2);
            game.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese los nombres de los jugadores");
        }
    });

    backButton.addActionListener(e -> {
        setContentPane(startPanel);
        revalidate();
        repaint();
    });
}


    private void showScores() {
    HistorialPanel historialPanel = new HistorialPanel(() -> {
        setContentPane(startPanel);
        revalidate();
        repaint();
    });

    setContentPane(historialPanel);
    revalidate();
    repaint();
}
    class ImagePanel extends JPanel {
        private Image backgroundImage;
        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
