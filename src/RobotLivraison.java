/* f classe hethi les methode deplacer w eli baadha yetkhdmou (énnoné) */

public class RobotLivraison extends RobotConnecte {

    // Constantes d'énergie
    protected static final int ENERGIE_LIVRAISON = 15;
    protected static final int ENERGIE_CHARGEMENT = 5;

    protected String colisActuel ;
    protected String destination ;
    protected boolean enlivraison ;

    //constructeur AVEC paramétres
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
    public  void effectuertacher(){ }
}
