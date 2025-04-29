/*normalement hasb marit ce robot mech bech ydeplaci w mech chyaamel des taches donc
les fonction deplacer et effectuerraszebi chyabkaw abstract (zid rakez maa el énnoncé mta robot hetha
w robot eli baadou )*/

public abstract class RobotConnecte extends Robot implements Connectable{
    public boolean connecte;
    public String reseauConnecte;


    public RobotConnecte(int x, int y, String id,int energie){ /* majbedch aal energie f constructeur ama
        lezem nhotha ken bech nkhaliw el constructeur mtaa classe robot akeka */
        super(x, y, id, energie);
        this.connecte = false;
        this.reseauConnecte = null ;
    }


    public abstract void deplacer(int x,int y);
    public abstract void effectuertacher();
    @Override
    public void connecter(String reseau){
        if (energie<5){
            System.out.println("L'énergie doit être >= 5% pour se connecter");
        }
        else {
            connecte = true;
            reseauConnecte = reseau;
            energie-=5;
            this.ajouterHistorique("Robot connecte à: "+reseauConnecte);
        }
    }
    @Override
    public void deconnecter(){
        connecte = false;
        reseauConnecte = null;
        this.ajouterHistorique("RobotConnecte deconnecte de "+reseauConnecte);
    }
    @Override
    public void envoyerDonnees(String donnees) throws Exception {
        if (!connecte) {
            throw new Exception("Le robot n'est pas connecté.");//exception twali robotexception eli bech naamloha felekher
        } else {
            if (energie < 3) {
                System.out.println("L'énergie doit être >= 3% pour se connecter");
            } else {
                energie -= 3;
                this.ajouterHistorique("Données envoyées : " + donnees);
            }
        }
    }
}
