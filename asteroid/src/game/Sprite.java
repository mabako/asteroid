package game;

import java.awt.Color;
import java.util.Random;

import shapes.Circle;
import shapes.Figure;
import shapes.Point;
import shapes.Polygon;

/**
 * Klasse für Sprites, die im Falle von Asteroiden zufällig erstellt werden
 * können
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105251312
 */
public class Sprite
{
	/** Kollisionsbox der Shape */
	private Circle boundingBox;

	/** Echte Box (was gezeichnet wird) */
	private Polygon physical;

	/** Gesamtes Objekt */
	private Figure gesamt;

	/**
	 * Sprite auf Koordinaten erstellen
	 * 
	 * @param x
	 *            X-Koordinate für Sprite-Mittelpunkt
	 * @param y
	 *            Y-Koordinate für Sprite-Mittelpunkt
	 * @param radius
	 */
	public Sprite( double x, double y, int radius )
	{
		// Boundingbox anlegen - Größe zufällig
		boundingBox = new Circle( radius, new Point( x, y ) );
		boundingBox.setColor( Color.RED );

		// Aktuelles Objekt anlegen
		physical = new Polygon( );

		// Beide zu einer Figur hinzufügen
		gesamt = new Figure( );
		gesamt.addShape( boundingBox );
		gesamt.addShape( physical );
	}

	/**
	 * Erstellt einen Asteroiden als diese Figur, basiert auf zufälligen Daten
	 * innerhalb der BoundingBox
	 */
	public void createAsteroid( )
	{
		int winkel = 0;
		Random zufallsgenerator = new Random( );
		do
		{
			// Punkt auf Kreis erstellen
			addPoint( (double)boundingBox.getRadius( ) / 2.0 * ( 1.0 + zufallsgenerator.nextDouble( ) ), winkel );

			// Nächsten Winkel generieren
			winkel = Math.min( 360, winkel + zufallsgenerator.nextInt( 30 ) + 15 );
		}
		while( winkel < 360 );
	}

	/**
	 * Erstellt einen Punkt innerhalb des Radius
	 * 
	 * @param radius
	 *            der Radius im Bereich von [0 .. originaler Radius], inwieweit
	 *            der Punkt vom Mittelpunkt entfernt sein darf
	 * @param winkel
	 */
	protected void addPoint( double radius, int winkel )
	{
		// Parameter überprüfen
		if( radius < 0 || radius > boundingBox.getRadius( ) )
			throw new IllegalArgumentException( "Radius ist " + radius + ", darf aber nur im Bereich von 0 bis " + boundingBox.getRadius( ) + " liegen." );
		if( winkel < 0 || winkel >= 360 )
			throw new IllegalArgumentException( "Winkel ist " + winkel + ", muss aber im Bereich von 0 <= winkel < 360 liegen" );

		// Winkel konvertieren
		double radians = Math.toRadians( winkel );

		// Neue Punkte errechnen
		double px = radius * Math.sin( radians ) + boundingBox.getCenter( ).getX( );
		double py = radius * Math.cos( radians ) + boundingBox.getCenter( ).getY( );

		// und zum Polygon hinzufügen
		physical.addPoint( new Point( px, py ) );
	}

	/**
	 * Liefert die BoundingBox zurück
	 * 
	 * @return Die BoundingBox
	 */
	public Circle getBoundingBox( )
	{
		return boundingBox;
	}

	/**
	 * Gibt das zu zeichnende Polygon zurück
	 * 
	 * @return das zu zeichnende Polygon
	 */
	public Polygon getPhysical( )
	{
		return physical;
	}
}
