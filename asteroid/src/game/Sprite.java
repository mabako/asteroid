package game;

import java.awt.Color;
import java.util.Random;

import shapes.Circle;
import shapes.Point;
import shapes.Polygon;

/**
 * Klasse für Sprites, die im Falle von Asteroiden zufällig erstellt werden
 * können
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105251135
 */
public class Sprite
{
	/** Kollisionsbox der Shape */
	protected Circle boundingBox;

	/** Echte Box (was gezeichnet wird) */
	protected Polygon physicalBox;

	/**
	 * Konstruktor, falls kein Sprite von dieser Klasse erzeugt werden soll
	 */
	protected Sprite( )
	{

	}

	/**
	 * Zufälliges Sprite auf Koordinaten erstellen
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
		physicalBox = new Polygon( );

		int winkel = 0;
		Random zufallsgenerator = new Random( );
		while( winkel < 360 )
		{
			// Punkt auf Kreis erstellen
			addPoint( radius / 2 * ( 1 + zufallsgenerator.nextDouble( ) ), winkel );

			// Nächsten Winkel generieren
			winkel = Math.min( 360, winkel + zufallsgenerator.nextInt( 30 ) + 15 );
		}
	}

	/**
	 * Erstellt einen Punkt innerhalb des Radius
	 * 
	 * @param radius
	 *            der Radius im Bereich von [0 .. originaler Radius], inwieweit
	 *            der Punkt vom Mittelpunkt entfernt sein darf
	 * @param winkel
	 */
	private void addPoint( double radius, int winkel )
	{
		// Parameter überprüfen
		if( radius < 0 || radius > boundingBox.getRadius( ) )
			throw new IllegalArgumentException( "Radius ist " + radius + ", darf aber nur im Bereich von 0 bis " + boundingBox.getRadius( ) + " liegen." );

		// Winkel konvertieren
		double radians = Math.toRadians( winkel );

		// Neue Punkte errechnen
		double px = radius * Math.sin( radians ) + boundingBox.getCenter( ).getX( );
		double py = radius * Math.cos( radians ) + boundingBox.getCenter( ).getY( );

		// und zum Polygon hinzufügen
		physicalBox.addPoint( new Point( px, py ) );
	}
}
