package game;

import gui.GameKeyListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import teaching.WhiteBoard;

/**
 * Das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 *         Mathias Kuschel (mathias.kuschel@gmx.de)
 * @version 201105261600
 */
public final class Game implements Runnable
{
	/** Gibt an, ob das Spiel zur Zeit läuft */
	private volatile boolean running = false;

	/** Alle Asteroiden */
	private List< Sprite > asteroids = Collections.synchronizedList( new ArrayList< Sprite >( ) );

	/** Raumschiff */
	private ControlledSprite ship;

	/** Anzahl der zu erstellenden Asteroiden */
	private int asteroidCount = 20;

	/** KeyListener für GUI */
	private GameKeyListener keyListener;

	/**
	 * Konstruktor für das Spiel
	 * 
	 * @param keyListener
	 *            der KeyListener für GUI
	 */
	public Game( GameKeyListener keyListener )
	{
		this.keyListener = keyListener;
	}

	/**
	 * Spiel-Thread-Methode
	 */
	@Override
	public void run( )
	{
		List< Thread > threads = setupSingleGame( );

		// Alle Threads starten
		for( Thread t : threads )
			t.start( );

		// Warten, bis alle Threads beendet sind
		for( Thread t : threads )
		{
			try
			{
				t.join( );
			}
			catch( InterruptedException e )
			{
			}
		}
	}

	/**
	 * Erstellt alle initialen Asteroiden und das Raumschiff, alles für ein
	 * Spiel
	 * 
	 * @return
	 */
	private List< Thread > setupSingleGame( )
	{
		ship = null;
		asteroids = Collections.synchronizedList( new ArrayList< Sprite >( ) );

		// Spiel läuft
		running = true;

		// Liste mit Threads
		List< Thread > threads = new LinkedList< Thread >( );

		// Raumschiff erstellen
		ship = new ControlledSprite( this, 300, 300, 15, keyListener );
		ship.createShip( );
		ship.setAngle( -130 );
		ship.setMoveSpeed( 50 );
		threads.add( new Thread( ship ) );

		// Whiteboard temporär speichern
		WhiteBoard whiteBoard = ship.getPhysical( ).getWhiteBoard( );
		whiteBoard.setKeyListener( keyListener );

		// Asteroiden anlegen
		for( int i = 0; i < asteroidCount; ++i )
		{
			Sprite s = createAsteroid( whiteBoard );
			threads.add( new Thread( s ) );
		}

		return threads;
	}

	/**
	 * Erstellt einen Asteroiden auf dem WhiteBoard
	 * 
	 * @param whiteBoard
	 *            Whiteboard, auf dem Asteroid erstellt werden soll
	 * @return Sprite des Asteroiden
	 */
	private Sprite createAsteroid( WhiteBoard whiteBoard )
	{
		Random zufallsgenerator = new Random( );

		// Fester Radius
		int radius = zufallsgenerator.nextInt( 30 ) + 10;

		// Anfang aussuchen
		double x = 0, y = 0, angle = 0;
		switch( zufallsgenerator.nextInt( 4 ) )
		{
			case 0:
				// Befindet sich links des whiteboards
				x = whiteBoard.getMinX( ) - radius;
				y = zufallsgenerator.nextDouble( ) * ( whiteBoard.getMaxY( ) - whiteBoard.getMinY( ) ) + whiteBoard.getMinY( );
				angle = zufallsgenerator.nextDouble( ) * 90 + 45;
				break;
			case 1:
				// Rechts des Whiteboards
				x = whiteBoard.getMaxX( ) + radius;
				y = zufallsgenerator.nextDouble( ) * ( whiteBoard.getMaxY( ) - whiteBoard.getMinY( ) ) + whiteBoard.getMinY( );
				angle = zufallsgenerator.nextDouble( ) * 90 + 225;
				break;
			case 2:
				// unterhalb des whiteboards
				x = zufallsgenerator.nextDouble( ) * ( whiteBoard.getMaxX( ) - whiteBoard.getMinX( ) ) + whiteBoard.getMinX( );
				y = whiteBoard.getMinY( ) - radius;
				angle = zufallsgenerator.nextDouble( ) * 90 + 315;
				break;
			case 3:
				// oberhalb des whiteboards
				x = zufallsgenerator.nextDouble( ) * ( whiteBoard.getMaxX( ) - whiteBoard.getMinX( ) ) + whiteBoard.getMinX( );
				y = whiteBoard.getMaxY( ) + radius;
				angle = zufallsgenerator.nextDouble( ) * 90 + 135;
				break;
		}

		// Zufällig im Winkel von -30° < w < 30° drehen
		double rotateAngle = ( zufallsgenerator.nextDouble( ) - 0.5 ) * 60;

		// Asteroid auf Koordinaten anlegen
		Sprite s = new Sprite( this, x, y, radius );
		s.createAsteroid( );
		s.setAngle( angle );
		s.setRotateAngle( rotateAngle );
		s.setMoveSpeed( 30 * zufallsgenerator.nextDouble( ) );
		return s;
	}

	/**
	 * Hält die Ausführung des Spieles an (keine Pause, sondern <i>löscht</i>
	 * alles auf dem Feld)
	 */
	public void stop( )
	{
		// TODO: Leben implementieren - d.h. running wird auf false, gesetzt,
		// ein Leben abgezogen und in run( ) solange der Thread ausgeführt, bis
		// alle Leben weg sind
		running = false;
	}

	/**
	 * Gibt zurück, ob das Spiel läuft
	 * 
	 * @return true falls das Spiel läuft, false sonst
	 */
	public boolean isRunning( )
	{
		return running;
	}

	/**
	 * Gibt eine Liste mit allen Asteroiden zurück
	 * 
	 * @return Liste aller Asteroiden
	 */
	public final List< Sprite > getAsteroids( )
	{
		return asteroids;
	}

	/**
	 * Gibt das Raumschiff zurück
	 * 
	 * @return das Raumschiff
	 */
	public Sprite getShip( )
	{
		return ship;
	}
}
