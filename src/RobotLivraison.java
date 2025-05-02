/* f classe hethi les methode deplacer w eli baadha yetkhdmou (énnoné) */
import java.util.Scanner;
public class RobotLivraison extends RobotConnecte {
    // Constantes d'énergie
    protected static final int ENERGIE_LIVRAISON = 15;
    protected static final int ENERGIE_CHARGEMENT = 5;
    protected int colisActuel;
    protected String destination;
    protected boolean enlivraison;

    public RobotLivraison(int x, int y, String id) {
        super(x, y, id);
        this.colisActuel = 0;
        this.destination = null;
        this.enlivraison = false;
    }

    @Override
    public void deplacer(int x, int y) {
        double distance = Math.sqrt(Math.pow(x - this.getX(), 2) + Math.pow(y - this.getY(), 2));
        if (distance > 100) {
            //leve exception ya zabri
        }
        int energieNecessaire = (int) Math.ceil(0.3 * distance);//ceil tkaber fama round tsagher, ama nrmlment hachetna bih akber
        this.verifierMaintenance();
        this.verifierEnergie(energieNecessaire);
        this.setX(x);
        this.setY(y);
        this.consommerEnergie(energieNecessaire);
        heuresUtilisation += (int) Math.ceil(distance / 10);
        ajouterHistorique("Déplacement vers (" + x + "," + y + ")");
    }

    @Override
    // lezemha exception :
    public void effectuertacher() {
        Scanner scanner = new Scanner(System.in);
        if (!enMarche) {
            // exception !
        } else if (enlivraison) {
            System.out.print("Veuillez entrer les coordonnées (X,Y) de la destination : ");
            System.out.print("Donnez X : ");
            int x = scanner.nextInt();
            System.out.print("Donnez Y : ");
            int y = scanner.nextInt();
            faireLivraison(x, y);
        } else {
            System.out.print("Est ce que tu veux charger un nouveau colis ? Oui ou Non ?");
            String reponse = scanner.nextLine();
            if (!reponse.toUpperCase().equals("OUI") && !reponse.toUpperCase().equals("NON")) {
                while (!reponse.toUpperCase().equals("OUI") && !reponse.toUpperCase().equals("NON")) {
                    System.out.print("Réponse incorrecte !");
                    System.out.print("Donnez votre réponse : Oui ou Non ? ");
                    reponse = scanner.nextLine();
                }
            }
            if ((reponse.toUpperCase()).equals("OUI")) {
                if (verifierEnergie(5)) {
                    String destination;
                    System.out.print("Donnez votre destanation");
                    destination = scanner.nextLine();
                    chargerColis(destination);
                }
            } else if ((reponse.toUpperCase()).equals("NON")) {
                this.ajouterHistorique("En attente de colis");
            }
        }
    }

    public void faireLivraison(int Destx, int Desty) {
        verifierEnergie(ENERGIE_LIVRAISON);
        enlivraison = true;
        deplacer(Destx, Desty);
        consommerEnergie(ENERGIE_LIVRAISON);
        colisActuel = 0;
        enlivraison = false;
        ajouterHistorique("Livraison terminée à " + destination);
        destination = null;
    }

    public void chargerColis(String destination) {
        if (enlivraison) {
            //asba
        }
        if (colisActuel != 0) {
            //aasba lik ya sabri
        }
            verifierEnergie(ENERGIE_CHARGEMENT);
            consommerEnergie(ENERGIE_CHARGEMENT);
            colisActuel = 1;
            this.destination = destination;
            enlivraison = true;
            ajouterHistorique("Colis chargé pour destination : " + destination);
        }
    public String toString() {
        return String.format("RobotLivraison [ID : %s, Position : (%d,%d), Énergie : %d%%, Heures : %d, Colis : %s, Destination : %s, Connecté : %s]",
                getId(), getX(), getY(), getEnergie(), getHeuresUtilisation(),
                (colisActuel != 0 ? "1" : "0"),
                (destination != null ? destination : "-"),
                (this.connecte ? "Oui" : "Non"));
    }
    }
