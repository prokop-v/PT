import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Sablona pro vytvareni grafu
 * @author Vaclav Prokop, Filip Valtr
 */

public class GrafMatice implements IGraf {

//== Atributy instanci

    /**
     * Dvojrozmerne pole ukladajici sousednosti v grafu
     */
    private final double[][] pole;

    /**
     * paramentr obsahujici nekonecno
     */
    private static final double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;

    /**
     * Kontruktor grafu, kde do pole ukladame pocet vrcholu daneho grafu a nastavime hodnoty
     * sousednosti na 0
     *
     * @param pocetVrcholu (int)
     */
    public GrafMatice(int pocetVrcholu) {
        this.pole = new double[pocetVrcholu][pocetVrcholu];
        for (int i = 0; i < pole.length; i++) {
            for (int j = 0; j < pole[i].length; j++) {
                pole[i][j] = -1;
            }
        }
    }
    /**
     * Metoda pridejHranu mezi vrcholy prida neorientovanou hranu s danou cenou cesty
     * @param i    (int)
     * @param j    (int)
     * @param cena (double)
     */
    public void pridejHranu(int i, int j, double cena) {
        pole[i][j] = cena;
        pole[j][i] = cena;
    }
    /**
     * Vypocita danou mezi dvema vrcholu, ktery mezi sebou maji hranu
     *
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
     * Vrati pole, ktere obsahuje vypoctene cesty
     *
     * @return pole (double[][])
     */
    public double[][] getPole() {
        return pole;
    }
        
    /**
     * Dijkstruv algoritmus, ktery vypocita z jednoho vrcholu nejkratsi cesty ke vsem ostatnim vrcholum a 
     * ziska seznam vrcholu v ceste ke kazdemu vrcholu a k nim vrati pole cen
     * @param graf,  graf ve kterem chceme hledat (double[][])
     * @param start, vrchol, ze ktereho chceme hledat nejkratsi cesty (int)
     * @param nejkratsiCesty, seznam vrcholu v ceste ke kazdemu vrcholu
     * @return vzdalenosti (double[]) vracejici pole s nejkratsimi cestami
     */
    public double[] dijkstra2(double[][] graf, int start, List<List<VrcholCena>> nejkratsiCesty) {
        int pocetVrcholu = graf.length;
        boolean[] navstiveny = new boolean[pocetVrcholu]; //vytvori pole, kde zjistujeme, jestli dany vrchol byl jiz navstiven
        double[] vzdalenosti = new double[pocetVrcholu]; //vytvori pole vzdalenosti, kde si ukladame ceny k vrcholum
        int[] predchudci = new int[pocetVrcholu]; //vytvori pole predchudcu, kde si ukladame predchudce vrcholu (vrcholi v ceste)

        Arrays.fill(vzdalenosti, POSITIVE_INFINITY);//na zacatku nastavime vzdalenosti na nekonecno
        Arrays.fill(predchudci, -1); //naplnime predchudce -1

        vzdalenosti[start] = 0.0;//vzdalenost do stejnyho vrcholu je vzdy 0

        for (int i = 0; i < pocetVrcholu; i++) {
            double minVzdalenost = POSITIVE_INFINITY;//nastavime pro nasledne porovnani s min prvkem
            int minVrchol = 0;//minimalni prvek

            for (int vrchol = 0; vrchol < pocetVrcholu; vrchol++) {//postupujeme postupne pres vsechny vrcholy
                if (!navstiveny[vrchol] && vzdalenosti[vrchol] < minVzdalenost) {//pokud nebyl navstiveny a vzdalenost k vrcholu
                    minVzdalenost = vzdalenosti[vrchol];//je mensi nez minvzdalenost, nastavime do minvzdalenosti tuto vzdalenost
                    minVrchol = vrchol;//ulozime index nejnizsiho vrcholu
                }
            }

            navstiveny[minVrchol] = true;//nastavime, ze jsme ho jiz navstivili

            for (int vrchol = 0; vrchol < pocetVrcholu; vrchol++) {
                if (!navstiveny[vrchol] && graf[minVrchol][vrchol] != -1) {//pokud prvek nebyl navstiven a v grafu existuje cesta mezi vrcholy
                    double novaCena = vzdalenosti[minVrchol] + graf[minVrchol][vrchol];//potom prictem k minvrcholu i cenu k dalsimu vrcholu
                    if (novaCena < vzdalenosti[vrchol]) {//pokud je tato nova cena levnejsi, pak ji zamenime
                        vzdalenosti[vrchol] = novaCena;//nejnizsi ceny ulozime do pole vzdalenosti
                        predchudci[vrchol] = minVrchol;//predchudce ulozime do pole predchudcu
                    }
                }
            }
        }

        //Sleduje cesty a jejich ceny
        for (int cilovyVrchol = 0; cilovyVrchol < pocetVrcholu; cilovyVrchol++) {
            List<VrcholCena> cesta = new ArrayList<>(); //list pro vrcholy vyskytujici se po nejkratsi ceste
            int aktualniVrchol = cilovyVrchol; 

            while (aktualniVrchol != -1) { //dokud aktualni vrchol ma predchudce
            	//pridame do seznamu index vrcholu (v prvnim pruchodu cilovyVrchol) a ceny cesty k nemu
            	cesta.add(new VrcholCena(aktualniVrchol, vzdalenosti[aktualniVrchol]));
            	//jdeme po predchudcich v ceste neboli aktualni vrchol se rovna predchudcovi aktualnimu vrcholu
                aktualniVrchol = predchudci[aktualniVrchol];
            }
            //prevratime cestu
            Collections.reverse(cesta);
            //Pro kazdy vrchol priradi cestu (cestu s vrcholi k nemu)
            nejkratsiCesty.add(cesta);
        }
        //vrati pole cen
        return vzdalenosti;
    }
}