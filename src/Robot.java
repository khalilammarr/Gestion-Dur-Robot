import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import exceptions.* ;


public abstract class Robot {

    protected int x;
    protected int y;
    protected String id;
    public int heuresUtilisation = 0;
    protected int energie;
    public boolean enMarche = false;
    protected List<String> historiqueActions;

    public Robot(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
        Scanner scanner = new Scanner(System.in);
        this.energie = 100;
        this.historiqueActions = new ArrayList<>();
        this.ajouterHistorique("Robot Créé");
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    public int getEnergie() {
        return energie;
    }

    public int getHeuresUtilisation() {
        return heuresUtilisation;
    }

    public List<String> getHistoriqueActions() {
        return historiqueActions;
    }

    public void ajouterHistorique(String action) {
        String log = "[" + java.time.LocalDateTime.now() + "] " + action;
        historiqueActions.add(log);
    }

    public boolean verifierEnergie(int energieRequise) throws EnergieInsuffisanteException {
        if (energieRequise > this.energie) {
            throw new EnergieInsuffisanteException(energieRequise, this.energie);
        }
        return true;
    }

    public void verifierMaintenance() throws MaintenanceRequiseException {
        if (this.heuresUtilisation >= 100) {
            throw new MaintenanceRequiseException(this.heuresUtilisation);
        }
    }

    public void demarrer() throws EnergieInsuffisanteException {
        if (energie < 10) {
            throw new EnergieInsuffisanteException("démarrer le robot", 10, energie);
        }
        enMarche = true;
        ajouterHistorique("Démarrage du robot");
    }

    public void arreter() {
        enMarche = false;
        ajouterHistorique("Arret Du Robot");
    }

    public void consommerEnergie(int quantite) {
        int temp = energie - quantite;
        if (temp <= 0) {
            energie = 0;
        } else {
            energie = temp;
        }
    }

    public void recharger(int quantite) {
        if (energie + quantite > 100) {
            energie = 100;
        } else {
            energie += quantite;
        }
    }

    public abstract void deplacer(int x, int y);

    public abstract void effectuertacher() throws RobotEnPanneException, EnergieInsuffisanteException;

    public List<String> getHistorique() {
        return historiqueActions;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [ID: " + id +
                ", Position: (" + x + "," + y + ")" +
                ", Énergie: " + energie + "%" +
                ", Heures: " + heuresUtilisation + "]";
    }
}
