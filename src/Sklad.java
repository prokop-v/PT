/**
 * Sablona pro vytvareni skladu
 * @author Vaclav Prokop, Filip Valtr
 *
 */
public class Sklad {
//== Atributy instanci

	/**
	 * informace o tom kolik pytlu se ve skladu aktualne nachazi
	 */
	private int aktualniKS;

//== Konstantni atributy instanci
	/**
	 * cas pro doplneni pytlu do skladu
	 */
	private final double ts;
	/**
	 * cas za ktery jsou pytle na kolecko nalozeny/vylozeny
	 */
	private final double tn;
	/**
	 *  pocet kusu pytlu ktere jsou vzdy po uplynuti doby ts znovu doplneny
	 */
	private final int ks;
	/**
	 *  x-ova souradnice skladu
	 */
	private final double xS;
	/**
	 *  y-ova souradnice skladu
	 */
	private final double yS;
	/**
	 * cas posledniho doplneni skladu
	 */
	private double casPoslednihoDoplneniSkladu;
	/**
	 * kontroluje prvni doplneni
	 */
	private boolean prvniDoplneni = false;

//== Konstruktor
	/**
	 * Konstruktor tridy, ktery vytvari instanci
	 * @param xS (double)
	 * @param yS (double)
	 * @param ks (int)
	 * @param ts (double)
	 * @param tn (double)
	 */
	public Sklad(double xS, double yS, int ks, double  ts, double tn) {
		this.xS = xS;
		this.yS = yS;
		this.ks = ks;
		this.aktualniKS = ks;
		this.ts = ts;
		this.tn  = tn;
	}
	
//== Getry, setry k atributum

	/**
	 * Vrati x-ovou souradnici skladu
	 * @return xS (double)
	 */
	public double getXs() {
		return this.xS;
	}

	/**
	 * Vrati y-ovou souradnici skladu
	 * @return yS (double)
	 */
	public double getYs() {
		return this.yS;
	}

	/**
	 * Vrati pocet kusu pytlu ktere byly doplneny po vyprseni intervalu ts
	 * @return ks (int)
	 */
	public int getKs() {
		return this.ks;
	}
	
	/**
	 * Vrati casovy interval za ktery dojde k doplneni skladu
	 * @return ts (double)
	 */
	public double getTs() {
		return this.ts;
	}
	
	/**
	 * Vrati cas potrebny pro nalozeni/vylozeni kolecka
	 * @return tn (double)
	 */
	public double getTn() {
		return this.tn;
	}

	/**
	 * Vrati pocet pytlu ktere jsou ve skladu
	 * @return	aktualniKS (int)
	 */
	public int getAktualniKS() {
		return aktualniKS;
	}

	/**
	 * Nastavi aktualni pocet pytlu ve skladu
	 * @param aktualniKS (int)
	 */
	public void setAktualniKS(int aktualniKS) {
		this.aktualniKS = aktualniKS;
	}

	@Override
	public String toString() {
		return "X souradnice " + xS + " Y souradnice " + yS + " pocet pytlu: " + ks + " cas obnovy " + ts + " cas nalozeni/vylozeni:" + tn ;
	}

	/**
	 * Metoda zkontroluje jestli uz nastal cas, kdy se do skladu doplni pytle
	 * Pokud ano tak k aktualnim pytlum na sklade pricte pytle
	 * @param time cas simulace
	 */
	public void zkontrolujDoplneniSkladu(double time) {
		//slouzi pro ulozeni kolikrat melo k dojit k doplneni skladu
		int nasobnostDoplneniSkladu = 0 ;

		// doba po kterou se nekontrolovalo doplneni skladu
		double mezicas = time - casPoslednihoDoplneniSkladu;

		//pomocny boolean
		boolean dopln = false;
		
		//test pro pripad ze jeste nedoslo k dopleni skladu
		if (time >= ts && !prvniDoplneni ) {
			dopln = true;
		}
		
		//test jestli ma dojit k doplneni skladu
		if( (time - mezicas) >= ts || dopln) {
			
			//cyklus odecita od mezicas ts dokud mezicas neni mensi nez ts
			while( mezicas >= ts ) {
				mezicas = mezicas - ts;
				//zvyseni nasobnosti
				nasobnostDoplneniSkladu++;
			}
			double cas = casPoslednihoDoplneniSkladu;
			//dopocteni posledniho doplneni skladu
			casPoslednihoDoplneniSkladu = casPoslednihoDoplneniSkladu + (nasobnostDoplneniSkladu*ts);
			
			//doplneni pytlu a vypisovani kontrolnich vypisu
			while(nasobnostDoplneniSkladu != 0) {
				nasobnostDoplneniSkladu--;
				cas = cas + ts;
				//mezicas = mezicas + ts;
				setAktualniKS(getAktualniKS() + getKs());
			}	
		    prvniDoplneni = true;
		}
	}
}

