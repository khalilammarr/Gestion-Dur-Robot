import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class InterfaceRobotLivraison extends JFrame {
    private final int GRID_SIZE = 6;
    private JButton[][] gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
    private HashMap<String, Point> destinationMap = new HashMap<>();
    private Point destinationPoint = null;
    private ImageIcon robotImage;
    private RobotLivraison robotLivraison;
    private JLabel energieLabel;
    private JLabel connectionLabel;
    private String[][] cellLabels = new String[GRID_SIZE][GRID_SIZE]; // Store original cell labels
    private String[][] nomsBoutons = new String[GRID_SIZE][GRID_SIZE];


    public InterfaceRobotLivraison() {
        setLocationRelativeTo(null);
        setTitle("Simulation Robot Livraison");
        setSize(1400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Load and scale robot image
        try {
            robotImage = loadRobotImage("/robot_icon.png"); // Ensure the image is in resources or root
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image robot non trouvée !");
            robotImage = null;
        }

        // Panel grille
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        String[] lieuxTunisie = {
                "Tunis", "La Marsa", "Sidi Bou Saïd", "Ariana", "Carthage", "Le Bardo",
                "Sfax", "Gabès", "Djerba", "Houmt Souk", "Midoun", "Zarzis",
                "Nabeul", "Hammamet", "Kelibia", "Korba", "Sousse", "Kantaoui",
                "Monastir", "Mahdia", "Kairouan", "Tozeur", "Nefta", "Douz",
                "Gafsa", "Kébili", "Tataouine", "Médenine", "El Jem", "Testour",
                "Bizerte", "Tabarka", "Beja", "Le Kef", "Siliana", "Jendouba"
        };

        int index = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String name = lieuxTunisie[index++];
                JButton btn = new JButton(name);
                nomsBoutons[i][j] = name;
                btn.setBackground(Color.WHITE);
                btn.setFont(new Font("Arial", Font.PLAIN, 12));
                destinationMap.put(name.toUpperCase(), new Point(i, j));
                gridButtons[i][j] = btn;
                gridPanel.add(btn);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Panel de contrôle
        JPanel controlPanel = new JPanel();

        JButton btnCreer = new JButton("Créer Robot");
        JButton btnCharger = new JButton("Charger Colis");
        JButton btnLivrer = new JButton("Faire Livraison");
        JButton btnDemarrer = new JButton("Démarrer");
        JButton btnArreter = new JButton("Arrêter");
        JButton btnConfig = new JButton("Configuration");
        JButton btnConnecter = new JButton("Connecter");
        JButton btnDeconnecter = new JButton("Déconnecter");
        JButton btnEnvoyerDonnees = new JButton("Envoyer Données");
        JButton btnChanter = new JButton("Chanter");

        btnCreer.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2));
            JTextField fieldId = new JTextField();
            JTextField fieldX = new JTextField();
            JTextField fieldY = new JTextField();

            panel.add(new JLabel("ID du Robot :"));
            panel.add(fieldId);
            panel.add(new JLabel("Position X (0-" + (GRID_SIZE - 1) + ") :"));
            panel.add(fieldX);
            panel.add(new JLabel("Position Y (0-" + (GRID_SIZE - 1) + ") :"));
            panel.add(fieldY);

            int result = JOptionPane.showConfirmDialog(this, panel, "Créer un nouveau robot", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int x = Integer.parseInt(fieldX.getText());
                    int y = Integer.parseInt(fieldY.getText());
                    String id = fieldId.getText().trim();

                    if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE || id.isEmpty()) {
                        throw new IllegalArgumentException();
                    }
                    robotLivraison=new RobotLivraison(x,y,id);
                    updateGrid();
                    JOptionPane.showMessageDialog(this, "Robot \"" + id + "\" créé à (" + x + "," + y + ")");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Entrée invalide !");
                }
            }
        });
        btnDemarrer.addActionListener(e -> {
            if (robotLivraison.getEnergie()> 10) {
                robotLivraison.setEnMarche(true);
                robotLivraison.ajouterHistorique("Robot démarré");
                robotLivraison.consommerEnergie(10);
                updateGrid();
                JOptionPane.showMessageDialog(this, "Robot démarré.");
            } else {
                JOptionPane.showMessageDialog(this, "Impossible de démarrer. Batterie vide.");
            }
        });

        btnArreter.addActionListener(e -> {
            robotLivraison.setEnMarche(false);
            robotLivraison.ajouterHistorique("Robot arrêté");
            updateGrid();
            JOptionPane.showMessageDialog(this, "Robot arrêté.");
        });

        btnCharger.addActionListener(e -> {
            if (!robotLivraison.isEnMarche()) {
                JOptionPane.showMessageDialog(this, "Le robot doit être démarré pour charger un colis.");
                return;
            }
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField fieldColis = new JTextField();
            JTextField fieldDest = new JTextField();
            panel.add(new JLabel("Nom du colis :"));
            panel.add(fieldColis);
            panel.add(new JLabel("Destination (ex: Sfax) :"));
            panel.add(fieldDest);

            int result = JOptionPane.showConfirmDialog(this, panel, "Charger Colis", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                robotLivraison.setColis(fieldColis.getText());
                robotLivraison.setDestination( fieldDest.getText().toUpperCase());
                if (destinationMap.containsKey(robotLivraison.getDestination())) {
                    destinationPoint = destinationMap.get(robotLivraison.getDestination());
                    robotLivraison.ajouterHistorique("Colis '" + robotLivraison.getColis() + "' chargé pour " + robotLivraison.getDestination());
                    JOptionPane.showMessageDialog(this, "Colis \"" + robotLivraison.getColis() + "\" chargé pour " + robotLivraison.getDestination());
                    highlightDestination(destinationPoint);
                } else {
                    JOptionPane.showMessageDialog(this, "Destination invalide !");
                }
            }
        });

        btnLivrer.addActionListener(e -> {
            if (!robotLivraison.isEnMarche()) {
                JOptionPane.showMessageDialog(this, "Le robot doit être démarré pour livrer.");
                return;
            }
            if (destinationPoint == null) {
                JOptionPane.showMessageDialog(this, "Aucune destination définie !");
                return;
            }
            //robotLivraison.faireLivraison(destinationPoint.x, destinationPoint.y);
            moveRobotTo(destinationPoint);
        });
        btnConfig.addActionListener(e -> {
            JTextArea area = new JTextArea();

            // Using traditional for loop instead of forEach
            List<String> historique = robotLivraison.getHistoriqueActions();
            for (int i = 0; i < historique.size(); i++) {
                String entry = historique.get(i);
                area.append(entry + "\n");
            }

            area.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JButton rechargeBtn = new JButton("Recharger");
            rechargeBtn.addActionListener(ev -> {
                robotLivraison.setEnergie(100);
                robotLivraison.ajouterHistorique("Batterie rechargée à 100%");
                updateGrid();
            });

            JPanel configPanel = new JPanel(new BorderLayout());
            configPanel.add(scrollPane, BorderLayout.CENTER);
            configPanel.add(rechargeBtn, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(this, configPanel, "Configuration", JOptionPane.PLAIN_MESSAGE);
        });
        btnChanter.addActionListener(e -> {
            if (robotLivraison == null) {
                JOptionPane.showMessageDialog(this, "Erreur: Robot non créé !",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!robotLivraison.enMarche) {
                JOptionPane.showMessageDialog(this, "Erreur: Robot non démarré !",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!robotLivraison.connecte) {
                JOptionPane.showMessageDialog(this, "Erreur: Robot non connecté !",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
                robotLivraison.chanter();
                JOptionPane.showMessageDialog(this, "Le robot chante !",
                        "Information", JOptionPane.INFORMATION_MESSAGE);

        });
        btnConnecter.addActionListener(e -> {
            if (robotLivraison == null) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord créer un robot.");
                return;
            }

            if (!robotLivraison.isEnMarche()) {
                JOptionPane.showMessageDialog(this, "Le robot doit être démarré pour se connecter.");
                return;
            }

            if (robotLivraison.getEnergie() < 5) {
                JOptionPane.showMessageDialog(this, "La connexion nécessite au moins 5% d'énergie.");
                return;
            }

            String reseau = JOptionPane.showInputDialog(this, "Nom du réseau à connecter:", "Connexion", JOptionPane.QUESTION_MESSAGE);
            if (reseau != null && !reseau.trim().isEmpty()) {
                try {
                    robotLivraison.connecter(reseau);
                    connectionLabel.setText("État: Connecté à " + reseau);
                    connectionLabel.setForeground(Color.GREEN);
                    JOptionPane.showMessageDialog(this, "Robot connecté au réseau: " + reseau);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la connexion: " + ex.getMessage());
                }
            }
        });

        btnDeconnecter.addActionListener(e -> {
            if (robotLivraison == null) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord créer un robot.");
                return;
            }

            try {
                robotLivraison.deconnecter();
                connectionLabel.setText("État: Non connecté");
                connectionLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "Robot déconnecté du réseau.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la déconnexion: " + ex.getMessage());
            }
        });

        btnEnvoyerDonnees.addActionListener(e -> {
            if (robotLivraison == null) {
                JOptionPane.showMessageDialog(this, "Veuillez d'abord créer un robot.");
                return;
            }

            if (!robotLivraison.isEnMarche()) {
                JOptionPane.showMessageDialog(this, "Le robot doit être démarré pour envoyer des données.");
                return;
            }

            String donnees = JOptionPane.showInputDialog(this, "Données à envoyer:", "Envoyer Données", JOptionPane.QUESTION_MESSAGE);
            if (donnees != null && !donnees.trim().isEmpty()) {
                try {
                    robotLivraison.envoyerDonnees(donnees);
                    updateGrid(); // Update to reflect energy consumption
                    JOptionPane.showMessageDialog(this, "Données envoyées avec succès.");
                } catch (RobotNonConnecteException ex) {
                    JOptionPane.showMessageDialog(this, "Le robot n'est pas connecté. Veuillez vous connecter d'abord.");
                } catch (EnergieInsuffisanteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            }
        });

        controlPanel.add(btnCreer);
        controlPanel.add(btnDemarrer);
        controlPanel.add(btnArreter);
        controlPanel.add(btnCharger);
        controlPanel.add(btnLivrer);
        controlPanel.add(btnConnecter);
        controlPanel.add(btnDeconnecter);
        controlPanel.add(btnEnvoyerDonnees);
        controlPanel.add(btnChanter);
        controlPanel.add(btnConfig);
        // Affichage simple de l'énergie
        energieLabel = new JLabel("Énergie actuelle : --%");
        connectionLabel = new JLabel("État: Non connecté");
        connectionLabel.setForeground(Color.RED);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(energieLabel);
        topPanel.add(new JLabel(" | "));
        topPanel.add(connectionLabel);
        add(topPanel, BorderLayout.NORTH);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private ImageIcon loadRobotImage(String path) throws Exception {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            throw new Exception("Image not found: " + path);
        }
        ImageIcon icon = new ImageIcon(imgURL);
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void highlightDestination(Point p) {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                gridButtons[i][j].setBackground(Color.WHITE);

        gridButtons[p.x][p.y].setBackground(Color.YELLOW);
        updateGrid();
    }

    private void updateGrid() {
        // Reset all cells to their original state
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridButtons[i][j].setText(nomsBoutons[i][j]);
                gridButtons[i][j].setIcon(null);
                gridButtons[i][j].setBackground(Color.WHITE);
            }
        }

        // Check if robot exists
        if (robotLivraison != null) {
            // Set robot position
            if (robotImage != null) {
                gridButtons[robotLivraison.getX()][robotLivraison.getY()].setText("");
                gridButtons[robotLivraison.getX()][robotLivraison.getY()].setIcon(robotImage);
            } else {
                gridButtons[robotLivraison.getX()][robotLivraison.getY()].setText("🤖");
            }

            // Change background color based on robot state
            Color robotColor = Color.LIGHT_GRAY; // Default: not running
            if (robotLivraison.isEnMarche()) {
                if (robotLivraison.connecte) { // Check if connected - using direct field access
                    robotColor = new Color(100, 200, 100); // Green if connected
                } else {
                    robotColor = Color.CYAN; // Blue if running but not connected
                }
            }
            gridButtons[robotLivraison.getX()][robotLivraison.getY()].setBackground(robotColor);

            // Update energy label
            energieLabel.setText("Énergie actuelle : " + robotLivraison.getEnergie() + "%");

            // Update connection status
            if (robotLivraison.connecte) {
                connectionLabel.setText("État: Connecté à " + robotLivraison.reseauConnecte);
                connectionLabel.setForeground(Color.GREEN);
            } else {
                connectionLabel.setText("État: Non connecté");
                connectionLabel.setForeground(Color.RED);
            }
        }

        // Highlight destination if there is one
        if (destinationPoint != null) {
            gridButtons[destinationPoint.x][destinationPoint.y].setBackground(Color.YELLOW);
        }
    }

    private void moveRobotTo(Point dest) {
        Timer timer = new Timer(300, null); // 300ms delay between moves

        timer.addActionListener(e -> {
            if (!robotLivraison.isEnMarche() || robotLivraison.getEnergie() <= 0 ||
                    (robotLivraison.getX() == dest.x && robotLivraison.getY() == dest.y)) {

                // Stop timer if robot reached destination or is out of energy or stopped
                timer.stop();

                // If reached and still has energy, finalize delivery
                if (robotLivraison.getEnergie() > 0 && robotLivraison.isEnMarche()) {
                    robotLivraison.ajouterHistorique("Livraison du colis '" + robotLivraison.getColis() + "' à " + robotLivraison.getDestination());
                    JOptionPane.showMessageDialog(null, "Livraison du colis \"" + robotLivraison.getColis() + "\" à " + robotLivraison.getDestination() + " effectuée.");
                    robotLivraison.setColis(null);
                    robotLivraison.setDestination(null);
                    destinationPoint = null;
                    updateGrid();
                }

                return;
            }

            int nextX = robotLivraison.getX();
            int nextY = robotLivraison.getY();

            // Calculate next step
            if (nextX != dest.x)
                nextX += Integer.signum(dest.x - nextX);
            else if (nextY != dest.y)
                nextY += Integer.signum(dest.y - nextY);

            // Move robot
            robotLivraison.deplacer(nextX, nextY);

            // Update UI
            updateGrid();
            energieLabel.setText("Énergie actuelle : " + robotLivraison.getEnergie() + "%");

            // Energy warnings
            if (robotLivraison.getEnergie() < 30)
                energieLabel.setForeground(Color.ORANGE);
            if (robotLivraison.getEnergie() < 10)
                energieLabel.setForeground(Color.RED);
            if (robotLivraison.getEnergie() == 0) {
                JOptionPane.showMessageDialog(null, "Batterie vide. Robot arrêté.");
            }
        });

        timer.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceRobotLivraison().setVisible(true));
    }
}