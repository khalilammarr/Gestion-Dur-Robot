import exceptions.* ;


import java.util.Scanner;
public class RobotLivraison extends RobotConnecte {
    // Constantes d'énergie
    protected static final int ENERGIE_LIVRAISON = 15;
    protected static final int ENERGIE_CHARGEMENT = 5;
    protected int colisActuel;
    protected String destination;
    protected boolean enlivraison;
    protected String colis;
    public RobotLivraison(int x, int y, String id) {
        super(x, y, id);
        this.colisActuel = 0;
        this.destination = null;
        this.enlivraison = false;
    }
    public String getColis() {
        return colis;
    }

    public void setColis(String colis) {
        this.colis = colis;
    }

    public int getColisActuel() {
        return colisActuel;
    }

    public void setColisActuel(int colisActuel) {
        this.colisActuel = colisActuel;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isEnlivraison() {
        return enlivraison;
    }

    public void setEnlivraison(boolean enlivraison) {
        this.enlivraison = enlivraison;
    }

    @Override
    public void deplacer(int x, int y) {
        double distance = Math.sqrt(Math.pow(x - getX(), 2) + Math.pow(y - getY(), 2));
        if (distance > 100) {
            System.out.println("Déplacement trop long : distance maximale = 100 unités.");
            return;
        }
        try {
            int energieNecessaire = (int) Math.ceil(0.3 * distance);
            verifierMaintenance();
            verifierEnergie(energieNecessaire);
            setX(x);
            setY(y);
            consommerEnergie(energieNecessaire);
            heuresUtilisation += (int) Math.ceil(distance / 10);
            ajouterHistorique("Déplacement vers (" + x + "," + y + ")");
        } catch (EnergieInsuffisanteException | MaintenanceRequiseException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void effectuertacher() throws RobotEnPanneException, EnergieInsuffisanteException {
        Scanner scanner = new Scanner(System.in);

        if (!enMarche) {
            throw new RobotEnPanneException();
        }

        if (enlivraison) {
            System.out.print("Veuillez entrer les coordonnées (X,Y) de la destination : ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            faireLivraison(x,y);
        } else {
            System.out.print("Voulez-vous charger un colis ? (Oui/Non) : ");
            scanner.nextLine(); // pour nettoyer le buffer
            String reponse = scanner.nextLine().trim().toUpperCase();

            while (!reponse.equals("OUI") && !reponse.equals("NON")) {
                System.out.print("Réponse invalide. Répondez par Oui ou Non : ");
                reponse = scanner.nextLine().trim().toUpperCase();
            }

            if (reponse.equals("OUI")) {
                System.out.print("Donnez votre destination : ");
                String destination = scanner.nextLine();
                chargerColis(destination);
            } else {
                ajouterHistorique("En attente de colis.");
            }
        }
    }



    public void faireLivraison(int Destx, int Desty) {
        try {
            int seuilEnergie = IsModeEcologique ? (int)(ENERGIE_LIVRAISON*0.8) :ENERGIE_LIVRAISON ;
            verifierEnergie(seuilEnergie);
            enlivraison = true;
            deplacer(Destx, Desty);
            consommerEnergie(seuilEnergie);
            colisActuel = 0;
            enlivraison = false;
            ajouterHistorique("Livraison terminée à " + destination);
            destination = null;
        } catch (EnergieInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }
    public void chargerColis(String destination) {
        try {
            if (enlivraison || colisActuel != 0) {
                System.out.println("Impossible de charger : déjà en livraison ou colis existant.");
                return;
            }
            int seuilEnergie = IsModeEcologique ? (int)(ENERGIE_LIVRAISON*0.8) :ENERGIE_LIVRAISON ;
            verifierEnergie(seuilEnergie);
            consommerEnergie(seuilEnergie);
            colisActuel = 1;
            this.destination = destination;
            enlivraison = true;
            ajouterHistorique("Colis chargé pour destination : " + destination);
        } catch (EnergieInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }

    public String toString() {
        return String.format("RobotLivraison [ID : %s, Position : (%d,%d), Énergie : %d%%, Heures : %d, Colis : %s, Destination : %s, Connecté : %s]",
                getId(), getX(), getY(), getEnergie(), getHeuresUtilisation(),
                (colisActuel != 0 ? "1" : "0"),
                (destination != null ? destination : "-"),
                (this.connecte ? "Oui" : "Non"));
    }
    }
