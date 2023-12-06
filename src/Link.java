/**
 * Sablona pro vytvareni spojoveho seznamu
 * @author Vaclav Prokop, Filip Valtr
 */
public class Link {

//== Atributy instanci
    /**
     * soused vrcholu
     */
     public int soused;
    /**
     * Cena hrany vedoucí k sousedovi
     */
    public double cena;
    /**
     * Odkaz na dalšího souseda v seznamu
     */
    public Link next;

    /**
     * Konstruktor třídy Link pro inicializaci atributů
     * @param soused (int)
     * @param next (Link)
     * @param cena (double)
     */
     public Link(int soused, Link next, double cena) {
        this.soused = soused;
        this.next = next;
        this.cena = cena;
    }
}