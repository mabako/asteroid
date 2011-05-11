package shapes;

/**
 * Ein Punkt in einem 2-dimensionalen Koordinatensystem
 * 
 * @author Marcus Bauer
 * @version 201104220244
 */
final public class Point
{
	/** x-Koordinate */
	private double x;

	/** y-Koordinate */
	private double y;

	/**
	 * Maximales Delta, um das sich zwei Koordinaten unterscheiden dürfen, um
	 * als gleich zu gelten
	 */
	public static final double DELTA = 1e-10;

	/**
	 * Konstruktor für Objekt der Klasse Point
	 * 
	 * @param x
	 *            x-Koordinate
	 * @param y
	 *            y-Koordinate
	 */
	public Point( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public double getX( )
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY( )
	{
		return y;
	}

	/**
	 * bewege den Punkt um dx, dy
	 * 
	 * @param dx
	 *            Distanz in x
	 * @param dy
	 *            Distanz in y
	 * @return den Punkt
	 */
	public Point move( double dx, double dy )
	{
		this.x += dx;
		this.y += dy;
		return this;
	}

	/**
	 * Erzeugt eine Kopie
	 */
	public Point copy( )
	{
		return new Point( x, y );
	}

	/**
	 * Dreht den Punkt im Uhrzeigersinn (d.h. entgegen der normalen Quadranten)
	 * 
	 * @param center
	 *            Punkt, der als Ursprung für die Drehung dient
	 * @param phi
	 *            Winkel, um den der Punkt gedreht werden soll
	 * @throws IllegalArgumentException
	 *             , falls der Mittelpunkt ungültig ist.
	 */
	public void rotate( Point center, double phi )
	{
		if( center == null )
			throw new IllegalArgumentException( "Punkt, um den gedreht werden soll, ist null" );

		// Winkel umrechnen
		double rad = Math.toRadians( phi );

		// In Koordinatenursprung verschieben
		x -= center.getX( );
		y -= center.getY( );

		// Koordinaten nach Drehung berechnen
		double x2 = x * Math.cos( rad ) + y * Math.sin( rad );
		double y2 = -x * Math.sin( rad ) + y * Math.cos( rad );

		// Verschieben rückgängig machen, neue Koordinaten setzen
		x = x2 + center.getX( );
		y = y2 + center.getY( );
	}

	/**
	 * Gibt den Hashwert des Punktes zurück
	 * 
	 * @return Hashwert
	 */
	@Override
	public int hashCode( )
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits( x );
		result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
		temp = Double.doubleToLongBits( y );
		result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
		return result;
	}

	/**
	 * Überprüft, ob der angegebene Punkt dieselben Koordinaten hat
	 * 
	 * @param obj
	 *            der andere Punkt
	 */
	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( !( obj instanceof Point ) )
			return false;
		Point other = (Point) obj;
		if( Math.abs( x - other.x ) > Point.DELTA )
			return false;
		if( Math.abs( y - other.y ) > Point.DELTA )
			return false;
		return true;
	}
}
