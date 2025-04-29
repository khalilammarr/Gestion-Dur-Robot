/* f classe hethi les methode deplacer w eli baadha yetkhdmou (énnoné) */

import java.util.Scanner;

public class RobotLivraison extends RobotConnecte {

    // Constantes d'énergie
    protected static final int ENERGIE_LIVRAISON = 15;
    protected static final int ENERGIE_CHARGEMENT = 5;

    protected String colisActuel ;
    protected String destination ;
    protected boolean enlivraison ;

    public RobotLivraison(int x, int y, String id,int energie) { /* nafs mochkla mtaa el energie lena
        (kima classe eli kbalha noksed)  */
        super(x, y, id, energie);
        this.colisActuel = null ; /* fama mochkla lena : f énnoncé hat valeur par defaut 0 or que heya string
        zid thabet feha */
        this.destination = null ;
        this.enlivraison = false ;
    }





    @Override
    public void deplacer(int x,int y){ }
    @Override
    // lezemha exception :
    public  void effectuertacher(){
        Scanner scanner = new Scanner(System.in);
        if(!enMarche){
            // exception !
        }
        else if(enlivraison){
            System.out.print("Veuillez entrer les coordonnées (X,Y) de la destination : ");
            System.out.print("Donnez X : ");
            int x = scanner.nextInt() ;
            System.out.print("Donnez Y : ");
            int y = scanner.nextInt() ;
            faireLivraison(x,y) ;
        }
        else {
            System.out.print("Est ce que tu veux charger un nouveau colis ? Oui ou Non ?");
            String reponse = scanner.nextLine() ;
            if(!reponse.toUpperCase().equals("OUI") && !reponse.toUpperCase().equals("NON")){
                while (!reponse.toUpperCase().equals("OUI") && !reponse.toUpperCase().equals("NON")){
                    System.out.print("Réponse incorrecte !");
                    System.out.print("Donnez votre réponse : Oui ou Non ? ");
                    reponse = scanner.nextLine() ;
                }
            }
            if((reponse.toUpperCase()).equals("OUI")){
                if(verifierEnergie(5)){ // 5 testahlekhom k tchargi colis
                    String destination ;
                    System.out.print("Donnez votre destanation");
                    destination = scanner.nextLine() ;
                    chargerColis(destination);
                }

            }
            else if((reponse.toUpperCase()).equals("NON")){
                this.ajouterHistorique("En attente de colis");
            }
        }
    }
    public void faireLivraison(int Destx,int Desty){

    }
    public void chargerColis(String destination){

    }
}
