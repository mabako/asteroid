package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105260003
 */
public final class Game implements Runnable
{
	/** Gibt an, ob das Spiel zur Zeit läuft */
	private volatile boolean running = false;

	/** Alle Asteroiden */
	private List< Sprite > asteroids = Collections.synchronizedList( new ArrayList< Sprite >( ) );

	/** Raumschiff */
	private ControlledSprite ship;

	/**
	 * Konstruktor für das Spiel
	 */
	public Game( )
	{
	}

	/**
	 * Spiel-Thread-Methode
	 */
	@Override
	public void run( )
	{
		List< Thread > threads = setupSingleGame( );

		// Warten, bis alle Threads beendet sind
		while( threads.size( ) > 0 )
		{
			try
			{
				// Beendete Threads entfernen
				for( Thread t : threads )
					if( !t.isAlive( ) )
						threads.remove( t );

				Thread.sleep( 100 );
			}
			catch( InterruptedException e )
			{
				break;
			}
		}
	}

	/**
	 * Erstellt alle initialen Asteroiden und das Raumschiff, alles für ein Spiel
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

		// Asteroiden anlegen
		Sprite s = new Sprite( this, 100, 100, new Random( ).nextInt( 50 ) + 30 );
		s.createAsteroid( );
		threads.add( new Thread( s ) );
		// TODO: mehr Asteroiden, die nicht bei Beginn des Spieles schon auf dem
		// Spielfeld sind

		// Raumschiff erstellen
		ControlledSprite cs = new ControlledSprite( this, 300, 300, 15 );
		cs.createShip( );
		cs.setAngle( -130 );
		cs.setMoveSpeed( 2 );
		threads.add( new Thread( cs ) );

		// Alle Threads starten
		for( Thread t : threads )
			t.start( );
		return threads;
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
