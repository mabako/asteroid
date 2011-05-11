package shapes;

/**
 * Abstrakte Klasse, die ein Objekt auf einer Zeichenfläche zeichnet und bewegt
 * 
 * @author Marcus Bauer
 * @version 201103231934
 */
public abstract class Drawable
{
	/**
	 * zeichnet ein Objekt auf der Zeichenfläche
	 */
	public abstract void draw( );

	/**
	 * bewegt ein Objekt auf der Zeichenfläche um dx in Richtung x-Achse und um
	 * dy in Richtung y-Achse
	 * 
	 * @param dx
	 *            distanz in x
	 * @param dy
	 *            distanz in y
	 * @return the drawable
	 */
	public abstract Drawable move( double dx, double dy );

	/**
	 * Gibt den ersten Punkt zurück
	 * 
	 * @return 1. Eckpunkt des Objektes, oder (0, 0) falls keiner
	 */
	public abstract Point getFirstPoint( );

	/**
	 * Vergleicht ob die Objekte gleich sind, wenn verschoben
	 * 
	 * @param other
	 *            das Vergleichsobjekt
	 * @param delta
	 *            Differenz zwischen beiden Figuren
	 * @return true falls gleich, false sonst
	 */
	public abstract boolean equalsRelative( Drawable other, Point delta );

	/**
	 * Dreht das Objekt um einen bestimmten Winkel
	 * 
	 * @param center
	 *            Punkt, der als Ursprung für die Drehung dient
	 * @param phi
	 *            Winkel, um den der Punkt gedreht werden soll
	 * @throws IllegalArgumentException
	 *             , falls der Mittelpunkt ungültig ist.
	 */
	public abstract void rotate( Point center, double phi );
}
