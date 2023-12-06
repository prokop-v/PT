/**
 * Sablona pro vytvareni cest
 * @author Vaclav Prokop, Filip Valtr
 */
public class Cesta {

//== Konstantni atributy instanci

	/**
	 * index zakaznika (zakaznici jsou v rozmezi ((S+1),...,(S+Z))
	 */
	private final int i;
	/**
	 * index skladu (sklady jsou v rozmezi 1,..,S)
	 */
	private final int j;
	

//== Konstruktor

	/**
	 * Konstruktor pro vytvareni instanci
	 * @param i (int)
	 * @param j (int)
	 */
	public Cesta(int i, int j) {
		this.i = i;
		this.j = j;
	}
//== Getry, setry k atributum

	/**
	 * Vrati index prislusneho zakaznika
	 * @return i (int)
	 */
	public int getI() {
		return i;
	}

	/**
	 * Vrati index skladu ze ktereho vede cesta k zakaznikovi
	 * @return j (int)
	 */
	public int getJ() {
		return j;
	}

	/**
	 * Vrati informace o ceste
	 * @return String
	 */
	public String toString() {
		return "Cesta [i = "+ this.i + " j = "+ this.j+ "]";
	}

}