/* f classe hethi les methode deplacer w eli baadha yetkhdmou (énnoné) */

import exceptions.* ;


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
    public void effectuertacher() {
        Scanner scanner = new Scanner(System.in);
        if (!enMarche) {
            try {
                throw new RobotEnPanneException();
            } catch (RobotEnPanneException e) {
                System.out.println(e.getMessage());
                return;
            }
        }

        if (enlivraison) {
            System.out.print("Entrez les coordonnées (X,Y) de la destination : ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            faireLivraison(x, y);
        } else {
            System.out.print("Charger un nouveau colis ? (Oui/Non) : ");
            scanner.nextLine(); // pour vider le buffer
            String reponse = scanner.nextLine();
            while (!reponse.equalsIgnoreCase("OUI") && !reponse.equalsIgnoreCase("NON")) {
                System.out.print("Réponse incorrecte. Répondez Oui ou Non : ");
                reponse = scanner.nextLine();
            }

            if (reponse.equalsIgnoreCase("OUI")) {
                try {
                    if (verifierEnergie(5)) {
                        System.out.print("Entrez la destination : ");
                        String dest = scanner.nextLine();
                        chargerColis(dest);
                    }
                } catch (EnergieInsuffisanteException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                ajouterHistorique("En attente de colis");
            }
        }
    }


    public void faireLivraison(int Destx, int Desty) {
        try {
            verifierEnergie(ENERGIE_LIVRAISON);
            enlivraison = true;
            deplacer(Destx, Desty);
            consommerEnergie(ENERGIE_LIVRAISON);
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
            verifierEnergie(ENERGIE_CHARGEMENT);
            consommerEnergie(ENERGIE_CHARGEMENT);
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
