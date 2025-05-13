import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.sound.sampled.*;
import exceptions.*;

public abstract class Robot {

    protected int x;
    protected int y;
    protected String id;
    protected int energie;
    protected int heuresUtilisation = 0;
    protected boolean enMarche = false;
    protected boolean IsModeEcologique = false;
    protected List<String> historiqueActions;

    private Timer inactiviteTimer; // Timer pour surveiller l'inactivité

    public Robot(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.energie = 100;
        this.historiqueActions = new ArrayList<>();
        this.ajouterHistorique("Robot Créé");
        demarrerSurveillanceInactivite();  // Démarre la surveillance de l'inactivité
    }

    // Getters & Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public String getId() { return id; }
    public int getEnergie() { return energie; }
    public int getHeuresUtilisation() { return heuresUtilisation; }
    public boolean isEnMarche() { return enMarche; }
    public List<String> getHistoriqueActions() { return historiqueActions; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setId(String id) { this.id = id; }
    public void setEnergie(int energie) { this.energie = Math.min(energie, 100); }
    public void setHeuresUtilisation(int heures) { this.heuresUtilisation = heures; }
    public void setEnMarche(boolean enMarche) { this.enMarche = enMarche; }
    public void setHistoriqueActions(List<String> historique) { this.historiqueActions = historique; }

    // Historique
    public void ajouterHistorique(String action) {
        String log = "[" + java.time.LocalDateTime.now() + "] " + action;
        historiqueActions.add(log);
    }

    // Vérifications
    public boolean verifierEnergie(int energieRequise) throws EnergieInsuffisanteException {
        if (energie < energieRequise) {
            throw new EnergieInsuffisanteException(energieRequise, energie);
        }
        return true;
    }

    public void verifierMaintenance() throws MaintenanceRequiseException {
        if (heuresUtilisation >= 100) {
            throw new MaintenanceRequiseException(heuresUtilisation);
        }
    }

    // Marche/Arrêt
    public void demarrer() throws EnergieInsuffisanteException {
        int seuil = IsModeEcologique ? 8 : 10;
        verifierEnergie(seuil);
        consommerEnergie(seuil);
        enMarche = true;
        ajouterHistorique("Démarrage du robot" + (IsModeEcologique ? " en mode économique" : ""));
        resetInactiviteTimer(); // Réinitialise le timer à chaque démarrage
    }

    public void arreter() {
        enMarche = false;
        ajouterHistorique("Arrêt du robot");
        demarrerSurveillanceInactivite(); // Redémarre la surveillance d'inactivité
    }

    // Nettoyage
    public void nettoyer() throws EnergieInsuffisanteException, RobotEnPanneException {
        if (!enMarche) throw new RobotEnPanneException();
        int cout = IsModeEcologique ? 4 : 6;
        verifierEnergie(cout);
        consommerEnergie(cout);
        ajouterHistorique("Nettoyage à (" + x + "," + y + ")" + (IsModeEcologique ? " [mode économique]" : ""));
        resetInactiviteTimer(); // Réinitialise le timer après une action
    }

    public void seNettoyer() throws EnergieInsuffisanteException, RobotEnPanneException {
        if (!enMarche) throw new RobotEnPanneException();
        int cout = IsModeEcologique ? 2 : 3;
        verifierEnergie(cout);
        consommerEnergie(cout);
        ajouterHistorique("Auto-nettoyage du robot" + (IsModeEcologique ? " [mode économique]" : ""));
        resetInactiviteTimer(); // Réinitialise le timer après une action
    }

    // Rechargement
    public void recharger(int quantite) {
        energie = Math.min(energie + quantite, 100);
        ajouterHistorique("Recharge de " + quantite + "%");
    }

    // Consommation énergie
    public void consommerEnergie(int quantite) {
        energie = Math.max(0, energie - quantite);
    }

    public void chanter() {
        int seuil = IsModeEcologique ? 2 : 4;
        try {
            File fichierSon = new File("son/song.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(fichierSon);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            ajouterHistorique("Son personnalisé émis par le robot");
            consommerEnergie(seuil);
        } catch (Exception e) {
            System.out.println("Erreur audio : " + e.getMessage());
        }
    }

    public void activerModeEcologique() {
        IsModeEcologique = true;
        ajouterHistorique("Mode économique activé");
    }
    public void desactiverModeEcologique() {
        IsModeEcologique = false;
        ajouterHistorique("Mode économique désactivé");
    }
    protected void demarrerSurveillanceInactivite() {
        if (inactiviteTimer != null && inactiviteTimer.isRunning()) {
            inactiviteTimer.stop();
        }

        inactiviteTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!enMarche && IsModeEcologique && energie < 100) {
                    recharger(10); // Recharge de 10% toutes les 5s
                    ajouterHistorique("Recharge automatique (mode éco) suite à inactivité.");
                    System.out.println("Recharge automatique déclenchée (mode éco).");
                    demarrerSurveillanceInactivite(); // Relance la surveillance après la recharge
                }
            }
        });
        inactiviteTimer.setRepeats(false); // Ne se déclenche qu'une fois
        inactiviteTimer.start();
    }

    protected void resetInactiviteTimer() {
        if (inactiviteTimer != null) {
            inactiviteTimer.restart(); // Redémarre le timer à chaque action
        }
    }

    // Abstract
    public abstract void deplacer(int x, int y);
    public abstract void effectuertacher() throws RobotEnPanneException, EnergieInsuffisanteException;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [ID: " + id +
                ", Position: (" + x + "," + y + ")" +
                ", Énergie: " + energie + "%" +
                ", Heures: " + heuresUtilisation + "]";
    }
}
