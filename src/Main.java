import exceptions.EnergieInsuffisanteException;
import exceptions.RobotEnPanneException;

import java.util.Scanner;

public class Main {

    private static RobotLivraison robot;

    public static void main(String[] args) throws EnergieInsuffisanteException, RobotEnPanneException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            afficherMenu();

            int choix = scanner.nextInt();

            switch (choix) {
                case 0: creerRobot(scanner); break;
                case 1: demarrerRobot(); break;
                case 2: chargerColis(); break;
                case 3: effectuerTache(); break;
                case 4: deplacerRobot(scanner); break;
                case 5: afficherEtatRobot(); break;
                case 6: afficherHistorique(); break;
                case 7: rechargerEnergie(); break;
                case 8: eteindreRobot(); break;
                case 9: System.out.println("Au revoir!"); return;
                case 10: activerModeEconomique(); break;
                case 11: desactiverModeEconomique(); break;
                case 12: nettoyerZone(); break;
                case 13: autoNettoyage(); break;
                case 14: emettreSonRobot(); break;
                default: System.out.println("Option invalide. Essayez encore.");
            }

        }
    }

    public static void afficherMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 🤖 MENU DU ROBOT LIVRAISON                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║  0. 🆕 Créer un robot            │  1. 🔑 Démarrer le robot            ║");
        System.out.println("║  2. 📦 Charger un colis          │  3. 🛠️  Effectuer une tâche         ║");
        System.out.println("║  4. 🧭 Déplacer le robot         │  5. 📊 Afficher l'état              ║");
        System.out.println("║  6. 📜 Afficher l'historique     │  7. 🔋 Recharger l'énergie          ║");
        System.out.println("║  8. 📴 Éteindre le robot          │  9. ❌ Quitter                      ║");
        System.out.println("║ 10. 🌱 Activer mode économique   │ 11. 🚫 Désactiver mode économique   ║");
        System.out.println("║ 12. 🧹 Nettoyer la zone          │ 13. 🧽 Nettoyer le robot (auto)     ║");
        System.out.println("║ 14. 🔊 Émettre un son            │                                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.print("👉 Choisissez une option : ");
    }



    public static void creerRobot(Scanner scanner) {
        System.out.print("Entrez l'ID du robot : ");
        String id = scanner.next();
        System.out.print("Entrez les coordonnées X du robot : ");
        int x = scanner.nextInt();
        System.out.print("Entrez les coordonnées Y du robot : ");
        int y = scanner.nextInt();

        robot = new RobotLivraison(x, y, id);
        System.out.println("Robot créé avec succès!");
    }

    public static void nettoyerZone() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        try {
            robot.nettoyer();
            System.out.println("Zone nettoyée !");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void autoNettoyage() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        try {
            robot.seNettoyer();
            System.out.println("Robot nettoyé !");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void emettreSonRobot() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        robot.emettreSonPersonnalise();
    }


    public static void activerModeEconomique() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        robot.activerModeEconomic();
        System.out.println("Mode économique activé !");
    }

    public static void desactiverModeEconomique() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        robot.DesactiverModeEconomic();
        System.out.println("Mode économique désactivé !");
    }


    public static void demarrerRobot() {
        try {
            if (robot == null) {
                System.out.println("Vous devez d'abord créer un robot.");
                return;
            }
            robot.demarrer();
            System.out.println("Robot démarré!");
        } catch (EnergieInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void chargerColis() throws EnergieInsuffisanteException, RobotEnPanneException {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        robot.effectuertacher();
    }

    public static void effectuerTache() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        try {
            robot.effectuertacher();
        } catch (RobotEnPanneException | EnergieInsuffisanteException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deplacerRobot(Scanner scanner) {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }

        System.out.print("Entrez la coordonnée X pour déplacer le robot : ");
        int x = scanner.nextInt();
        System.out.print("Entrez la coordonnée Y pour déplacer le robot : ");
        int y = scanner.nextInt();

        robot.deplacer(x, y);
    }

    public static void afficherEtatRobot() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        System.out.println(robot);
    }

    public static void afficherHistorique() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }
        for (String action : robot.getHistorique()) {
            System.out.println(action);
        }
    }

    public static void rechargerEnergie() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }

        System.out.print("Entrez la quantité d'énergie à ajouter : ");
        Scanner scanner = new Scanner(System.in);
        int quantite = scanner.nextInt();
        robot.recharger(quantite);
        System.out.println("Robot rechargé!");
    }

    public static void eteindreRobot() {
        if (robot == null) {
            System.out.println("Vous devez d'abord créer un robot.");
            return;
        }

        robot.arreter();
        System.out.println("Robot éteint!");
    }
}
