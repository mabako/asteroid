package shapes;

import java.util.ArrayList;

/**
 * Klasse Rechteck, mit Verankerungspunkt topLeft und Länge und Breite
 * 
 * @author Marcus Bauer
 * @version 201103231203
 */
public class Rectangle extends Polygon
{
	/**
	 * Konstruktor für Objekte der Klasse Linie
	 * 
	 * @param bottomLeft
	 *            linker unterer Eckpunkt
	 * @param lx
	 *            Länge der Seiten in x-Achse
	 * @param ly
	 *            Länge der Seiten in y-Achse
	 */
	public Rectangle( Point bottomLeft, int lx, int ly )
	{
		ArrayList< Point > p = new ArrayList< Point >( );
		p.add( bottomLeft );
		p.add( new Point( bottomLeft.getX( ) + lx, bottomLeft.getY( ) ) );
		p.add( new Point( bottomLeft.getX( ) + lx, bottomLeft.getY( ) + ly ) );
		p.add( new Point( bottomLeft.getX( ), bottomLeft.getY( ) + ly ) );
		try
		{
			setPoints( p );
		}
		catch( PolygonShapeException e )
		{
			assert false : "Rectangle erstellen ist fehlgeschlagen, obwohl dies nie passieren sollte";
		}
	}
}
