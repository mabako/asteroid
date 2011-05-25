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
 * @version 201105260003
 */
public class Sprite implements Runnable
{
	/** Spielinstanz */
	private Game game;

	/** Kollisionsbox der Shape */
	private Circle boundingBox;

	/** Echte Box (was gezeichnet wird) */
	private Polygon physical;

	/** Gesamtes Objekt */
	private Figure gesamt;

	/** Winkel, in dem sich das Sprite bewegt */
	private double angle = 0;

	/** Distanz, um die das Sprite pro Sekunde bewegt wird */
	private double speed = 0;

	/** Wieoft der Thread pro Sekunde aufgerufen wird */
	private static final int ITERATIONS_PER_SECOND = 50;

	/**
	 * Sprite auf Koordinaten erstellen
	 * 
	 * @param game
	 *            die Spielinstanz
	 * @param x
	 *            X-Koordinate für Sprite-Mittelpunkt
	 * @param y
	 *            Y-Koordinate für Sprite-Mittelpunkt
	 * @param radius
	 */
	public Sprite( Game game, double x, double y, int radius )
	{
		this.game = game;

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
			addPoint( (double) boundingBox.getRadius( ) / 2.0 * ( 1.0 + zufallsgenerator.nextDouble( ) ), winkel );

			// Nächsten Winkel generieren
			winkel = Math.min( 360, winkel + zufallsgenerator.nextInt( 30 ) + 15 );
		}
		while( winkel < 360 );

		game.getAsteroids( ).add( this );
	}

	/**
	 * Erstellt einen Punkt innerhalb des Radius
	 * 
	 * @param radius
	 *            der Radius im Bereich von [0 .. originaler Radius], inwieweit
	 *            der Punkt vom Mittelpunkt entfernt sein darf
	 * @param angle
	 */
	protected void addPoint( double radius, int angle )
	{
		// Parameter überprüfen
		if( radius < 0 || radius > boundingBox.getRadius( ) )
			throw new IllegalArgumentException( "Radius ist " + radius + ", darf aber nur im Bereich von 0 bis " + boundingBox.getRadius( ) + " liegen." );
		if( angle < 0 || angle >= 360 )
			throw new IllegalArgumentException( "Winkel ist " + angle + ", muss aber im Bereich von 0 <= winkel < 360 liegen" );

		// Neue Punkte errechnen
		physical.addPoint( Util.getPointInFrontOf( boundingBox.getCenter( ), radius, angle ) );
	}

	/**
	 * Liefert die Spielinstanz zurück
	 * 
	 * @return Die Spielinstanz
	 */
	public Game getGame( )
	{
		return game;
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

	/**
	 * Bewegt das Sprite als Thread
	 */
	public void run( )
	{
		// Schleifendurchläufe pro Sekunde errechnen
		int sleep = 1000 / ITERATIONS_PER_SECOND;
		do
		{
			// Sofern sich unser Sprite nicht bewegt, ist Kollisionsberechnung witzlos
			if( speed > Point.DELTA )
			{
				// Neue Koordinaten errechnen
				Point offset = Util.getPointInFrontOf( speed, angle );
	
				// Figur bewegen
				gesamt.move( offset.getX( ), offset.getY( ) );
	
				// Kollisionen berechnen
				updateCollisions( );
			}

			try
			{
				// Und einige Millisekunden schlafen
				Thread.sleep( sleep );
			}
			catch( InterruptedException e )
			{
			}
		}
		while( game.isRunning( ) );
		
		// Elemente vom Whiteboard entfernen
		physical.setDrawn( false );
		boundingBox.setDrawn( false );
	}

	/**
	 * Gibt den Winkel zurück, in dem das Sprite bewegt wird.
	 * 
	 * @return Winkel
	 */
	public double getAngle( )
	{
		return angle;
	}

	/**
	 * Setzt den Winkel, in dem sich das Sprite bewegt.
	 * 
	 * @param angle
	 *            der zu setzende Winkel
	 */
	public void setAngle( double angle )
	{
		// Sprite rotieren
		double diff = angle - this.angle;
		gesamt.rotate( boundingBox.getCenter( ), diff );

		// Winkel speichern
		this.angle = angle;
	}

	/**
	 * Gibt die Bewegungsgeschwindigkeit (zurückgelegte Entfernung pro Sekunde)
	 * zurück
	 * 
	 * @return Bewegungsgeschwindigkeit
	 */
	public double getMoveSpeed( )
	{
		return speed;
	}

	/**
	 * Setzt die Bewegungsgeschwindigkeit (zurückgelegte Entfernung pro Sekunde)
	 * des Sprite
	 * 
	 * @param speed
	 *            neue Geschwindigkeit
	 */
	public void setMoveSpeed( double speed )
	{
		this.speed = speed;
	}

	/**
	 * Berechnet die Kollisionen
	 */
	protected void updateCollisions( )
	{
		// In diesem Fall nur relevant für Kollisionen mit dem Schiff
		if( collidesWith( game.getShip( ) ) )
		{
			game.stop( );
		}
	}

	/**
	 * Überprüft, ob sich dieses Sprite mit einem anderen schneidet
	 * 
	 * @param other
	 *            anderes Sprite
	 * @return true, falls sich die Sprites schneiden, false sonst
	 */
	protected boolean collidesWith( Sprite other )
	{
		// TODO Auto-generated method stub
		return false;
	}
}
