import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Robot {
    //ay attribut privé raditou protect bech najemch nekhdem behom f classe les classe lokhrine
    protected int x;
    protected int y;
    protected String id;
    public int heuresUtilisation=0;
    protected int energie;
    public boolean enMarche=false;
    protected List<String> historiqueActions;
    //normalement naamlou constructeur par defaut ?
    public Robot(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id=id;
        Scanner scanner = new Scanner(System.in);
        this.energie = 100;
        this.historiqueActions=new ArrayList<>();
        this.ajouterHistorique("Robot Créé");/* lena raw mahma ken el robot nafs el jomla f liste mtaa
        historique weli heya nafs mochkelt tostring */
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String getId() {
        return id;
    }
    public int getEnergie() {
        return energie;
    }
    public void ajouterHistorique(String action) {
        //Nappendiw  l wakt wl date wl message eli je maa l fonction lel liste historiqueActions,
    }
    public boolean verifierEnergie(int energierequise){ // badalt type de retour
        if(energierequise>this.energie){
            //on leve une exception : energie insuffisante
        }
        return false; // ila an ya2ti ma youkhalef thelika
    }
    public void verifierMaintenance(){
        if(this.heuresUtilisation>=100){
            //on leve un exception maintenance requise
        }
    }
    public void demarrer(){

        if(energie<10){
            //on leve exception ManqueEnergie
        }
        else{
            enMarche=true;
            // l energie tonkes wala le ya zabri
            ajouterHistorique("Démarrage Du Robot");
        }
    }
    public void arreter(){
        enMarche=false;
        ajouterHistorique("Arret Du Robot");
    }
    public void consommerEnergie(int quantite){
        int temp=energie-quantite;
        if(temp<=0){
            energie=0;
        }
        else{
            energie=temp;
        }
    }
    public void recharger(int quantite){
        if (energie+quantite>100){
            energie=100;
        }
        else{
            energie+=quantite;
        }
    }
    public abstract void deplacer(int x,int y);
    public abstract void effectuertacher();
    public List<String> getHistorique() {
        return historiqueActions;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName()+ " [ID: " + id + // .getSimpleName tejbdlk esm l class l instance
                ", Position: (" + x + "," + y + ")" +
                ", Énergie: " + energie + "%" +
                ", Heures: " + heuresUtilisation + "]";
    } // fazet robot industriel mch shiha lezem nejbdou esm l subclass fama robotlivrasionn , robot connecte w zebi
}
