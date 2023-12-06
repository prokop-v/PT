/**
 * Sablona pro vytvareni vypisu
 * @author Vaclav Prokop, Filip Valtr
 */
public class Vypis implements Comparable<Vypis>{

//== Atributy instanci
    /**
     * text, ktery se ma vypsat do konzole
     */
    private final String vypis;
    /**
     * cas, kdy se ma text vypsat
     */
    private final double time;
    /**
     * uklada informaci o tom, o ktere kolecko se jedna
     */
    private final int poradiKolecka;
    /**
     * zjistuje, jestli pytel vylozil pytle
     */
    private boolean bylyPytleVylozeny = false;
    /**
     * poradi pozadavku
     */
    private final int poradiPozadavku;
    /**
     * slouzi k vypisu navratu do skladu ve spravnem poradi
     */
    private boolean vypisKoleckaNaSklade = false;
    /**
     *Uchovava celkovy pocet ulozenych pytlu
     */
    private int pocetVylozenychPytlu = 0;

    /**
     * je pro serazeni umrznuti
     */
    private final boolean umrzl;
    /**
     * je pro serazeni opravy
     */
    private final boolean oprava;
    /**
     * je pro serazeni prvniho vypisu
     */
    private final boolean prvniVypis;
    /**
     * je pro serazeni odjezdu
     */
    private final boolean odjezd;
    /**
     * je pro serazeni vylozeni
     */
    private final boolean vylozeni;
    /**
     * je pro serazeni navratu do skladu
     */
    private final boolean navratDoSkladu;

//== Konstruktor

    /**
     * Konstruktor vytvarejici vypisy na konzoli
     * @param time (double)
     * @param vypis (String)
     * @param umrzl (boolean)
     * @param poradiPozadavku (int)
     * @param poradiKolecka (int)
     * @param prvniVypis (boolean)
     * @param oprava (boolean)
     * @param odjezd (boolean)
     * @param vylozeni (boolean)
     * @param navratDoSkladu (boolean)
     */
    public Vypis(double time, String vypis, boolean umrzl, int poradiPozadavku, int poradiKolecka, boolean prvniVypis, boolean oprava, boolean odjezd,
                 boolean vylozeni, boolean navratDoSkladu){
        this.vypis = vypis;
        this.time = time;
        this.poradiKolecka = poradiKolecka;
        this.poradiPozadavku = poradiPozadavku;
        this.umrzl = umrzl;
        this.prvniVypis = prvniVypis;
        this.oprava = oprava;
        this.odjezd = odjezd;
        this.vylozeni = vylozeni;
        this.navratDoSkladu = navratDoSkladu;
    }

    //== Gettery a Settery

    /**
     * Vrati cas, kdy ma byt vypis vypsan
     * @return time (double)
     */
    public double getTime() {
        return time;
    }

    /**
     * Vrati true, nebo false, podle toho, jestli byly pytle vylozeny
     * @return true/false
     */
    public boolean bylyPytleVylozeny() {
        return bylyPytleVylozeny;
    }

    /**
     * Nastavi, jestli byly pytle vylozeny na true/false
     * @param bylyPytleVylozeny (boolean)
     */
    public void setBylyPytleVylozeny(boolean bylyPytleVylozeny) {
        this.bylyPytleVylozeny = bylyPytleVylozeny;
    }

    /**
     * Nastavi, jestli se jedna o vypis navratu na sklad
     * @param vypisKoleckaNaSklade (boolean)
     */
    public void setVypisKoleckaNaSklade(boolean vypisKoleckaNaSklade) {
        this.vypisKoleckaNaSklade = vypisKoleckaNaSklade;
    }

    /**
     * zjisti pocet vylozenych pytlu
     * @return (int)
     */
    public int getPocetVylozenychPytlu() {
        return pocetVylozenychPytlu;
    }

    /**
     * nastavi pocet vylozenych pytlu
     * @param pocetVylozenychPytlu (int)
     */
    public void setPocetVylozenychPytlu(int pocetVylozenychPytlu) {
        this.pocetVylozenychPytlu = pocetVylozenychPytlu;
    }

    /**
     * compareTo metoda pro porovnavani casy vypisu v prioritni fronte a nasledne jejich serazeni
     * @param v objekt, se kterym se porovnava
     * @return (int)
     */
    @Override
    public int compareTo(Vypis v) {
        if (this.time > v.time) {//pokud cas prvniho vypisu je vetsi nez cas v tak je prvni vetsi
            return 1;
        } else if (this.time < v.time) {//kdyz je cas prvniho mensi tak je mensi
            return -1;
        } else{ //pokud se rovnaji
            if(this.poradiPozadavku > v.poradiPozadavku){
                return 1;
            }
            else if (this.poradiPozadavku < v.poradiPozadavku){
                return -1;
            } else {
                if (this.poradiKolecka > v.poradiKolecka) {
                    return 1;
                } else if (this.poradiKolecka < v.poradiKolecka) {
                    return -1;
                } else {
                    if (this.umrzl && !v.umrzl) {
                        return 1;
                    } else if (v.umrzl && !this.umrzl) {
                        return -1;
                    } else {
                        if(this.prvniVypis && !v.prvniVypis){
                            return -1;
                        }
                        else if (v.prvniVypis && !this.prvniVypis){
                            return 1;
                        }
                        else{
                            if(this.oprava && !v.oprava){
                                return -1;
                            }
                            else if (v.oprava && !this.oprava){
                                return 1;
                            }
                            else{
                                if(this.odjezd && !v.odjezd){
                                    return -1;
                                }
                                else if (v.odjezd && !this.odjezd){
                                    return 1;
                                }
                                else{
                                    if(this.vylozeni && !v.vylozeni){
                                        return -1;
                                    }
                                    else if (v.vylozeni && !this.vylozeni){
                                        return 1;
                                    }
                                    else{
                                        if(this.navratDoSkladu && !v.navratDoSkladu){
                                            return -1;
                                        }
                                        else if (v.navratDoSkladu && !this.navratDoSkladu){
                                            return 1;
                                        }
                                        else{
                                            return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * vrati vypis
     */
    @Override
    public String toString() {
        return vypis;
    }
}
