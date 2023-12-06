/**
 * Sablona pro vytvareni zakazniku
 * @author Vaclav Prokop, Filip Valtr
 */
public class Zakaznik {
//== Konstantni atributy instanci
	/**
	 * x-ova souradnice zakaznika
	 */
	private final double xZ;
	/**
	 * y-ova souradnice zakaznika
	 */
	private final double yZ;

//== Konstruktor
	/**
	 * Konstruktor pro vytvareni instanci
	 * @param xZ - x-ova souradnice zakaznika
	 * @param yZ - y-ova souradnice zakaznika
	 */
	public Zakaznik(double xZ, double yZ) {
		this.xZ = xZ;
		this.yZ = yZ;
	}
//== Getry, setry k atributum

	/**
	 * Vrati x-ovou souradnici zakaznika
	 * @return xZ (double)
	 */
	public double getXz() {
		return xZ;
	}
	/**
	 * Vrati y-ovou souradnici zakaznika
	 * @return yZ (double)
	 */
	public double getYz() {
		return yZ;
	}
	@Override
	public String toString() {
		return "Zakaznik [xZ=" + xZ + ", yZ=" + yZ + "]";
	}
}
