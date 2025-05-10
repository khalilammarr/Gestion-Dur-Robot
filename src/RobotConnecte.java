/*normalement hasb marit ce robot mech bech ydeplaci w mech chyaamel des taches donc
les fonction deplacer et effectuerraszebi chyabkaw abstract (zid rakez maa el énnoncé mta robot hetha
w robot eli baadou )*/

import exceptions.* ;

public abstract class RobotConnecte extends Robot implements Connectable{
    public boolean connecte;
    public String reseauConnecte;
    public RobotConnecte(int x, int y, String id){ /* majbedch aal energie f constructeur ama
        lezem nhotha ken bech nkhaliw el constructeur mtaa classe robot akeka */
        super(x, y, id);
        this.connecte = false;
        this.reseauConnecte = null ;
    }
    public abstract void deplacer(int x,int y);
    public abstract void effectuertacher();
    @Override
    public void connecter(String reseau){
        if (energie<5){
            System.out.println("La connexion à un réseau nécessite au moins 5% de l'énergie");
        }
        else {
            connecte = true;
            reseauConnecte = reseau;
            consommerEnergie(5);
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
    public void envoyerDonnees(String donnees) throws EnergieInsuffisanteException, RobotNonConnecteException {
        if (!connecte) {
            throw new RobotNonConnecteException("envoyer des données");
        }
        if (energie < 3) {
            throw new EnergieInsuffisanteException("envoyer des données", 3, energie);
        }
        consommerEnergie(3);
        ajouterHistorique("Données envoyées : " + donnees);
    }

}
