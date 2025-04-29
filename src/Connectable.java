public interface Connectable {

    void connecter(String reseau) throws Exception;// exception lezem nrodoha robot exception
    void deconnecter();
    void envoyerDonnees(String donnees) throws Exception;//kif eli fou9ha
}
