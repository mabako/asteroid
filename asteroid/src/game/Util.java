package game;

import shapes.Point;

/**
 * Hilfsfunktionen für das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105252240
 */
public class Util
{
	/**
	 * Berechnet den Punkt, der von (0, 0) mit einem bestimmten Radius in einem
	 * bestimmten Winkel anliegt
	 * 
	 * @param distance
	 *            Entfernung, die der neue Punkt haben soll
	 * @param winkel
	 *            Winkel
	 * @return der neue Punkt
	 */
	public static Point getPointInFrontOf( double distance, double angle )
	{
		// Winkel konvertieren
		double radians = Math.toRadians( angle );

		double px = distance * Math.sin( radians );
		double py = distance * Math.cos( radians );

		// und zum Polygon hinzufügen
		return new Point( px, py );
	}

	/**
	 * Berechnet den Punkt, der von gegebenem Ursprung mit einem bestimmten
	 * Radius in einem bestimmten Winkel anliegt
	 * 
	 * @param center
	 *            der Ursprung
	 * @param distance
	 *            Entfernung, die der neue Punkt haben soll
	 * @param winkel
	 *            Winkel
	 * @return der neue Punkt
	 */
	public static Point getPointInFrontOf( Point center, double distance, double angle )
	{
		if( center == null )
			throw new IllegalArgumentException( "Punkt, von dem ausgehend der nächste Punkt berechnet werden soll, ist null." );
		return getPointInFrontOf( distance, angle ).move( center.getX( ), center.getY( ) );
	}
}
