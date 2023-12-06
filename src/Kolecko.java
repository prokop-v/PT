/**
 * Sablona pro vytvareni kolecek
 * @author Vaclav Prokop, Filip Valtr
 */
class Kolecko{

    //== Konstantni atributy instanci

    /**
     * udava nazev kolecka
     */
	public final String druh;
    /**
     * udava rychlost kolecka
     */
	public final double v;
    /**
     * hodnota, ktera uklada celkovou vydrz kolecka
     */
	public final double vydrz;
    /**
     * udava maximalni zatizeni kolecka
     */
	public final int kd;
    /**
     * udava kolik casu kolecko potrebuje pro provedeni udrzby
     */
	public final double td;

    //== Atributy instanci

    /**
     * atribut obsahujici cilovou destinaci
     */
    public int cil;
    /**
     * aktualni pozice kolecka
     */
    public int pozice;
    /**
     * cislo pozadavku, o ktere se kolecko stara
     */
    public int cisloPozadavku;
    /**
     * pocet pytlu, ktery dane kolecko prevazi
     */
    public int aktualnePytlu;
    /**
     * cas odjezdu kolecka ze skladu
     */
    public double casOdjezdu;
    /**
     * cas prijezdu kolecka k zakaznikovi
     */
    public double casPrijezduZak;
    /**
     * cas navratu kolecka zpatky do skladu
     */
    public double casPrijezduSklad;
    /**
     * obsahuje aktualni vydrz vozidla, ta se cestou snizuje
     */
    public double aktualniVydrz;
    /**
     * udrzuje informaci o tom, kolik kolecek jiz bylo vygenerovano
     */
    public static int pocet = 0;

    /**
     * udava poradi kolecka
     */
    public int poradi;

    //== Konstruktor

    /**
     * Konstruktor pro vytvareni instanci tridy
     * @param druh (String)
     * @param v (double)
     * @param vydrz (double)
     * @param kd (int)
     * @param td (double)
     */
	public Kolecko(String druh, double v, double vydrz, int kd, double td){
        pocet++;
        poradi = pocet;
        this.druh = druh;
        this.v = v;
        this.vydrz = vydrz;
        this.kd = kd;
        this.td = td;
        this.aktualniVydrz = vydrz;
	}

    //== Getry, setry k atributum instanci

    /**
     * Vrati poradi kolecka
     * @return poradi (int)
     */
    public int getPoradi() {
		return poradi;
	}

    /**
     * Vrati druh kolecka
     * @return druh (String)
     */
	public String getDruh() {
    	return druh;
    }

    /**
     * Vrati rychlost kolecka
     * @return v (double)
     */
    public double getV() {
        return v;
    }

    /**
     * Vrati celkovou vydrz kolecka
     * @return vydrz (double)
     */
    public double getVydrz() {
        return vydrz;
    }

    /**
     * Vraci maximalni zatizeni kolecka
     * @return kd (int)
     */
    public int getKd() {
        return kd;
    }

    /**
     * Vraci cas, ktery kolecko potrebuje pro provedeni udrzby
     * @return td (int)
     */
    public double getTd() {
        return td;
    }
    /**
     * Vrati matersky sklad kolecka
     */
    public int getPozice() {
    	return this.pozice;
    }
    /**
     * Nastavuje aktualni pozici kolecka
     * @param pozice (int)
     */
    public void setPozice(int pozice) {
        this.pozice = pozice;
    }

    /**
     * Vraci cilovou destinaci kolecka
     * @return cil (int)
     */
    public int getCil() {
        return cil;
    }

    /**
     * Nastavuje cilovou destinaci kolecka
     * @param cil (int)
     */
    public void setCil(int cil) {
        this.cil = cil;
    }

    /**
     * Nastavi kolecku o jaky pozadavek se stara
     * @param cisloPozadavku (int)
     */
    public void setCisloPozadavku(int cisloPozadavku) {
        this.cisloPozadavku = cisloPozadavku;
    }

    /**
     * Nastavi kolecku o jaky pozadavek se stara
     * @return cisloPozdavku (int)
     */
    public int getCisloPozadavku() {
        return cisloPozadavku;
    }

    /**
     * Vrati pocet nalozenych pytlu
     * @return aktualnePytlu (int)
     */
    public int getAktualnePytlu() {
		return aktualnePytlu;
	}

    /**
     * Nastavi aktualni pocet pytlu na kolecku
     * @param aktualnePytlu (int)
     */
	public void setAktualnePytlu(int aktualnePytlu) {
		this.aktualnePytlu = aktualnePytlu;
	}

    /**
     * Vrati cas odjezdu kolecka ze skladu
     * @return casOdjezdu (double)
     */
	public double getCasOdjezdu() {
		return casOdjezdu;
	}

    /**
     * Nastavi cas odjezdu kolecka
     * @param casOdjezdu (double)
     */
	public void setCasOdjezdu(double casOdjezdu) {
		this.casOdjezdu = casOdjezdu;
	}

    /**
     * Vrati, kdy ma kolecko dojet k zakaznikovi (jeho index)
     * @return casPrijezduZak (double)
     */
	public double getCasPrijezduZak() {
		return casPrijezduZak;
	}

    /**
     * Nastavi cas, kdy kolecko dojede k zakaznikovi
     * @param casPrijezduZak (double)
     */
	public void setCasPrijezduZak(double casPrijezduZak) {
		this.casPrijezduZak = casPrijezduZak;
	}
	/**
	 * Vrati cas prijezdu kolecka na sklad
	 * @return casPrijezduSklad (double)
	 */
	public double getCasPrijezduSklad() {
		return casPrijezduSklad;
	}
    /**
     * Nastavi cas navratu na sklad
     * @param casPrijezduSklad (double)
     */
	public void setCasPrijezduSklad(double casPrijezduSklad) {
		this.casPrijezduSklad = casPrijezduSklad;
	}

    /**
     * Vrati aktualni vydrz kolecka
     * @return aktualniVydrz (double)
     */
	public double getAktualniVydrz() {
		return aktualniVydrz;
	}

    /**
     * Nastavi aktualni vydrz kolecka
     * @param aktualniVydrz (double)
     */
	public void setAktualniVydrz(double aktualniVydrz) {
		this.aktualniVydrz = aktualniVydrz;
	}

    /**
     * Metoda toString vypisujici udaje o danem kolecku
     * @return String
     */
	@Override
	public String toString() {
		return "Kolecko [druh=" + druh + ", v=" + v + ", vydrz=" + vydrz + ", kd=" + kd + ", td=" + td + "]";
	}
}