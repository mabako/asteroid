package shapes;

import java.util.ArrayList;

/**
 * Basisklasse für Polygone, die die Eckpunkte verwaltet
 * 
 * @author Marcus Bauer
 * @version 201103232101
 */
public class Polygon extends Shape
{
	/** Liste der Punkte */
	protected ArrayList< Point > points = new ArrayList< Point >( );

	/**
	 * Standard-Konstruktor
	 */
	public Polygon( )
	{

	}

	/**
	 * Zeichnet das Polygon auf das Whiteboard
	 */
	@Override
	public void draw( )
	{
		// Altes Bild löschen
		getWhiteBoard( ).removeShape( representation );

		// Punkte laden
		double[ ] x = new double[ points.size( ) ];
		double[ ] y = new double[ points.size( ) ];
		for( int i = 0; i < points.size( ); ++i )
		{
			x[i] = points.get( i ).getX( );
			y[i] = points.get( i ).getY( );
		}

		// Polygon erstellen
		representation = getWhiteBoard( ).drawPolygon( x, y, getColor( ), isSolid( ), 0 );
	}

	/**
	 * Zugriff auf die Punkte der Form
	 * 
	 * @return nicht veraenderbare Liste der Punkte
	 */
	public ArrayList< Point > getPoints( )
	{
		// Temporäre Liste anlegen
		ArrayList< Point > temp = new ArrayList< Point >( );

		// Kopien aller Punkte hinzufügen
		for( Point p : points )
			temp.add( p.copy( ) );

		// Liste zurückliefern
		return temp;
	}

	/**
	 * bewege des Polygon um dx, dy
	 * 
	 * @param dx
	 *            Distanz in x
	 * @param dy
	 *            Distanz in y
	 */
	@Override
	public Polygon move( double dx, double dy )
	{
		// Alle Punkte bewegen
		for( Point point : points )
			point.move( dx, dy );

		// Neu zeichnen
		draw( );
		return this;
	}

	/**
	 * Setze die Eckpunkte des Polygons
	 * 
	 * @param p
	 *            neue Eckpunkte
	 * @throws PolygonShapeException
	 *             falls nicht mindestens zwei Punkte übergeben wurden
	 */
	public void setPoints( ArrayList< Point > points ) throws PolygonShapeException
	{
		// Liste muss mindestens zwei Punkte enthalten
		if( points.size( ) < 2 )
			throw new PolygonShapeException( "Nur " + points.size( ) + " Punkte erhalten - müssen mindestens 2 sein" );

		// Alle alten Punkte löschen
		this.points.clear( );

		// Alle Punkte übernehmen
		for( Point p : points )
			this.points.add( p.copy( ) );

		draw( );
	}

	/**
	 * Liefert den ersten Punkt zurück, oder (0, 0) falls es keinen Punkt im
	 * Polygon gibt
	 */
	@Override
	public Point getFirstPoint( )
	{
		if( points.size( ) == 0 )
			return new Point( 0, 0 );

		return points.get( 0 ).copy( );
	}

	/**
	 * Vergleicht ob die Polygone gleich sind, wenn alle Punkte dieses Polygons
	 * um delta verschoben werden
	 * 
	 * @param other
	 *            das Vergleichsobjekt
	 * @param delta
	 *            Differenz zwischen beiden Figuren
	 * @return true falls gleich, false sonst
	 */
	@Override
	public boolean equalsRelative( Drawable other, Point delta )
	{
		// TODO: Punkte müssen in der gleichen Reihenfolge vorkommen, sollten
		// jedoch verschoben sein können (z.b. Polygon(a, b, c, d) == Polygon(b,
		// c, d, a) == Polygon(c, d, a, b) != Polygon(a, d, c, b)
		if( this == other )
			return delta.equals( new Point( 0, 0 ) );
		if( other == null )
			return false;
		if( !( other instanceof Polygon ) )
			return false;

		Polygon poly = (Polygon) other;
		if( !getColor( ).equals( poly.getColor( ) ) )
			return false;
		if( isSolid( ) != poly.isSolid( ) )
			return false;
		ArrayList< Point > polyPoints = poly.getPoints( );
		if( polyPoints.size( ) != points.size( ) )
			return false;

		for( int i = 0; i < points.size( ); ++i )
		{
			// Punkt verschieben
			Point p = points.get( i ).copy( );
			p.move( delta.getX( ), delta.getY( ) );

			// Falls nicht gleich, ungleiche Polygone
			if( !p.equals( polyPoints.get( i ) ) )
				return false;
		}

		return true;
	}

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
	public void rotate( Point center, double phi )
	{
		for( Point point : points )
			point.rotate( center, phi );

		draw( );
	}
}
