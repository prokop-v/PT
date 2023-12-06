/**
 * Sablona pro ukladani vrcholu a jejich cen k vytvoreni kuk na kolecko
 * @author Vaclav Prokop, Filip Valtr
 *
 */
public class VrcholCena {

	//== Atributy instanci
	/**
	 * udrzuje cenu k vrcholu
	 */
	public double cena;
	/**
	 * udrzuje vrchol, o ktery se jedna
	 */
	public int vrchol;

	//==Kontruktor
	/**
	 * Kontruktor tridy VrcholCena, ktera slouzi k uchovani vrcholu vedoucich nejkratsi cestou k cilovemu mistu
	 * @param vrchol (int)
	 * @param cena (double)
	 */
	public VrcholCena(int vrchol, double cena) {
		this.cena = cena;
		this.vrchol = vrchol;
	}
}