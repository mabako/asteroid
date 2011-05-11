package shapes;

import java.util.ArrayList;

/**
 * Eine Linie
 * 
 * @author Marcus Bauer
 * @version 201103231203
 */
public class Line extends Polygon
{
	/**
	 * Konstruktor für Objekte der Klasse Linie
	 * 
	 * @param start
	 *            Startpunkt
	 * @param end
	 *            Endpunkt
	 */
	public Line( Point start, Point end )
	{
		ArrayList< Point > p = new ArrayList< Point >( );
		p.add( start );
		p.add( end );
		try
		{
			setPoints( p );
		}
		catch( PolygonShapeException e )
		{
			assert false : "Line erstellen ist fehlgeschlagen, obwohl dies nie passieren sollte";
		}

	}
}
