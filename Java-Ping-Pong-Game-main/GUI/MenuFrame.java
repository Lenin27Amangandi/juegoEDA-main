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
        startPanel = new ImagePanel("Java-Ping-Pong-Game-main/Utilies/Museo.png");

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
        scoreButton.setBounds(450, 300, 150, 50);
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
        inputPanel.setLayout(new GridLayout(5, 1, 5, 5));

        inputPanel.add(new JLabel("Jugador 1:"));
        player1Field = new JTextField();
        inputPanel.add(player1Field);

        inputPanel.add(new JLabel("Jugador 2:"));
        player2Field = new JTextField();
        inputPanel.add(player2Field);

        JPanel btnPanel = new JPanel(new FlowLayout());
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

    // private void showScores() {
    //     new HistorialFrame();
    // }

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
