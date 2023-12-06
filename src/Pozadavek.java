/**
 * Sablona pro vytvareni pozadavku
 * @author Vaclav Prokop, Filip Valtr
 *
 */
public class Pozadavek implements Comparable<Pozadavek> {

//== Atributy instanci

	/**
	 * cas prichodu pozadavku
	 */
	private double tz;
	
//== Konstantni atributy instanci

	/**
	 * doba do ktere musi byt pytle vylozeny u zakaznika
	 */
	private final double tp;

	/**
	 * index zakaznika kteremu prislusy pozadavek
	 */
	private final int zp;
	/**
	 * mnozstvi pytlu ktere zakaznik pozaduje
	 */
	private final int kp;
	/**
	 * udava poradi daneho pozadavku
	 */
	private int poradi;
	/**
	 * cas, do kdy musi byt pozadavek splnen
	 */
	private double deadline;
	/**
	 * boolean zdali ma byt pozadavek odsunut
	 */
	private boolean odsunuty;
	/**
	 * Deleni pozadavku na vice pozadavku s mene pytlema
	 */
	private boolean rozlozeny = false;

//== Tridni atributy

	/**
	 * udrzuje pocet pozadavku
	 */
	private static int pocet = 0;
	
//== Konstruktor

	/**
	 * Kontruktor tridy pro vytvareni instanci
	 * @param tz (double)
	 * @param zp (int)
	 * @param kp (int)
	 * @param tp (double)
	 */
	public Pozadavek(double tz, int zp, int kp, double tp) {
		poradi = pocet + 1;
		pocet++;
		this.tz = tz;
		this.zp = zp;
		this.kp = kp;
		this.tp = tp;
		
		//kdyz se vytvori pozadavek vypocte se i deadline
		deadline = tz + tp;
		odsunuty = false;
	}
	
//== Getry, setry k atributum

	/**
	 * Vrati cas prichodu pozadavku
	 * @return tz (double)
	 */
	public double getTz() {
		return tz;
	}

	/**
	 * Nastavi poradi pozadavku
	 * @param tz (double)
	 */
	public void setTz(double tz) {
		 this.tz = tz;
	}

	/**
	 * Vrati index zakaznika prislusneho pozadavku
	 * @return zp (int)
	 */
	public int getZp() {
		return zp;
	}

	/**
	 * Vrati mnozstvi pytlu, ktere zakaznik pozaduje
	 * @return kp (int)
	 */
	public int getKp() {
		return kp;
	}

	/**
	 * Vrati cas kdy pytle musi byt u zakaznika doruceny
	 * @return tp (double)
	 */
	public double getTp() {
		return tp;
	}

	/**
	 * Vrati poradi pozadavku
	 * @return poradi (int)
	 */
	public int getPoradi() {
		return poradi;
	}

	/**
	 * Nastavi poradi pozadavku
	 * @param poradi (int)
	 */
	public void setPoradi(int poradi) {
		this.poradi = poradi;
	}

	/**
	 * Vrati cas, do kdy ma byt pozadavek splnen
	 * @return deadline (double)
	 */
	public double getDeadline() {
		return deadline;
	}

	/**
	 * Nastavi cas, do kdy ma byt pozadavek splnen
	 * @param deadline (double)
	 */
	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}

	/**
	 * Nastavi posunuti pozadavku
	 * @param odsunuty (boolean)
	 */
	public void setOdsunuty(boolean odsunuty) {
		this.odsunuty = odsunuty;
	}
	/**
	 * Vrati rozlozeni pozadavku, true pokud je jeden z rozlozenych, false pokud neni 
	 * @return true, kdyz je odsunut, false kdyz ne.
	 */
	public boolean getRozlozeny() {
		return rozlozeny;
	}
	/**
	 * nastavi rozlozeni
	 * @param rozlozeny true, kdyz je rozlozeny, false pokud nikoliv.
	 */
	public void setRozlozeny(boolean rozlozeny) {
		this.rozlozeny = rozlozeny;
	}

	/**
	 * zjisti, jestli je pozadavek odsunuty
	 * @return (boolean)
	 */
	public boolean isOdsunuty() {
		return odsunuty;
	}

	//==Verejne metody instanci

	/**
	 * Vypise pozadavek v pozadovanym formatu
	 * @return (String)
	 */
	public String vypisPozadavek() {
		if(odsunuty){
			return "Cas: "+ Math.round(this.tz) +", Pozadavek "+ poradi +", Zakaznik: "+this.zp+", Pocet pytlu: "+this.kp+", Deadline: "+Math.round(this.deadline) +
					" \n  -> Nemame pytle tzn. musi se cekat na doplneni skladu -> zpracovani pozadavku "+ poradi+" je odsunuto";
		}
		return "Cas: "+ Math.round(this.tz) +", Pozadavek "+ poradi +", Zakaznik: "+this.zp+", Pocet pytlu: "+this.kp+", Deadline: "+Math.round(this.deadline);
	}

	@Override
	public String toString() {
		return "Pozadavek [tz=" + tz + ", zp=" + zp + ", kp=" + kp + ", tp=" + tp + "]";
	}

	/**
	 * compareTo metoda pro porovnavani casu pozadavku v prioritni fronte a nasledne jejich serazeni
	 * @param p objekt, se kterym se porovnava
	 * @return (int)
	 */
	@Override
	public int compareTo(Pozadavek p) {
		if(this.tz > p.tz) {
			return 1;
		}
		else if (this.tz < p.tz) {
			return -1;
		}
		else{
			if(this.poradi > p.poradi) {
				return 1;
			}
			else if(this.poradi < p.poradi){
				return -1;
			}
			else {
				return 0;
			}
			
		}
	}
}
