import java.util.Random;

/**
 * Sablona pro vytvareni kolecek
 * @author Vaclav Prokop, Filip Valtr
 *
 */
public class DruhKolecka implements Comparable<DruhKolecka>{

//== Atributy instanci

	/**
	 * udava kolik casu kolecko potrebuje pro provedeni udrzby
	 */
	private final double td;
	/**
	 * udava maximalni zatizeni kolecka
	 */
	private final int kd;
	
	
//== Konstantni atributy instanci

	/**
	 * textovy retezec reprezentujici druh kolecka
	 */
	private final String druh;
	/**
	 * minimalni rychlost kolecka
	 */
	private final double vMin;
	/**
	 * maximalni rychlost kolecka
	 */
	private final double vMax;
	/**
	 * konstantni rychlost kolecka
	 */
	private final double v;
	/**
	 * minimalni vzdalenost kterou muze kolecko prekonat
	 */
	private final double dMin;
	/**
	 * maximalni vzdalenost kterou muze kolecko prekonat
	 */
	private final double dMax;
	/**
	 * cas udavajici dobu nez kolecko bude potrebovat opravit
	 */
	private final double vydrz;
	/**
	 * udava procentualni zastoupeni druhu kolecka ve vozovem parku firmy
	 */
	private final double pd;
	
//== Konstruktor

	/**
	 * Konstruktor pro vytvareni instanci
	 * @param druh (String)
	 * @param vMin (double)
	 * @param vMax (double)
	 * @param dMin (double)
	 * @param dMax (double)
	 * @param td (double)
	 * @param kd (int)
	 * @param pd (double)
	 */
	public DruhKolecka(String druh, double vMin, double vMax, double dMin, double dMax, double td, int kd, double pd) {

		this.druh = druh;
		this.vMin = vMin;
		this.vMax = vMax;
		
		Random r = new Random();

		//Vypocet rychlosti podle rovnomerneho rozdeleni
		this.v = vMin + (vMax - vMin) * r.nextDouble();
		this.dMin = dMin;
		this.dMax = dMax;
		
		double strHod = (this.dMin + this.dMax) / 2;
		double smerOdchyl = (this.dMax - this.dMin) / 4;
		double odecteniOdchylky = strHod - smerOdchyl;
		double pricteniOdchylky = strHod + smerOdchyl;

		//vypocteni vydrze pomoci normalniho rozdeleni se stredni hodnotou a smerodatnou odchylkou
		this.vydrz = odecteniOdchylky + (pricteniOdchylky - odecteniOdchylky) * r.nextDouble();
		this.td = td;
		this.kd = kd;
		this.pd = pd;
	}
	
//== Getry, setry k atributum instanci

	/**
	 * Vrati druh kolecka
	 * @return druh (String)
	 */
	public String getDruh() {
		return druh;
	}

	/**
	 * Vrati konstantni rychlost kolecka, kterou se pohybuje
	 * @return v (double)
	 */
	public double getV() {
		return v;
	}

	/**
	 * Vrati cas potrebny pro provedeni udrzby
	 * @return td (double)
	 */
	public double getTd() {
		return td;
	}

	/**
	 * Vrati maximalni nosnost kolecka
	 * @return kd (int)
	 */
	public int getKd() {
		return kd;
	}

	/**
	 * Vrati procentualni zastoupeni druhu kolecka 
	 * @return pd (double)
	 */
	public double getPd() {
		return pd;
	}

	/**
	 * Vrati cas udavajici nez se kolecko opotreubje a bude potrebovat provest udrzbu
	 * @return vydrz (double)
	 */
	public double getVydrz() {
		return vydrz;
	}

	@Override
	public String toString() {
		return "Kolecko [, druh=" + druh + ", vMin=" + vMin + ", vMax=" + vMax + ", dMin="
				+ dMin + ", dMax=" + dMax + " td=" + td + ", kd=" + kd +" , pd=" + pd + "]";
	}

	/**
	 * compareTo metoda pro porovnavani casu kolecek v prioritni fronte a nasledne jejich serazeni
	 * @param druh objekt, se kterym se porovnava
	 * @return (int)
	 */
	@Override
	public int compareTo(DruhKolecka druh) {
		if(this.pd < druh.pd) {
			return 1;
		}else if (this.pd > druh.pd) {
			return -1;
		}else{
			return 0;
		}
	}
}