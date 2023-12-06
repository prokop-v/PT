import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 * Hlavni trida programu obsahujici celou simulaci a zpracovani souboru
 * @author Vaclav Prokop, Filip Valtr
 */
public class Main {
//== Tridni atributy

    /**
     * uklada index aktualne pouzivaneho skladu
     */
    public static int k;

    /**
     * pouziva se jako index pri zpracovavani souboru
     */
    public static int i;
    /**
     * cas ukonceni simulace
     */
    public static double end = POSITIVE_INFINITY;
    /**
     * cas simulace
     */
    public static double time;
    /**
     * uklada si data ze souboru, abychom s nimi mohli dale pracovat
     */
    public static String[] poleStringu = new String[20];
    /**
     * obsahuje pozadavky, ktere maji byt zpracovany
     */
    public static Pozadavek[] polePozadavku;
    /**
     * obsahuje pole skladu, ktere mame k dispozici
     */
    public static Sklad[] poleSkladu;
    /**
     * obsahuje pole zakazniku, ktere mame obsluhovat
     */
    public static Zakaznik[] poleZakazniku;
    /**
     * obsahuje pole kolecek, ktere mohou dovazet
     */
    public static DruhKolecka[] poleKolecek;
    /**
     * obsahuje pole cest, po kterych se muze jet
     */
    public static Cesta[] poleCest;
    /**
     * obsahuje djikstru ze vsech skladu
     */
    public static double[] poleCen;
    /**
     * obsahuje vsechna vygenerovana kolecka, nektera ale cestu nezvladnou
     */
    public static ArrayList<Kolecko> vygenerovaneKolecka = new ArrayList<>();
    /**
     * tento ArrayList obsahuje ta kolecka, se kteryma nakonec pracujeme
     */
    public static ArrayList<Kolecko> existujiciKolecka = new ArrayList<>();
    /**
     * x-ova souradnice prvniho vrcholu v ceste
     */
    public static double souradnice1X;
    /**
     * y-ova souradnice prvniho vrcholu v ceste
     */
    public static double souradnice1Y;
    /**
     * x-ova souradnice druheho vrcholu v ceste
     */
    public static double souradnice2X;
    /**
     * y-ova souradnice druheho vrcholu v ceste
     */
    public static double souradnice2Y;
    /**
     * atribut pro ukladani celkoveho vylozeneho pytlu
     */
    public static int pocetVylozenychPytlu = 0;
    /**
     * atribut pro ukladani celkoveho poctu zpracovanych pozadavku
     */
    public static int zpracovanePozadavky = 0;
    /**
     * zjisti, jak je resena implementace grafu
     */
    public static boolean implSpojSeznamem = false;
    /**
     * Dvojrozmerny list datoveho typu VrcholCena
     */
    public static List<List<VrcholCena>> shortestPaths  = new ArrayList<>();
    /**
     * preskoceni pozadavku pokud neda deadline
     */
    public static boolean skip = false;
    /**
     * slouzi k vypsani spravneho pozadavku konce programu, k rozliseni, jak program skoncil
     */
    public static boolean neniCesta = false;
    /**
     * slouzi k rozliseni situace, kdy kolecko na sklade je, a kdy je potreba generovat
     */
    public static boolean generujKolecko = true;
    /**
     * slouzi k preskoceni navysovani poctu zpracovanych pozadavaku kdyz jede kolecko vicekrat
     */
    public static boolean preskocPricteniPozadavku = false;
    /**
     * Metoda pro zvětšování pole, pokud je dat více a do pole Stringu se nevejdou,
     * pole se vzdy zvysi o dvojnasobek
     */
    public static void zvetsiPole() {
        if (poleStringu[poleStringu.length - 1] != null) {           //pokud se data nevejdou do pole, přidá místo
            String[] novePole = new String[poleStringu.length * 2];
            for (int i = 0; i < poleStringu.length; i++) {
                novePole[i] = poleStringu[i];
            }
            poleStringu = novePole;
        }
    }
    /**
     * Metoda, která načte vstupní soubor
     * a uloží jeho data do pole (nejdrive je ale zpracuje)
     * @param argument (String)
     */
    public static void nactiData(String argument) {

        try {
            File soubor = new File(argument);
            Scanner myReader = new Scanner(soubor);   //čtení souboru

            while (myReader.hasNext()) {

                String data = myReader.next();
                String promenna;
                String promenna2;

                if (data.isEmpty()) {     //zjistí jestli je řádek prázdný
                    continue;
                }

                zvetsiPole();

                if (data.contains("❄")) {

                    if (!data.startsWith("❄")) {                            //řeší problémy s číslem u ❄ bez mezery
                        String cut1 = data.substring(0, data.indexOf("❄"));
                        String cut2 = data.substring(data.indexOf("❄"));
                        poleStringu[i++] = cut1;
                        data = cut2;
                    }

                    data = data + " " + myReader.nextLine();     //když je znak, tak načte celej řádek místo jen jednoho stringu nextLine() x next()
                    int krumpac = data.lastIndexOf("⛏");    //pokud je krumpac -1, znamená to, že nenašel, tudíž musí být na dalším řádku,

                    while (krumpac == -1) {                 // tak ho tam hledáme
                        data = myReader.nextLine();
                        krumpac = data.lastIndexOf("⛏");
                    }

                    promenna = data.substring(0, krumpac + 1);          //když najdeme, substring
                    promenna2 = data.replace(promenna, "");

                    if (promenna2.isEmpty()) {
                        continue;
                    }

                    promenna2 = promenna2.trim();

                    while (promenna2.contains(" ")) {    //while cyklus kontroluje, když promenna2 drží víc než jednu hodnotu, např, "0 0" a rozděluje je na "0" "0"
                        zvetsiPole();
                        String temp = promenna2.substring(0, promenna2.indexOf(" ") + 1);
                        poleStringu[i] = temp.trim();
                        promenna2 = promenna2.substring(promenna2.indexOf(" ") + 1);
                        i++;
                    }
                    poleStringu[i] = promenna2;
                    i++;
                }
                else {
                    poleStringu[i] = data.trim(); //pokud neobsahuje *, tak jsou vsechna data potrebna a ukladame vsechny
                    i++;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("CHYBA PŘI ČTENÍ SOUBORU"); //Chybova hlaska pokud nastane chyba v cteni
            e.printStackTrace();
        }
    }

    /**
     * Metoda z nactenych dat v poli vytvori instance potrebne pro simulaci
     */
    public static void vytvorInstance() {

        //sklady
        poleSkladu = new Sklad[Integer.parseInt(poleStringu[0])];
        int j = 1; //zacinajici index v souboru

        for (int i = 0; i < Integer.parseInt(poleStringu[0]); i++) {
            poleSkladu[i] = new Sklad(Double.parseDouble(poleStringu[j++]), //X
                    Double.parseDouble(poleStringu[j++]), //Y
                    Integer.parseInt(poleStringu[j++]), //pocetPytlu
                    Double.parseDouble(poleStringu[j++]), //cas pro doplneni
                    Double.parseDouble(poleStringu[j++])); //cas nalozeni/vylozeni
        }

        //zakaznici
        poleZakazniku = new Zakaznik[Integer.parseInt(poleStringu[j++])];

        for (int i = 0; i < poleZakazniku.length; i++) {
            poleZakazniku[i] = new Zakaznik(Double.parseDouble(poleStringu[j++]), //X
                    Double.parseDouble(poleStringu[j++])); //Y
        }

        //cesty
        poleCest = new Cesta[Integer.parseInt(poleStringu[j++])];

        for (int i = 0; i < poleCest.length; i++) {
            poleCest[i] = new Cesta(Integer.parseInt(poleStringu[j++]), //sklad
                    Integer.parseInt(poleStringu[j++])); //S+index zakznika
        }

        //kolecka
        poleKolecek = new DruhKolecka[Integer.parseInt(poleStringu[j++])];

        for (int i = 0; i < poleKolecek.length; i++) {
            poleKolecek[i] = new DruhKolecka(poleStringu[j++], //druh
                    Double.parseDouble(poleStringu[j++]), //vmin
                    Double.parseDouble(poleStringu[j++]), //vmax

                    otestujVstupVzdalenosti(poleStringu[j++] ), //dmin
                    otestujVstupVzdalenosti(poleStringu[j++] ), //dmax
                    Double.parseDouble(poleStringu[j++]), //td
                    Integer.parseInt(poleStringu[j++]), //kd
                    Double.parseDouble(poleStringu[j++]));//pd
        }

        //pozadavky
        polePozadavku = new Pozadavek[Integer.parseInt(poleStringu[j++])];

        for (int i = 0; i < polePozadavku.length; i++) {
            polePozadavku[i] = new Pozadavek(
                    Double.parseDouble(poleStringu[j++]), //tz
                    Integer.parseInt(poleStringu[j++]), //zp
                    Integer.parseInt(poleStringu[j++]), //kp
                    Double.parseDouble(poleStringu[j++]));//tp
        }
    }

    /**
     * Hlavni spousteci metoda, obsahujici zpracovani souboru a naslednou simulaci
     * @param args (String[])
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uživatel nezadal vstupní soubor, či jich zadal více.");
            System.exit(1);
        }
        //nacte a zpracuje vstupni data
        nactiData(args[0]);
        //vytvori ze vstupnich dat instance
        vytvorInstance();

        //vybere graf vyhodnejsi pro implementaci
        IGraf grafSousednosti;

        //vytvorime novy graf sousednosti
        if((poleSkladu.length+ poleZakazniku.length) >= (poleCest.length+1)){  //2V >= pocet cest
            grafSousednosti = new GrafSeznam(poleSkladu.length + poleZakazniku.length); //spojovy seznam
            implSpojSeznamem = true;
        } else {
            grafSousednosti = new GrafMatice(poleSkladu.length + poleZakazniku.length); //matice sousednosti
        }

        //inicializace vrcholu
        for (int i = 0; i < poleCest.length; i++) {
            inicializujSouradniceVrcholuVCeste(i);

            //vypocet vzdalenosti pro cenu cest
            grafSousednosti.pridejHranu(poleCest[i].getI() - 1,
                    poleCest[i].getJ() - 1,
                    grafSousednosti.vzdalenost(
                            souradnice1X,
                            souradnice2X,
                            souradnice1Y,
                            souradnice2Y));
        }

        //vytvorime si prioritni frontu pozadavku a vypisu
        //v prioritni fronte se nachazi pozadavky.
        PrioritniFronta fronta = new PrioritniFronta(polePozadavku.length);
        // v prioritni fronte si uchovavame vsechny vypisy, i ty ktere predpocitavame dale v programu
        PrioritniFrontaVypis vypisy = new PrioritniFrontaVypis(polePozadavku.length);

        //do fronty pozadavku ulozime zadane pozadavky
        for (int i = 0; i < polePozadavku.length; i++) {
            fronta.add(polePozadavku[i]);
        }

        //ZACATEK SIMULACE
        if (!fronta.jePrazdna()) { //pokud nejsou zadany pozadavky, program se nespusti
            //casovani simulace
            while(!fronta.jePrazdna()) {
                //vybere nejnizsi cas z pozadavku
                vyberCas(fronta);
                //Prubezne doplnovani pytlu do skladu v prubehu simulace
                for (int i = 0; i < poleSkladu.length;i++) {
                    poleSkladu[i].zkontrolujDoplneniSkladu(time);
                }

                //do ty doby, dokud neni prazdna fronta, a mame pozadavky se stejnym casem, tak s nimi pracujem
                while (!fronta.jePrazdna() && fronta.get().getTz() == time) {
                    vyberNejkratsiZeSkladu(grafSousednosti, fronta); //vybereme nejkratsi sklad, kde budeme generovat kolecko

                    //pred tim nez budeme generovat kolecka se podivame jestli z existujicih kolecek neni na nekterem sklade kolecko
                    //ktere je volne a dokaze zpracovat pozadavek do deadlinu
                    vyberExistujiciKoleckoNaSklade(fronta, vypisy);

                    //Pokud kolecko na sklade neni k dispozici tak generujeme kolecko jinak preskakujeme
                    if(generujKolecko) {
                        //prida vygenerovane kolecko mezi existujici kolecka
                        existujiciKolecka.add(vygenerujKolecko(fronta, vypisy));

                    }

                    //jestlize kolecko nedokaze dojet k zakaznikovi v deadlinu nebo neni k zakaznikovi cesta ze skladu tak skip = true
                    if(skip) {
                        existujiciKolecka.remove(existujiciKolecka.size()-1);//odeberem nullovy prvek v listu, ktery naznacuje ze zadne kolecko nemuze dojet k zakaznikovi
                    }

                    //pokud generuji sklady 0 pytlu -> konec simulace
                    int count = 0;
                    int mezni = poleSkladu.length-1;
                    for (int i = 0; i <poleSkladu.length; i++) {
                        if(poleSkladu[i].getKs() == 0) {
                            if (count == mezni) {
                                System.out.println("Cas: " + Math.round(fronta.get().getDeadline()) + ", Zakaznik " + fronta.get().getZp() + " umrzl zimou, protoze jezdit s koleckem je hloupost, konec");
                                System.out.println("Sklad: " + (k + 1) + " generuje " + poleSkladu[k].getKs() + " ks pytlu");
                                System.out.println("----------------------");
                                System.out.println("Celkovy pocet dovezenych pytlu: " + pocetVylozenychPytlu);
                                System.out.println("Pocet zpracovanych pozadavku: " + zpracovanePozadavky);
                                System.exit(0);
                            }
                            count++;
                        }
                    }

                    //pokud generujem kolecko tak vykoname
                    if(generujKolecko && !skip) {
                        //preskocime protoze kolecko nedokaze dojet k zakaznikovi v deadlinu nebo neni k zakaznikovi cesta ze skladu (skip true)
                        Kolecko kol = existujiciKolecka.get(existujiciKolecka.size() - 1);
                        //nastavime kolecku o jaky pozadavek se stara
                        kol.setCisloPozadavku(fronta.get().getPoradi());
                        //pripravime vozik k odjezdu
                        pripravVozikKOdjezdu(kol, fronta, vypisy);
                    }
                    //nakonci implicitne nastavime generuj kolecko na true
                    generujKolecko = true;
                    //a nakonec vymazeme prave zpracovany pozadavek a nastavime skip na false pro dalsi pruchod
                    skip = false;
                    fronta.remove();
                }
            }
            //program konci
            for(int i = 0; i < vypisy.getCounter(); i++){
                //pokud cas posledniho vypisu je mensi nebo rovno deadlinu pozadavku ktery nelze zpracovat
                //(at uz z duvodu spatnych kolecek nebo ze neni cesta k zakaznikovi)
                if(time <= end) {
                    //vypiseme vypisy ktere jsou mensi nez deadline popripade stejne
                    if(vypisy.get().getTime()<=end) {
                        System.out.println(vypisy.get());
                        if(vypisy.get().getPocetVylozenychPytlu() != 0){
                            pocetVylozenychPytlu = pocetVylozenychPytlu + vypisy.get().getPocetVylozenychPytlu();
                        }
                        vypisy.remove();
                    }
                }
                else{//pokud cas posledniho vypisu je vetsi nez deadline pozadavku ktery nelze zpracovat
                    while(vypisy.get().getTime()<=end) {//Dokud jsou vypisy mensi nez deadline tak vypisujeme
                        System.out.println(vypisy.get());
                        if(vypisy.get().getPocetVylozenychPytlu() != 0){
                            pocetVylozenychPytlu = pocetVylozenychPytlu + vypisy.get().getPocetVylozenychPytlu();
                        }
                        vypisy.remove();
                    }
                    //ukonceni programu
                    System.out.println("-------------------------");
                    System.out.println("Požadavky byly vyčerpány.");
                    System.out.println("Celkovy pocet dovezenych pytlu: "+pocetVylozenychPytlu);
                    System.out.println("Pocet zpracovanych pozadavku: "+ zpracovanePozadavky);
                    System.exit(0);
                }
            }
            //ukonceni programu
            System.out.println("-------------------------");
            System.out.println("Požadavky byly vyčerpány.");
            System.out.println("Celkovy pocet dovezenych pytlu: "+pocetVylozenychPytlu);
            System.out.println("Pocet zpracovanych pozadavku: "+ zpracovanePozadavky);
            System.exit(0);
        }
        //do tohoto else se dostaneme pouze, pokud v simulaci nebyly zadany zadne pozadavky
        else {
            System.out.println("Nebyly zadány žádné požadavky, program končí...");
            System.out.println("Celkovy pocet dovezenych pytlu: 0");
            System.out.println("Pocet zpracovanych pozadavku: "+ zpracovanePozadavky);
        }
    }

    /**
     * Metoda zkontroluje, jestli je pozadovany pocet pytlu na sklade, pak vraci true, jinak false
     * @param pocetPozadovanychKusu (int)
     * @return true pokud ano, jinak false
     */
    public static boolean jsouPytleNaSklade(int pocetPozadovanychKusu){
        return poleSkladu[k].getAktualniKS() >= pocetPozadovanychKusu;
    }
    /**
     * Metoda zjistujici, jestli vubec existuje nejake kolecko, ktere je schopne dojet k zakaznikovi a dojet v cas
     * @param fronta (Prioritni_fronta) -> pozadavku
     * @return true pokud ano, jinak false
     */
    public static boolean daToNejakeKolecko(PrioritniFronta fronta){
        //Projde vsechny druhy kolecek a otestuje jejich vydrz oproti vzdalenosti k zakaznikovi
        for(int i = 0; i < poleKolecek.length; i++){
            //Pokud je schopne nejake kolecko k zakaznikovi dojet
            if (poleKolecek[i].getVydrz() >= 2*poleCen[k] && stihneToKoleckoKtereToDa(poleKolecek[i], poleCen[k], fronta)){
                //Pokud to stihne i v cas, vrati true, jinak se jde na dalsi smycku
                return true;
            }
        }
        return false;
    }

    /**
     * Pomocna metoda k daToNejakeKolecko.
     * Otestuje jestli dany druh kolecka vubec stihne dojet k zakaznikovi.
     * @param druh_kol -> Druh kolecka
     * @param vzdalenostKZak -> cenak ze skladu k k zakaznikovi
     * @param fronta -> fronta s pozadavky
     * @return true pokud stihne, jinak false
     */
    public static boolean stihneToKoleckoKtereToDa(DruhKolecka druh_kol, double vzdalenostKZak, PrioritniFronta fronta){
        //doba kterou kolecko pojede k zakaznikovi
        double cas = vzdalenostKZak/druh_kol.getV();
        //cas nalozeni a vylozeni pytlu do maximalniho zatizeni kolecka
        double casNalozeniAVylozeni = (druh_kol.getKd()*poleSkladu[k].getTn())*2;
        //pokud cas prichodu pozadavku + cas jizdy + vylozeni a nalozeni pytlu je mensi nebo rovno deadline
        return cas + fronta.get().getTz() + casNalozeniAVylozeni <= fronta.get().getDeadline();
    }

    /**
     * Metoda testuje jestli primo toto vygenerovane kolecko je schopne dojet k zakaznikovi
     * a zpet na sklad
     * @param kolecko -> prozatimni vygenerovane kolecko
     * @return true pokud ano, jinak false
     */
    public static boolean testDojezduTamAZpet(Kolecko kolecko){
        //jestlize vydrz kolecka je vetsi nebo rovno ceste tam a zpatky
        return kolecko.getVydrz() >= poleCen[k] * 2;
    }

    /**
     * Metoda pro nalezení nejkratší cesty ze skladu ke cílovému zákazníkovi, kontroluje i podle
     * poctu pytlu dostupnych na sklade
     * @param grafSousednosti (Graf)
     * @param fronta (Prioritni_fronta)
     */
    public static void vyberNejkratsiZeSkladu(IGraf grafSousednosti, PrioritniFronta fronta) {

        //index cílového zákaznika
        int cilovyZak = fronta.get().getZp() + poleSkladu.length - 1;

        //pole obsahuje ceny cest ze vsech skladu do ciloveho zakaznika
        poleCen = new double[poleSkladu.length];

        for (int i = 0; i < poleSkladu.length; i++) {
            //do poleCen ulozime ceny k zakaznikovi, vyber zalezi na implementaci grafu
            if(implSpojSeznamem){
                poleCen[i] = ((GrafSeznam) grafSousednosti).dijkstra(((GrafSeznam) grafSousednosti).hrany, i, shortestPaths)[cilovyZak];
            } else {
                poleCen[i] = ((GrafMatice) grafSousednosti).dijkstra2(((GrafMatice) grafSousednosti).getPole(), (i), shortestPaths)[cilovyZak];
            }
        }
        //nastavime implicitne jako nejkratsi trasu z nulteho skladu
        double min_trasa = poleCen[0];

        //a budem si hlidat jestli je to z jinyho skladu rychlejsi, pokud ano, ==true
        boolean proslo = false;

        for (int i = 0; i < poleCen.length; i++) {
            if (poleCen[i] < min_trasa) {
                proslo = true;
                //a do minimalni trasy ulozime nejkratsi z poleCen
                min_trasa = poleCen[i];
                //nasledne si jeho index ulozime do atributu k, abychom s nim mohli dale pracovat
                k = i;
            }
        }

        //pokud proslo == false, znamena to ze nejkratsi cesta vede z prvniho skladu
        if (!proslo){
            k = 0;
        }

        //Dale kontrolujeme, jestli dany sklad na dostatek pytlu
        boolean test = otestujKapacituSkladu(k, fronta);
        if (!test) {
            //pokud ne, vymenime vychozi sklad na jiny
            k = vymenIndexSkladu(fronta);
        }

        //pokud je cena nekonecno, znamena to ze k zakaznikovi zadna cesta nevede
        if(poleCen[k] == POSITIVE_INFINITY){
            kZakaznikoviNeniCesta(cilovyZak);
        }
    }

    /**
     * Metoda vygeneruje kolecko, nastavi potrebne atributy a provede pozadovane testy, nasledne vrati vygenerovane kolecko.
     * 1. Zajistuje vygenerovani kolecka se zahrnutou pravdepodobnosti generovani.
     * 2. Nastavy potrebne atributy pro kolecko. (cil, pozice)
     * 3. Prida kolecko do listu moznych kolecek pro zpracovani dotycneho pozadavku.
     * 4. Otestuje jestli nejaky druh kolecka vubec muze dojet k zakaznikovi,
     * (pokud ne, vytvori informacni vypis a nastavi end do ktereho se program ukonci a nasledne do jeho hodnoty se vypisy vypisy)
     * 5. Dale otestuje jestli to zvladne dotycne vygenerovane kolecko.
     * 6. Za predpokladu ze ne, tak rekurzi vygeneruje to spravne a vrati ho.
     * @param fronta -> fronta pozadavku
     * @param vypisy2 -> fronta vypisu
     * @return kolecko -> (Kolecko)
     */
    public static Kolecko vygenerujKolecko(PrioritniFronta fronta, PrioritniFrontaVypis vypisy2) {

        //hodnota od 0 do 1
        Random r = new Random();
        double nahodneC = r.nextDouble();

        //promena pro prirazovani sanci ve foru
        double hranice;
        double soucetPredchozicHranic = 0;

        //Serazeni druhu kolecek podle pd -> s pomoci compareTo ve tride Druh_kolecka bude pole serazeno
        //tak, ze na prvnim miste bude druh kolecka s nejvetsim pd
        Arrays.sort(poleKolecek);
        for (int j = 0; j < poleKolecek.length; j++) {
            //hranice, ktera reflektuje pravdepodobnost vygenerovani kolecka.
            //v kazde iteraci for loopu se musi hranice zvetsit o pd nasledujiciho druhu
            hranice = poleKolecek[j].getPd() + soucetPredchozicHranic;

            //Pokud se nahodne vygenerovane cislo trefilo do hranice (pd druhu kolecka), pak se vygeneruje kolecko daneho druhu
            if (nahodneC < hranice) {

                //vygenerovani kolecka prislusneho druhu
                Kolecko kol = new Kolecko(poleKolecek[j].getDruh(),
                        poleKolecek[j].getV(),
                        poleKolecek[j].getVydrz(),
                        poleKolecek[j].getKd(),
                        poleKolecek[j].getTd());
                //Nastaveni potrebnych atributu pro zpracovani pozadavku
                //nastaveni indexu ciloveho zakaznika
                kol.setCil(fronta.get().getZp());
                //nataveni pozice kolecka kde se nachazi -> pri vygenerovani tedy na sklade
                kol.setPozice(k);
                //pridani kolecka do listu potencialnich kolecek pro vyreseni pozadavku
                vygenerovaneKolecka.add(kol);
                // ulozi true pokud nejake kolecko zvladne pozadavek vyridit
                boolean kontrola = daToNejakeKolecko(fronta);
                //Situace kdy pozadavek nelze zpracovat
                if(!kontrola) {
                    //docasna promena pro porovnani deadlinu pozadavku ktere nejdou zpracovat
                    double tmp = fronta.get().getDeadline();
                    //nastaveni atributu pro preskakovani vysilani kolecek a pridavani do listu existujicich kolecek
                    skip = true;

                    //vypsani nezpracovatelneho pozadavku
                    Vypis vypis = new Vypis(fronta.get().getTz(), fronta.get().vypisPozadavek(),false , fronta.get().getPoradi()
                            ,kol.getPoradi(), true, false, false, false, false); //prvni vypis
                    vypisy2.add(vypis);

                    //pokud tmp je mensi tak se zmeni deadline pro ukonceni simulace
                    if(tmp < end){
                        end = tmp;
                        //pokud kolecko nedokate k zakaznikovi dojet at uz jestli je pomale nebo nema vzdalenost
                        if(!neniCesta) {
                            //informujici vypis o tom ze zakaznik umrzl
                            Vypis vypis2 = new Vypis(fronta.get().getDeadline(), "Cas: " + Math.round(fronta.get().getDeadline()) + ", Zakaznik " + fronta.get().getZp() +
                                    " umrzl zimou, protoze jezdit s koleckem je hloupost, konec" +
                                    "\nzadne kolecko k zakaznikovi nedojede, nebo je moc pomale aby to zvladlo do deadlinu a pozadavek proto nelze zpracovat"
                                    , true, fronta.get().getPoradi(), kol.getPoradi(),false, false, false, false, false);
                            vypisy2.add(vypis2);
                        }else { //neni k zakaznikovi cesta z zadneho skladu
                            //informujici vypis o tom ze zakaznik umrzl
                            Vypis vypis2 = new Vypis(fronta.get().getDeadline(), "Cas: " + Math.round(fronta.get().getDeadline()) + ", Zakaznik " + fronta.get().getZp() +
                                    " umrzl zimou, protoze jezdit s koleckem je hloupost, konec" +
                                    "\nNeexistuje sklad vedoucí k zákazníkovi č. " + fronta.get().getZp() + ". Simulace končí."
                                    , true, fronta.get().getPoradi(), kol.getPoradi(),false, false, false, false, false);
                            vypisy2.add(vypis2);
                            //nastaveni pro dalsi prubehy
                            neniCesta = false;
                        }
                    }
                    //misto platneho vygenerovaneho kolecka vraci null-ove kolecko -> dale v programu osetrime aby byl prvek odstranen
                    return null;
                }

                // false pokud vygenerovane kolecko nema dostatecnou vydrz na cestu
                boolean kontrola2 = testDojezduTamAZpet(kol);
                //jestlize false, tak se rekurzivne zavola metoda dokud se nevygeneruje kolecko s dostatecnou vydrzi
                if(!kontrola2) {
                    vygenerovaneKolecka.add(kol);
                    Kolecko.pocet = Kolecko.pocet-1;
                    vygenerujKolecko(fronta, vypisy2);
                }
                //preruseni for cyklu
                break;
            }
            //ulozeni hranice pro vypocet hranice v nasledujici smycce
            soucetPredchozicHranic = hranice;
        }
        // vrati kolecko ktere dojede k zakaznikovy a zpatky, s nastavenym cilem, pozici a pozadavkem ktery ma zpracovat
        return vygenerovaneKolecka.get(vygenerovaneKolecka.size()-1);
    }

    /**
     * Metoda, porovnavajici pocet pytlu ve skladu oproti poctu pytlu v pozadavku
     * @param indexSkladu (int)
     * @param fronta (Prioritni_fronta)
     * @return true, kdyz je pocet pytlu na sklade vyssi nebo stejny, false pokud mensi
     */
    public static boolean otestujKapacituSkladu(int indexSkladu, PrioritniFronta fronta) {
        return poleSkladu[indexSkladu].getAktualniKS() >= polePozadavku[fronta.get().getPoradi() - 1].getKp();
    }

    /**
     * Metoda, která, když sklad není naložen, vymění tento sklad za sklad již naložený, pokud takový
     * sklad existuje, pokud, zůstáváme u nejbližšího skladu k zákazníkovi.
     * @param fronta (Prioritni_fronta)
     * @return m (int) .. index skladu, ze ktereho budeme posilat
     */
    public static int vymenIndexSkladu(PrioritniFronta fronta){
        double[] temp = poleCen.clone();
        Arrays.sort(temp);
        for (int j = 1; j < poleCen.length; j++) {
            //uložíme si index sesortovaneho pole, prvni prvek preskakujeme, protoze jiz nevyhovel v podmince
            double a = temp[j];
            //v metode otestujKapacituSkladu ale musime zachovat indexy z poleCen, aby vyber indexu byl na validnim miste
            for (int m = 0; m < temp.length; m++) {
                if (poleCen[m] == a) {
                    if (poleCen[m] == POSITIVE_INFINITY) { //pokud je vybrany sklad INFINITY, skipni
                        continue;
                    }
                    //takto projedeme všechny indexy, pokud zadny nevyhovuje, vracime k index, se kterym jsme zacali a generujem pytle...
                    if (otestujKapacituSkladu(m, fronta)) {
                        return m;
                    }
                }
            }
        }
        return k;
    }

    /**
     * Metoda zjistujici, jestli k zakaznikovi vubec vede nejaka cesta
     * @param cilovyZak (int), index zakaznika.
     */
    public static void kZakaznikoviNeniCesta(int cilovyZak){
        //nastaveni atributu pro identifikaci ze k zakaznikovi nevede cesta
        neniCesta = true;
    }

    /**
     * Metoda zjistujici, jestli kolecko unese pocet pozadovanych pytlu
     * @param pozadavek (Prioritni_fronta)
     * @param kol (Kolecko)
     * @return true, pokud unese, jestlize ne, vraci false
     */
    public static boolean neniPretizene(PrioritniFronta pozadavek, Kolecko kol) {
        return kol.getKd() >= pozadavek.get().getKp();
    }

    /**
     * Metoda pripravi vozik k odjezdu k zakaznikovi. Vyuziva pomocnou metodu vysliKolecko, neniPretizene a stihneToKoleckoVicekratDoDeadline.
     * 1. Zjistime jestli se na sklade nachazi dostatek pytlu, pokud ne odsuneme pozadavek, pokud ano pokracujeme dale
     * 2. Dale nastavi atributy pro opravu kolecka a vytvori vypis pokud k oprave ma dojit
     * 3. Vytvori uvadejici vypis pozadavku
     * 4. Nasledne otestuje jestli je kolecko pretizene nebo ne, pokud ne tak vysleme kolecko metodou vysliKolecko,
     * pokud ano tak otestujeme jestli kolecko stihne zpracovat pozadavek kdyz pojede vicekrat
     * (pokud ano vysleme kolecko, pokud ne pozadavek rozdelime na dva kdy kazdy ma pulku pytlu povodniho)
     * @param kol -> predane vygenerovane kolecko
     * @param fronta -> fronta pozadavku
     * @param vypisy -> fronta vypisu
     */
    public static void pripravVozikKOdjezdu(Kolecko kol, PrioritniFronta fronta, PrioritniFrontaVypis vypisy) {
        //na sklade jsou pytle k dispozici
        if(jsouPytleNaSklade(fronta.get().getKp())) {
            boolean jeTrebaOpravit = false;
            //Napocitame kolikrat kolecko bude muset jet pokud pojede samo
            int pytle = fronta.get().getKp();
            int pocetJizd = 0;
            while(pytle > 0 ) {
                pytle = pytle - kol.getKd();
                pocetJizd++;
            }
            //nastaveni atributu ze bude potreba opravit kolecko
            if( (kol.getAktualniVydrz() - (poleCen[k]*2)) < 0 ) {
                jeTrebaOpravit = true;
            }
            //pokud je pretizene a zaroven stihne pozadavek zpracovat do deadlinu
            if(!neniPretizene(fronta, kol) && stihneToKoleckoVickratDoDeadline(kol, poleCen[k], pocetJizd, fronta, jeTrebaOpravit)) {
                //Odecitame od aktualni vydrze kolecka delku cesty k zakaznikovi a zpatky, pro evidovani opotrebeni kolecka
                kol.setAktualniVydrz(kol.getAktualniVydrz() - (poleCen[k]*2) );

                //kolecko na cestu nema vydrz, a tak se naplanuje oprava kolecka
                if(kol.getAktualniVydrz() <= 0) {
                    jeTrebaOpravit = true;
                    //opravujeme kolečko
                    opravKolecko(kol, fronta, vypisy);
                }
            }
            //pozadavek se neodsouva
            fronta.get().setOdsunuty(false);
            //pokud se nejedna o rozlozene pozadavky tak chceme vlozit do vypisu jejich uvadejici vypis
            if(!fronta.get().getRozlozeny()) {
                Vypis vypis = new Vypis(fronta.get().getTz(), fronta.get().vypisPozadavek(), false,
                        fronta.get().getPoradi(), kol.getPoradi(),true, false, false, false, false); //prvni vypis
                vypisy.add(vypis);
            }
            //kolecko neni pretizene a muzeme ho nalozit tak aby zpracovalo pozadavek na jednu cestu
            if(neniPretizene(fronta, kol)) {
                kol.setAktualniVydrz(kol.getAktualniVydrz() - (poleCen[k]*2));
                if( (kol.getAktualniVydrz() < 0 )) {
                    jeTrebaOpravit = true;
                    //opravujeme kolecko
                    opravKolecko(kol, fronta, vypisy);
                }
                //Kolecko vysilame s poctem pytlu ktery je uveden v pozadavku
                vysliKolecko(fronta.get().getKp(), kol, fronta, vypisy, jeTrebaOpravit);
            }
            else{
                //kolecko je pretizene a bude muset jet na vicekrat nebo bude potrebovat pomoc od jineho kolecka
                //pokud to kolecko stihne do deadlinu vicekrat
                if(stihneToKoleckoVickratDoDeadline(kol, poleCen[k], pocetJizd, fronta, jeTrebaOpravit)) {
                    vysliKoleckoVicekrat(kol, fronta, vypisy, pocetJizd, jeTrebaOpravit);
                }else {//Kolecko to samo nestihne a bude potrebovat pomoc
                    rozlozPozadavek(kol, fronta);
                }
            }
        }
        else{
            //pytle nejsou na sklade a je potreba pockat v case
            //odsunuti pozadavku tim, ze se vytvori novy s jinym TZ
            //zjistime cas na ktery se ma pozadavek odsunout
            double casOdsunu = vypocitejCasOdsunu(time);

            Pozadavek odsunuty = new Pozadavek(casOdsunu, fronta.get().getZp(), fronta.get().getKp(), fronta.get().getTp() );

            //nastavime, ze se jedna o odsunuty pozadavek
            odsunuty.setOdsunuty(true);
            //nastaveni poradi pozadavku
            odsunuty.setPoradi(fronta.get().getPoradi());
            //jako deadline noveho pozadavku je nastaven deadline toho stareho, jelikoz pozadavek byl pouze odsunut
            odsunuty.setDeadline(fronta.get().getDeadline());
            odsunuty.setTz(fronta.get().getTz());

            //vytvarime dalsi vypis, kterej vypise odsunuty pozadavek
            Vypis vypis = new Vypis(fronta.get().getTz(), odsunuty.vypisPozadavek(), false, fronta.get().getPoradi(), kol.getPoradi(),
                    true, false, false, false, false);
            vypisy.add(vypis);

            //cas odsunuti
            odsunuty.setTz(casOdsunu);
            //pridani do fronty
            fronta.add(odsunuty);
            //Kolecko zustane na sklade jelikoz je pozadavek odsunut  (-4)
            kol.setCisloPozadavku(-4);
            //odstraneni kolecka jelikoz pozadavek byl odsunut
            if(generujKolecko) {
                existujiciKolecka.remove(existujiciKolecka.size()-1);
                Kolecko.pocet--;
            }
        }
    }

    /**
     * Metoda kolecko nalozi, nastavi cas odjezdu (opravu promitne do casu, pokud se bude opravovat), odecte nalozene pytle ze skladu,
     * nastavi cas prijezdu k zakaznikovi a prijezdu na sklad, vytvori vypis o zpracovani pozadavku, odjezdu, prijezdu na sklad,
     * navysi pocitadlo vylozenych pytlu a zpracovanych pozadavku a pomocnou metodou kukNaKolecko zajisti vypisy zakazniku koukajici na kolecko
     * @param pocetPytlu, ktere kolecko bude prevazet (int)
     * @param kol (Kolecko)
     * @param fronta -> pozadavku (Prioritni_fronta)
     * @param vypisy -> vypisu (Prioritni_fronta_vypis)
     * @param jeTrebaOpravit -> informace o oprave (boolean)
     */
    public static void vysliKolecko(int pocetPytlu, Kolecko kol, PrioritniFronta fronta, PrioritniFrontaVypis vypisy, boolean jeTrebaOpravit) {
        //nalozime pytle na kolecko
        kol.setAktualnePytlu(pocetPytlu);
        //odecteme pytle ze skladu
        poleSkladu[k].setAktualniKS(poleSkladu[k].getAktualniKS() - kol.getAktualnePytlu());
        //nastavime cas odjezdu tak te k time pricteme dobu nalozeni pytlu
        kol.setCasOdjezdu(poleSkladu[k].getTn() * pocetPytlu + time);
        //ULOŽENÍ VÝPISŮ DO PRIORITNÍ FRONTY
        // doba jakou pojede kolecko k zakaznikovi
        double cas2 =  (poleCen[k] / kol.getV());

        //Kolecko se opravovalo a je potreba to promitnout do casu odjezdu
        if(jeTrebaOpravit) {
            //cas odjezdu se zpozdi o dobu provedeni udrzby
            kol.setCasOdjezdu(kol.getCasOdjezdu()+kol.getTd());
        }
        //nastaveni prijezdu kolecka k zakaznikovi
        kol.setCasPrijezduZak(kol.getCasOdjezdu()+cas2);
        //doba vykladani pytlu
        double casVylozeniPytlu = kol.getAktualnePytlu()*poleSkladu[k].getTn();
        //doba jakou pojede kolecko k zakaznikovi a zpatky do skladu
        double cas = ((poleCen[k] * 2) / kol.getV())+casVylozeniPytlu;
        //cas prijezdu k zakaznikovi
        double casPrijezduKZakaznikovi = kol.getCasPrijezduZak();

        //pridani vypisu pro konecne vypsani
        Vypis vypsaniZpracovanehoPozadavku = new Vypis(kol.getCasPrijezduZak(), "Cas: " + Math.round(kol.getCasPrijezduZak()) + ", Kolecko: " + kol.getPoradi() + "," +
                " Zakaznik: " + kol.getCil() + ", Vylozeno pytlu: " + kol.getAktualnePytlu() + ", Vylozeno v: " + Math.round((casVylozeniPytlu+casPrijezduKZakaznikovi)) +
                " , Casova rezerva: " + Math.round((fronta.get().getDeadline()-(casPrijezduKZakaznikovi+casVylozeniPytlu))),false, fronta.get().getPoradi(), kol.getPoradi(),
                false, false, false, true, false);
        vypsaniZpracovanehoPozadavku.setPocetVylozenychPytlu(kol.getAktualnePytlu());
        vypisy.add(vypsaniZpracovanehoPozadavku);

        //vylozili jsme pytle u zakaznika
        vypisy.get().setBylyPytleVylozeny(true);

        //zpracovali jsme pozadavek
        if( (time+casVylozeniPytlu+casPrijezduKZakaznikovi) < end && !preskocPricteniPozadavku && !fronta.get().getRozlozeny()) {
            zpracovanePozadavky++;
        }

        //pridani vypisu pro vnitrni fungovani programu v case
        Vypis odjezd = new Vypis(time, "Cas: " + Math.round(time) + ", Kolecko: " + kol.getPoradi() + ", Sklad: "
                + (k+1) + ", Nalozeno pytlu: " + kol.getAktualnePytlu() +
                ", Odjezd v: " + Math.round(kol.getCasOdjezdu()), false, fronta.get().getPoradi(),
                kol.getPoradi(),false, false, true, false, false);
        vypisy.add(odjezd);

        //nastaveni prijezdu na sklad
        kol.setCasPrijezduSklad(kol.getCasOdjezdu()+cas);
        //vypis navratu kolecka na sklad
        Vypis vypsaniKoleckoNaSklade = new Vypis(kol.getCasPrijezduSklad(),"Cas: "+Math.round(kol.getCasPrijezduSklad())+", Kolecko: "+
                kol.getPoradi()+", Navrat do skladu: "+(k+1),false, fronta.get().getPoradi(), kol.getPoradi(),false,
                false, false, false, true);
        vypsaniKoleckoNaSklade.setVypisKoleckaNaSklade(true);  //Marka aby se vypsal cas navratu pred odjezdem
        vypisy.add(vypsaniKoleckoNaSklade);

        //prida do fronty vypisu vypisy s kukNaKolecko
        kukNaKolecko(kol, vypisy, kol.getCil(), casVylozeniPytlu);
        //Pytle se vylozili a kolecko je bez pytlu
        if(vypisy.get().bylyPytleVylozeny()) {
            kol.setAktualnePytlu(0);
        }
        //Kolecko vyridilo pozadavek je na sklade a tak je volne (pozadavek -2)
        kol.setCisloPozadavku(-4);
    }

    /**
     * Metoda inicializuje x a y souradnice vrcholu v ceste reprezentovanou indexem cesty z poleCest.
     * Prvni podminkou se zjisti, jestli prvni vrchol uvedeny v ceste je zakaznik nebo sklad a
     * na zaklade toho se pak inicializuji souradnice x a y.
     * Druha podminka funguje na stejnem principu akorat pracuje s druhym vrcholem.
     * @param i (int)
     */
    public static void inicializujSouradniceVrcholuVCeste(int i){
        //pro ulozeni indexu prvniho vrcholu
        int prvniVrchol;
        //pro ulozeni indexu druheho vrcholu
        int druhyVrchol;
        //slouzi pro ulozeni prvniho skladu (pokud je vrchol skladem)
        Sklad sklad1;
        //slouzi pro ulozeni prvniho zakaznika (pokud je vrchol zakaznikem)
        Zakaznik zakaznik1;
        //slouzi pro ulozeni druheho skladu (pokud je vrchol skladem)
        Sklad sklad2;
        //slouzi pro ulozeni druheho zakaznika (pokud je vrchol zakaznikem)
        Zakaznik zakaznik2;

        //inicializace indexu prvniho bodu v ceste
        //Projde jestli je index prvniho vrcholu z indexu skladu, jinak je index vrcholu z indexu zakazniku
        if (poleCest[i].getI() <= poleSkladu.length) {
            //vypoect indexu pro sklad
            //1. sklad je na 0 indexu v poli proto odečítám 1 u skladu
            prvniVrchol = poleCest[i].getI() - 1;
            //sklad vybrany z pole
            sklad1 = poleSkladu[prvniVrchol];
            //x-ova soradnice skladu a y-ova souradnice skladu
            souradnice1X = sklad1.getXs();
            souradnice1Y = sklad1.getYs();

        } else {
            //vypocet indexu pro zakaznika
            //zakaznik na indexu S (poslední sklad) + 1 tím pádem odečítám vztah -(poleSkladu.length+1)
            prvniVrchol = poleCest[i].getI() - (poleSkladu.length + 1);
            //zakaznik vybrany z pole
            zakaznik1 = poleZakazniku[prvniVrchol];
            //x-ova souradnice zakaznika a y-ova souradnice zakaznika
            souradnice1X = zakaznik1.getXz();
            souradnice1Y = zakaznik1.getYz();
        }
        //inicializace indexu druheho bodu v ceste
        //Projde jestli je index druheho vrcholu z indexu skladu, jinak je index vrcholu z indexu zakazniku
        if (poleCest[i].getJ() <= poleSkladu.length) {
            //vypoect indexu pro sklad
            druhyVrchol = poleCest[i].getJ() - 1;
            //sklad vybrany z pole
            sklad2 = poleSkladu[druhyVrchol];
            //x-ova souradnice skladu a y-ova souradnice skladu
            souradnice2X = sklad2.getXs();
            souradnice2Y = sklad2.getYs();
        } else {
            //vypocet indexu pro zakaznika
            druhyVrchol = poleCest[i].getJ() - (poleSkladu.length + 1);
            //zakanik vybrany z pole
            zakaznik2 = poleZakazniku[druhyVrchol];
            //x-ova souradnice zakaznika a y-ova souradnice zakaznika
            souradnice2X = zakaznik2.getXz();
            souradnice2Y = zakaznik2.getYz();
        }
    }

    /**
     * Metoda na zaklade predaneho casu vypocte cas na ktery se ma pozadavek odsunout, neboli cas doplneni skladu
     * @param time -> cas simulace (int)
     * @return casOdsunu (cas kdy dojde k doplneni skladu)
     */
    public static double vypocitejCasOdsunu(double time){
        double casOdsunu = time;
        //Nekonecna smycka, ktera se ukonci pri splneni podminky
        while(true) {
            casOdsunu++;
            // po zmodulovani je vysledek 0, tak vrati casOdsunu
            if ( (casOdsunu % poleSkladu[k].getTs() ) == 0) {
                return casOdsunu;
            }

        }

    }
    /**
     * Metoda otestuje format vstupnich dat k vzdalenosti kolecka
     * @param vzdalenost (String)
     * @return double
     */
    public static double otestujVstupVzdalenosti(String vzdalenost) {
        if(vzdalenost.contains("e")) {
            String pomocna1 = vzdalenost.substring(vzdalenost.indexOf("e")+1);
            String pomocna2 = vzdalenost.substring(0, vzdalenost.indexOf("e"));
            double vysledek = Double.parseDouble(pomocna2) * Math.pow(10, Double.parseDouble(pomocna1));
            return vysledek;
        }else {
            return Double.parseDouble(vzdalenost);
        }

    }
    /**
     * Vybere cas na zaklade nejnizsiho casu z pozadavku
     * @param fronta (Prioritni_fronta)
     */
    public static void vyberCas(PrioritniFronta fronta){
        time = fronta.get().getTz();
    }

    /**
     * Metoda vytvori vypisy KukKoleckoTam a KukKoleckoZpet a predpocita jejich cas, ktere nasledne vlozi do vypisu.
     * Vyuziva shortestPaths, ktere si udrzuje vzdalenosti k jednotlivym zakaznikum kteri se nachazeji v ceste.
     * @param kol (Kolecko)
     * @param vypisy Prioritni_Fronta_vypis)
     * @param cilovyZak (int)
     * @param casVylozeniPytlu (double)
     */
    public static void kukNaKolecko(Kolecko kol, PrioritniFrontaVypis vypisy, int cilovyZak, double casVylozeniPytlu){
        double casKZak;
        double casKSklad;
        for (int i = 0; i < shortestPaths.size(); i++) {
            //projde pokud je vrchol (obsahujici seznam vrcholu v ceste vedouci k nemu) roven cilovemu zakazniku
            if(i == (cilovyZak+poleSkladu.length-1)) {
                for (int j = 0; j < shortestPaths.get(i).size(); j++) {
                    //projde pokud vrchol v ceste neni cilovym zakaznikem a take neni skladem
                    if(shortestPaths.get(i).get(j).vrchol != (cilovyZak+poleSkladu.length-1) && shortestPaths.get(i).get(j).vrchol > poleSkladu.length-1) {
                        //vypocet jak dlouho kolecko pojede k zakaznikovi v ceste k cilovemu zakaznikovi
                        casKZak = shortestPaths.get(i).get(j).cena / kol.getV();
                        //vypocet jak dlouho kolecko pojede k zakaznikovi v ceste zpatky na sklad
                        casKSklad =  (poleCen[k] - shortestPaths.get(i).get(j).cena) / kol.getV();

                        //vypis kuk na kolecko u prislusneho zakaznika
                        Vypis kukNaKoleckoTam = new Vypis( kol.getCasOdjezdu()+casKZak,"Cas: "+ Math.round(kol.getCasOdjezdu()+casKZak) +", Kolecko: " + kol.getPoradi() +
                                ", Zakaznik: " + ((shortestPaths.get(i).get(j).vrchol)-(poleSkladu.length-1))+", Kuk na "+ kol.getDruh() +
                                " kolecko ", false, kol.cisloPozadavku, kol.getPoradi(),false,
                                false, false, false, false);
                        //pridani do vypisu
                        vypisy.add(kukNaKoleckoTam);
                        //vypis kuk na kolecko u prislusneho zakaznika
                        Vypis kukNaKoleckoZpet = new Vypis( casVylozeniPytlu+kol.getCasPrijezduZak()+casKSklad,"Cas: "+ Math.round(casVylozeniPytlu+kol.getCasPrijezduZak()+casKSklad) + ", Kolecko: " +
                                kol.getPoradi() + ", Zakaznik: " + ((shortestPaths.get(i).get(j).vrchol)-(poleSkladu.length-1))+", Kuk na "+ kol.getDruh() +
                                " kolecko ", false, kol.cisloPozadavku, kol.getPoradi(),false,
                                false, false, false, false);
                        //pridani do vypisu
                        vypisy.add(kukNaKoleckoZpet);
                    }
                }
            }else { //jinak prejde na dalsi vrchol
                continue;
            }
        }
    }
    /**
     * Otestuje jesti jedno kolecko konkretniho druhu stihne zpracovat pozadavek do deadline
     * @param kol (Kolecko)
     * @param vzdalenostKZak (double)
     * @param fronta (Prioritni_fronta)
     * @return true pokud sthine, false pokud nestihne
     */
    public static boolean stihneToKoleckoKtereToDa2(Kolecko kol, double vzdalenostKZak, PrioritniFronta fronta){
        //cas oprav
        double casOpravy = 0;
        //doba kterou kolecko pojede k zakaznikovi
        double cas = vzdalenostKZak/kol.getV();
        //cas nalozeni a vylozeni pytlu dohromady
        double casNalozeniAVylozeni = (kol.getKd()*poleSkladu[k].getTn())*2;
        //pokud cas prichodu pozadavku + cas jizdy + vylozeni a nalozeni pytlu je mensi nebo rovno deadline
        if(kol.getAktualniVydrz()-(poleCen[k]*2) < 0) {
            casOpravy = kol.getTd();
        }
        return cas + casOpravy + fronta.get().getTz() + casNalozeniAVylozeni <= fronta.get().getDeadline();
    }

    /**
     * Otestuje jestli kolecko stihne zpracovat pozadavek, pokud pojede vicekrat (do vypoctu zahrne vsechny probihajici opravy, cesty, vykladani, nakladani)
     * @param kol (Kolecko)
     * @param vzdalenostKZak (double)
     * @param pocetJizd (int)
     * @param fronta (Prioritni_fronta)
     * @param jeTrebaOpravit (boolean)
     * @return true pokud stihe, false pokud ne
     */
    public static boolean stihneToKoleckoVickratDoDeadline(Kolecko kol, double vzdalenostKZak, int pocetJizd , PrioritniFronta fronta, boolean jeTrebaOpravit){
        //doba kterou kolecko pojede k zakaznikovi
        //Cas pro jizdy tam a zpatky
        double casSVracenim = ((vzdalenostKZak*2)*(pocetJizd))/kol.getV();
        //Cas jizdy bez navratu na sklad
        double casBezVraceni = casSVracenim - (vzdalenostKZak/kol.getV());
        //cisty cas jizdy
        double casCisteJizdy = casBezVraceni;
        //cas nalozeni a vylozeni pytlu dohromady
        double casNalozeniAVylozeni = (fronta.get().getKp()*poleSkladu[k].getTn())*2;
        //predpocitani oprav
        double vydrzKol = kol.getAktualniVydrz();
        int pocetOprav = 0;
        //Prvni oprava uz probehla a tak to musime promitnout do counteru
        if(jeTrebaOpravit) {
            pocetOprav++;
        }
        for(int i = 0; i < (pocetJizd-1); i++) {
            vydrzKol = vydrzKol - (vzdalenostKZak*2);
            if(vydrzKol < 0) {
                pocetOprav++;
                vydrzKol = kol.getVydrz();
            }
        }
        //cas oprav
        double casOprav = pocetOprav * kol.getTd();
        //celkovy cas ktery bude kolecko potrebovat k obsluze
        double celkovyCas = casOprav + casCisteJizdy + casNalozeniAVylozeni;
        //pokud cas prichodu pozadavku + cas jizdy + vylozeni a nalozeni pytlu je mensi nebo rovno deadline
        return celkovyCas + fronta.get().getTz() <= fronta.get().getDeadline();
    }
    /**
     * Metoda vybere to nejblizsi z existujcich kolecek, ktere se nachazi na sklade a nezpracovava zadny jiny pozadavek
     * a zaroven je nejblize a schopneho ho vyresit
     * @param fronta (Prioritni_fronta)
     * @param vypisy (Prioritni_fronta_vypis)
     */
    public static void vyberExistujiciKoleckoNaSklade(PrioritniFronta fronta, PrioritniFrontaVypis vypisy){
        if(existujiciKolecka.size()!=0) {
            //list ktery si uklada indexy porslych skladu
            ArrayList<Integer> prosleSklady = new ArrayList<Integer>();
            //seradime poleCen od nejnizsi ceny k nejvyssi a ulozime do tmp
            double[] temp = poleCen.clone();
            Arrays.sort(temp);
            int f = k;
            label: for(int l = 0; l < poleSkladu.length; l++) {//jdeme pres vsechny sklady dokud nenajdem volne kolecko ktere je zraoven nejbliz zakaznikovi
                for(int i = 0; i < existujiciKolecka.size(); i++ ) {//projdeme vsechna existujici kolecka
                    if(existujiciKolecka.get(i).getPozice() == f) {//pokud kolecko bylo vygenerovano na tomto sklade
                        k = f;
                        if(existujiciKolecka.get(i).getCisloPozadavku() == -4 && existujiciKolecka.get(i).getCasPrijezduSklad() <= fronta.get().getTz()
                                && testDojezduTamAZpet(existujiciKolecka.get(i)) && stihneToKoleckoKtereToDa2(existujiciKolecka.get(i), poleCen[k], fronta)){
                            //rozlisujeme jestli je kolecko volne na sklade
                            //test jestli to kolecko stihne do deadlinu pozadavku
                            //jinak se presuneme na dalsi kolecko
                            generujKolecko = false;
                            existujiciKolecka.get(i).setCisloPozadavku(fronta.get().getPoradi());
                            existujiciKolecka.get(i).setCil(fronta.get().getZp());
                            pripravVozikKOdjezdu(existujiciKolecka.get(i), fronta, vypisy);
                            break label;
                        }
                    }
                }
                //priradime index proslehoSkladu do seznamu proslych skladu
                prosleSklady.add(k);
                if(l == poleSkladu.length-1) {
                    break;
                }
                //projdeme pres vsechny indexy skladu
                for(int i = 0; i < temp.length; i++) {
                    //projdeme pres vsechny indexy proslych skladu
                    for(int j = 0; j < prosleSklady.size(); j++ ) {
                        //pokud je inedex skladu roven indexu proslehoSkladu znamena to ze uz jsme ho prochazeli a prejdeme na dalsi index Sklad (sklad)
                        if( i == prosleSklady.get(j)) {
                            continue;
                        }else {//dotycny sklad jsme neprosli musime tedy zjistit jestli je zaroven dalsi nejkratsi sklad
                            //vyberem cenu dalsiho nejkratsiho skladu a porovname ji s cenou skladu na aktualnim vybranem indexu
                            //paklize se rovnaji tak je vybran dalsi nejkratsi sklad
                            if(temp[prosleSklady.size()] == poleCen[i] && otestujKapacituSkladu(i, fronta)) {
                                //Pokud na sklade jsou pytle
                                f = i;//ulozeni indexu dalsiho nejkratsiho skladu
                            }
                        }
                    }
                }

            }
        }
    }
    /**
     * Metoda vysle kolecko viceKrat dokud nezpracuje pozadavek a nastavi vsechny potrebne atributy
     * @param kol (Kolecko)
     * @param fronta (Prioritni_fronta)
     * @param vypisy (Prioritni_Fronta_vypis)
     * @param pocetJizd (int)
     * @param jeTrebaOpravit (boolean)
     */
    public static void vysliKoleckoVicekrat(Kolecko kol, PrioritniFronta fronta, PrioritniFrontaVypis vypisy, int pocetJizd, boolean jeTrebaOpravit){
        boolean opraveni = jeTrebaOpravit;
        //nastavime boolean na true aby se preskakovala iterace zpracovanych pozadavku
        preskocPricteniPozadavku = true;
        //pokud se nejedna o rozlozene pozadavky tak pricteme k zpracovanym pozadavku (uz vime ze se pozadavek zpracuje)
        if(!fronta.get().getRozlozeny()) {
            zpracovanePozadavky++;
        }
        //slouzi pro udrzeni casu (abychom nepreskocili pozadavky ve fronte kdybychom time pri predpocitavani zmenili)
        double temp = time;
        //slouzi jako promena pro pocet pytlu ktere budeme na kolecko nakladat v jedne jizde
        int pocetPytlu = 0;
        for(int u = 0; u < pocetJizd; u++) {
            if(u == pocetJizd-1) {//pokud posledni jizda
                pocetPytlu = fronta.get().getKp()%kol.getKd();//zmodulujeme a zbytek je pocet pytlu ktere v posledni jizde na kolecko nalozime
                if(pocetPytlu == 0) {//pokud vysledek po modoulovani je roven 0, pak vime ze v posledni jizde nalozime kolecko na maximalni zatez
                    pocetPytlu = kol.getKd();
                }
            }else {//jinak na kolecko nalozime jeho maximalni zatez
                pocetPytlu = kol.getKd();
            }
            //Dalsi jizdy musime promitnout na vydrzi kolecka
            if(u != 0) {
                //od aktualni vydrze odecteme cestu k zakaznikovi a zpatky
                kol.setAktualniVydrz(kol.getAktualniVydrz() - (poleCen[k]*2));
                //nastavime kolucku o jaky pozadavek se stara
                kol.setCisloPozadavku(fronta.get().getPoradi());
            }
            //pripravime vypis pokud kolecko uz nema vydrz na 1 cestu k zakaznikovi a zpatky
            if(kol.getAktualniVydrz() <= 0) {
                //opravujeme kolečko
                kol.setAktualniVydrz(kol.getVydrz());
                //nastavime atribut ktery predame metode vysliKolecko diky kteremu si kolecko prepocita odjezd o cas opravy
                opraveni = true;
                //Pouze informujici vypis o oprave  time + doba opravy
                Vypis oprava = new Vypis(time,"Cas: " + Math.round(time) + ", Kolecko: " + kol.getPoradi() + ", Zakaznik: "
                        + fronta.get().getZp() + ", " + kol.getDruh() + " vyzaduje udrzbu, Pokracovani mozne v: " +
                        Math.round(time+kol.getTd()), false, fronta.get().getPoradi(), kol.getPoradi(),false, true, false, false, false);
                vypisy.add(oprava);
            }
            // vysleme kolecko k zakaznikovi a vratime se zpatky na sklad
            //Kolecko vysilame s max. poctem pytlu ktere unese
            vysliKolecko(pocetPytlu, kol, fronta, vypisy, opraveni);
            time =  kol.getCasPrijezduSklad();
            if(u != 0) {
                //nastavime false pro opravy na dalsi pruchod
                opraveni = false;
            }

        }
        //time nastavime zase na puvodni cas
        time = temp;
        //nastaveni na false
        preskocPricteniPozadavku = false;
    }
    /**
     * Metoda rozlozi pozadavek ktery nedokaze zpracovat jedno kolecko do deadlinu (ani kdyz pojede vicekrat)
     * na 2 dalsi pozadavky ve stejnem case, stejnym poradim, stejnym cilovym zakaznikem, jako pozadujici pytle do prvniho,
     * ale nastavi polovinu mnozstvi puvodniho a do druheho zbytek
     * @param kol (Kolecko)
     * @param fronta (Prioritni_fronta)
     */
    public static void rozlozPozadavek(Kolecko kol, PrioritniFronta fronta) {
        //Kolecko nikam nepojede a zustane na skladu
        kol.setCisloPozadavku(-4);
        kol.setCasPrijezduSklad(fronta.get().getTz());
        //vime ze pozadavek se uz v budoucnu zpracuje
        if(!fronta.get().getRozlozeny()) {
            zpracovanePozadavky++;
        }
        //vypocet pytlu pro rozdelene pozadavky
        int pytleVPuvodnimPozadavku = fronta.get().getKp();
        int prvniPozadavek = pytleVPuvodnimPozadavku/2;
        int druhyPozadavek = pytleVPuvodnimPozadavku - prvniPozadavek;
        //prvni rozdeleny pozadavek
        Pozadavek pozadavek1 = new Pozadavek(fronta.get().getTz(), fronta.get().getZp(), prvniPozadavek, fronta.get().getTp() );
        pozadavek1.setPoradi(fronta.get().getPoradi());
        pozadavek1.setRozlozeny(true);
        fronta.add(pozadavek1);
        //druhy rozdeleny pozadavek
        Pozadavek pozadavek2 = new Pozadavek(fronta.get().getTz(), fronta.get().getZp(), druhyPozadavek, fronta.get().getTp() );
        pozadavek2.setPoradi(fronta.get().getPoradi());
        pozadavek2.setRozlozeny(true);
        fronta.add(pozadavek2);
    }

    /**
     * metoda, jenz vypisuje opravu kolecka a dodava kolecku vydrz
     * @param kol (Kolecko)
     * @param fronta (PrioritniFronta)
     * @param vypisy (PrioritniFrontaVypis)
     */
    public static void opravKolecko(Kolecko kol, PrioritniFronta fronta, PrioritniFrontaVypis vypisy){
        //opravujeme kolečko
        kol.setAktualniVydrz(kol.getVydrz());

        //Pouze informujici vypis o oprave  time + doba opravy
        Vypis oprava = new Vypis(fronta.get().getTz(),"Cas: " + Math.round(fronta.get().getTz()) + ", Kolecko: " + kol.getPoradi() + ", Zakaznik: "
                + fronta.get().getZp() + ", " + kol.getDruh() + " vyzaduje udrzbu, Pokracovani mozne v: " +
                Math.round(fronta.get().getTz()+kol.getTd()), false, fronta.get().getPoradi(), kol.getPoradi(),false, true, false, false, false);
        vypisy.add(oprava);
    }
}