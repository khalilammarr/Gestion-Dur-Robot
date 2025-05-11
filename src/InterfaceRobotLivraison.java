import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class InterfaceRobotLivraison extends JFrame {
    private final int GRID_SIZE = 6;
    private JButton[][] gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
    private HashMap<String, Point> destinationMap = new HashMap<>();
    private String destinationName = null;
    private Point destinationPoint = null;
    private String colis = null;
    private int energie = 100;
    private JProgressBar energieBar;
    private ImageIcon robotImage;
    private RobotLivraison robotLivraison;
    public InterfaceRobotLivraison() {
        setTitle("Simulation Robot Livraison");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load and scale robot image
        try {
            robotImage = loadRobotImage("/robot_icon.png"); // Ensure the image is in resources or root
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Image robot non trouvée !");
            robotImage = null;
        }

        // Panel grille
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        char letter = 'A';
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String name = "" + letter + j;
                JButton btn = new JButton(name);
                btn.setBackground(Color.WHITE);
                btn.setFont(new Font("Arial", Font.PLAIN, 12));
                destinationMap.put(name.toUpperCase(), new Point(i, j));
                gridButtons[i][j] = btn;
                gridPanel.add(btn);
            }
            letter++;
        }
        add(gridPanel, BorderLayout.CENTER);

        // Barre d'énergie
        energieBar = new JProgressBar(0, 100);
        energieBar.setValue(energie);
        energieBar.setStringPainted(true);
        energieBar.setForeground(Color.GREEN);
        add(energieBar, BorderLayout.NORTH);

        // Panel de contrôle
        JPanel controlPanel = new JPanel();

        JButton btnCreer = new JButton("Créer Robot");
        JButton btnCharger = new JButton("Charger Colis");
        JButton btnLivrer = new JButton("Faire Livraison");
        JButton btnDemarrer = new JButton("Démarrer");
        JButton btnArreter = new JButton("Arrêter");
        JButton btnConfig = new JButton("Configuration");

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
                    energieBar.setValue(energie);
                    energieBar.setForeground(Color.GREEN);
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
            panel.add(new JLabel("Destination (ex: B2) :"));
            panel.add(fieldDest);

            int result = JOptionPane.showConfirmDialog(this, panel, "Charger Colis", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                robotLivraison.setColis(fieldColis.getText());
                robotLivraison.setDestination( fieldDest.getText().toUpperCase());
                if (destinationMap.containsKey(robotLivraison.getDestination())) { // nbadlouha b lista nchoufou mawjouda fel lista wala le
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
            robotLivraison.getHistorique().forEach(entry -> area.append(entry + "\n"));
            area.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setPreferredSize(new Dimension(400, 200));

            JButton rechargeBtn = new JButton("Recharger");
            rechargeBtn.addActionListener(ev -> {
                robotLivraison.setEnergie(100);
                energieBar.setValue(energie);
                energieBar.setForeground(Color.GREEN);
                robotLivraison.ajouterHistorique("Batterie rechargée à 100%");
                updateGrid();
            });

            JPanel configPanel = new JPanel(new BorderLayout());
            configPanel.add(scrollPane, BorderLayout.CENTER);
            configPanel.add(rechargeBtn, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(this, configPanel, "Configuration", JOptionPane.PLAIN_MESSAGE);
        });

        controlPanel.add(btnCreer);
        controlPanel.add(btnDemarrer);
        controlPanel.add(btnArreter);
        controlPanel.add(btnCharger);
        controlPanel.add(btnLivrer);
        controlPanel.add(btnConfig);
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

    private void consommerEnergie(int quantite) {
        energie = Math.max(0, energie - quantite);
        energieBar.setValue(energie);

        if (energie < 30)
            energieBar.setForeground(Color.ORANGE);
        if (energie < 10)
            energieBar.setForeground(Color.RED);
        if (energie == 0) {
            robotLivraison.setEnMarche(false) ;
            robotLivraison.ajouterHistorique("Robot éteint automatiquement : batterie vide");
            SwingUtilities.invokeLater(() -> {
                updateGrid();
                JOptionPane.showMessageDialog(this, "Batterie vide. Robot arrêté.");
            });
        }
    }

    private void highlightDestination(Point p) {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                gridButtons[i][j].setBackground(Color.WHITE);

        gridButtons[p.x][p.y].setBackground(Color.YELLOW);
        updateGrid();
    }

    private void updateGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridButtons[i][j].setText(gridButtons[i][j].getActionCommand());
                gridButtons[i][j].setIcon(null);
                gridButtons[i][j].setBackground(Color.WHITE);
            }
        }

        if (robotImage != null) {
            gridButtons[robotLivraison.getX()][robotLivraison.getY()].setText("");
            gridButtons[robotLivraison.getX()][robotLivraison.getY()].setIcon(robotImage);
        } else {
            gridButtons[robotLivraison.getX()][robotLivraison.getY()].setText("🤖");
        }

        gridButtons[robotLivraison.getX()][robotLivraison.getY()].setBackground(robotLivraison.isEnMarche() ? Color.CYAN : Color.LIGHT_GRAY);

        if (destinationPoint != null)
            gridButtons[destinationPoint.x][destinationPoint.y].setBackground(Color.YELLOW);
    }

    private void moveRobotTo(Point dest) {
        new Thread(() -> {
            int dx = dest.x - robotLivraison.getX();
            int dy = dest.y - robotLivraison.getY();

            while ((robotLivraison.getX() != dest.x || robotLivraison.getY() != dest.y) && energie > 0 && robotLivraison.isEnMarche()) {
                if (robotLivraison.getX() != dest.x)
                    robotLivraison.setX(robotLivraison.getX() + Integer.signum(dx));
                else if (robotLivraison.getY() != dest.y)
                    robotLivraison.setY(robotLivraison.getY() + Integer.signum(dy));

                SwingUtilities.invokeLater(() -> {
                    updateGrid();
                    consommerEnergie(3);
                    robotLivraison.ajouterHistorique("Déplacement vers (" + robotLivraison.getX() + "," + robotLivraison.getY() + ")");
                });

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (energie > 0 && robotLivraison.isEnMarche()) {
                robotLivraison.ajouterHistorique("Livraison du colis '" + robotLivraison.getColis() + "' à " + robotLivraison.getDestination());
                JOptionPane.showMessageDialog(this, "Livraison du colis \"" + robotLivraison.getColis() + "\" à " + robotLivraison.getDestination() + " effectuée.");
                robotLivraison.setColis(null);
                robotLivraison.setDestination(null) ;
                destinationPoint = null;
                updateGrid();
            }
        }).start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceRobotLivraison().setVisible(true));
    }
}
