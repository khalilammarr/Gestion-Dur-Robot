import exceptions.* ;

public abstract class RobotConnecte extends Robot implements Connectable{
    public boolean connecte;
    public String reseauConnecte;
    public RobotConnecte(int x, int y, String id){
        super(x, y, id);
        this.connecte = false;
        this.reseauConnecte = null ;
    }
    public abstract void deplacer(int x,int y);
    public abstract void effectuertacher() throws RobotEnPanneException, EnergieInsuffisanteException;
    @Override
    public void connecter(String reseau){
        if (energie<5){
            System.out.println("La connexion à un réseau nécessite au moins 5% de l'énergie");
        }
        else {
            int seuil = IsModeEcologique ? 2 : 4;
            connecte = true;
            reseauConnecte = reseau;
            consommerEnergie(seuil);
            this.ajouterHistorique("Robot connecte à: "+reseauConnecte);
        }
    }
    @Override
    public void deconnecter(){
        connecte = false;
        this.ajouterHistorique("RobotConnecte deconnecte de "+reseauConnecte);
        reseauConnecte = null;
    }
    @Override
    public void envoyerDonnees(String donnees) throws EnergieInsuffisanteException, RobotNonConnecteException {
        if (!connecte) {
            throw new RobotNonConnecteException("envoyer des données");
        }
        if (energie < 3) {
            throw new EnergieInsuffisanteException("envoyer des données", 3, energie);
        }
        int seuilEnergie = IsModeEcologique ? 1 : 3;
        consommerEnergie(seuilEnergie);
        ajouterHistorique("Données envoyées : " + donnees);
    }

}
