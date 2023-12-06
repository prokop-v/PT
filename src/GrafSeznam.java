import java.util.Arrays;
import java.util.*;

/**
 * Sablona pro vytvareni grafu
 * @author Vaclav Prokop, Filip Valtr
 */
class GrafSeznam implements IGraf {
    /**
     * pole linku, ktere mezi sebou spojuje vrcholy a ceny knim prislusne
     */
    public final Link[] hrany;
    /**
     * pole linku, ktere mezi sebou spojuje vrcholy a ceny knim prislusne
     */
    public Link[] shortestPaths;
    /**
     * pole vyslednych cen
     */
    public double[] resultCosts;
    /**
     * Kontruktor grafu, kde do pole ukladame pocet vrcholu daneho grafu a nastavime atributy
     * @param pocetVrcholu (int)
     */
    public GrafSeznam(int pocetVrcholu) {
        this.hrany = new Link[pocetVrcholu];
        this.shortestPaths = new Link[pocetVrcholu];
        this.resultCosts = new double[pocetVrcholu];
    }

    /**
     * Vypocita danou vzdalenost mezi dvema vrcholu, ktery mezi sebou maji hranu
     * @param x1 (x souradnice 1. vrcholu)
     * @param x2 (x souradnice 2. vrcholu)
     * @param y1 (y souradnice 1. vrcholu)
     * @param y2 (y souradnice 2. vrcholu)
     * @return vysledek (double)
     */
    public double vzdalenost(double x1, double x2, double y1, double y2) {
        double xDelka = x2 - x1;
        double yDelka = y2 - y1;
        return Math.sqrt((xDelka * xDelka) + (yDelka * yDelka));
    }
    /**
     * Prida hranu tak ze napoji hranu linku na hrany 
     * @param i (int) i vrchol prvni v hrane
     * @param j (int) j vrchol druhy v hrane
     * @param cena (double) cena hrany
     */
    public void pridejHranu(int i, int j, double cena) {
        hrany[i] = new Link(j, hrany[i], cena);
        hrany[j] = new Link(i, hrany[j], cena);
    }
    /**
     * Dijkstruv algoritmus, ktery vypocita z jednoho vrcholu nejkratsi cesty ke vsem ostatnim vrcholum a 
     * ziska seznam vrcholu v ceste ke kazdemu vrcholu a k nim vrati pole cen
     * @param graf,  graf ve kterem chceme hledat (Link[])
     * @param start, vrchol, ze ktereho chceme hledat nejkratsi cesty (int)
     * @param nejkratsiCesty, seznam vrcholu v ceste ke kazdemu vrcholu
     * @return vzdalenosti (double[]) vracejici pole s nejkratsimi cestami
     */
    public double[] dijkstra(Link[] graf, int start, List<List<VrcholCena>> nejkratsiCesty) {
        int pocetVrcholu = graf.length;
        boolean[] navstiveny = new boolean[pocetVrcholu]; //vytvori pole, kde zjistujeme, jestli dany vrchol byl jiz navstiven
        double[] vzdalenost = new double[pocetVrcholu]; //vytvori pole vzdalenosti, kde si ukladame ceny k vrcholum
        int[] predchudce = new int[pocetVrcholu]; //vytvori pole predchudcu, kde si ukladame predchudce vrcholu (vrcholi v ceste)

        Arrays.fill(vzdalenost, Double.POSITIVE_INFINITY);//na zacatku nastavime vzdalenosti na nekonecno
        Arrays.fill(predchudce, -1); // Na začátku nemáme žádné předchůdce.

        vzdalenost[start] = 0.0; //vzdalenost do stejnyho vrcholu je vzdy 0

        for (int i = 0; i < pocetVrcholu; i++) {
            double minVzdalenost = Double.POSITIVE_INFINITY; //nastavime pro nasledne porovnani s min prvkem
            int minVrchol = 0; //minimalni prvek

            for (int vrchol = 0; vrchol < pocetVrcholu; vrchol++) { //postupujeme postupne pres vsechny vrcholy
                if (!navstiveny[vrchol] && vzdalenost[vrchol] < minVzdalenost) { //pokud nebyl navstiveny a vzdalenost k vrcholu
                    minVzdalenost = vzdalenost[vrchol]; //je mensi nez minvzdalenost, nastavime do minvzdalenosti tuto vzdalenost
                    minVrchol = vrchol; //ulozime index nejnizsiho vrcholu
                }
            }

            navstiveny[minVrchol] = true; //nastavime, ze jsme ho jiz navstivili
            //Link ktery se nachazi v grafu (Linku[]) na minvrcholu prohlasime za sousedniLink a pomoci napojovani (next) jedeme pres vsechny jeho sousedy 
            for (Link sousedniLink = graf[minVrchol]; sousedniLink != null; sousedniLink = sousedniLink.next) { 
                int vrchol = sousedniLink.soused; //do vrcholu si ulozime souseda
                // do noveCeny si ulozime vzdalenost z min vrcholu a pricteme cenu, ktera vede k aktualnimu vrcholu napojeneho na Link[minVrchol] 
                //(tzn. cena cesty k jednomu z vrcholu nachazejicich se v ceste)
                double novaCena = vzdalenost[minVrchol] + sousedniLink.cena;  
                if (novaCena < vzdalenost[vrchol]) {//Pokud je tato cena mensi tak aktualizujeme vzdalenosti a predchudce
                    vzdalenost[vrchol] = novaCena;	//Aktualizace ceny (vzdalenosti)
                    predchudce[vrchol] = minVrchol; // Aktualizace předchůdce pro novou nejkratší cestu.
                }
            }
        }

        // Sledování nejkratších cest:
        for (int cilovyVrchol = 0; cilovyVrchol < pocetVrcholu; cilovyVrchol++) {
            List<VrcholCena> cesta = new ArrayList<>(); //list pro vrcholy vyskytujici se po nejkratsi ceste
            int aktVrchol = cilovyVrchol;

            while (aktVrchol != -1) { //dokud aktualni vrchol ma predchudce
            	//pridame do seznamu index vrcholu (v prvni pruchodu cilovyVrchol) a ceny cesty k nemu
            	cesta.add(new VrcholCena(aktVrchol, vzdalenost[aktVrchol]));
            	//jdeme po predchudcich v ceste neboli aktualni vrchol se rovna predchudcovi aktualnimu vrcholu
            	aktVrchol = predchudce[aktVrchol];
            }
            //prevratime cestu
            Collections.reverse(cesta);
            //Pro kazdy vrchol priradi cestu (cestu s vrcholi k nemu)
            nejkratsiCesty.add(cesta);
        }
        //vrati pole cen
        return vzdalenost;
    }
}