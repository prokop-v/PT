/**
 * Interface pro vytvareni grafu
 * @author Vaclav Prokop, Filip Valtr
 */
public interface IGraf {
    /**
     * Vypocita danou mezi dvema vrcholu, ktery mezi sebou maji hranu
     * @param x1 (x souradnice 1. vrcholu)
     * @param x2 (x souradnice 2. vrcholu)
     * @param y1 (y souradnice 1. vrcholu)
     * @param y2 (y souradnice 2. vrcholu)
     * @return vysledek (double)
     */
    public double vzdalenost(double x1, double x2, double y1, double y2);

    /**
     * Metoda pridejHranu mezi vrcholy prida neorientovanou hranu s danou cenou cesty
     * @param i (int)
     * @param j (int)
     * @param cena (double)
     */
    public void pridejHranu(int i, int j, double cena);
}