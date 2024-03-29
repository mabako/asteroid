package game;

import gui.Fenster;
import gui.GameKeyListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import teaching.WhiteBoard;

/**
 * Das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 *         Mathias Kuschel (mathias.kuschel@gmx.de)
 * @version 201106151242
 */
public final class Game implements Runnable
{
	/** Gibt an, ob das Spiel zur Zeit läuft */
	private volatile boolean running = false;

	/** Alle Asteroiden */
	private List< Sprite > asteroids = Collections.synchronizedList( new ArrayList< Sprite >( ) );
	
	/** Alle Threads */
	private List< Thread > threads = Collections.synchronizedList( new ArrayList< Thread >( ) );

	/** Raumschiff */
	private ControlledSprite ship;

	/** Anzahl der zu erstellenden Asteroiden */
	private int asteroidCount = 40;

	/** KeyListener für GUI */
	private GameKeyListener keyListener;
	
	/** Anzahl der Leben */
	private int lives = 3;

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
		Fenster fenster = new Fenster( );
		fenster.addKeyListener( keyListener );

		while( lives > 0 )
		{
			setupSingleGame( );
			fenster.setLives( lives );
	
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
			threads.clear( );
			
			// Tot, also leben abziehen
			lives --;
			
			// 2 Sekunden warten
			try
			{
				Thread.sleep( 2000 );
			}
			catch( InterruptedException e )
			{
			}
			
			// Alles auf dem Whiteboard löschen
			ship.getPhysical( ).getWhiteBoard( ).clear( );
		}
		fenster.setLives( lives );
		ship.getPhysical( ).getWhiteBoard( ).close( );
	}

	/**
	 * Erstellt alle initialen Asteroiden und das Raumschiff, alles für ein
	 * Spiel
	 * 
	 * @return
	 */
	private void setupSingleGame( )
	{
		ship = null;
		asteroids = Collections.synchronizedList( new ArrayList< Sprite >( ) );

		// Spiel läuft
		running = true;

		// Raumschiff erstellen
		ship = new ControlledSprite( this, 300, 300, 15, keyListener );
		ship.createShip( );
		Thread shipThread = new Thread( ship );
		shipThread.start( );
		threads.add( shipThread );

		// KeyListener fürs WhiteBoard auf unseren KeyListener setzen
		WhiteBoard whiteBoard = ship.getPhysical( ).getWhiteBoard( );
		whiteBoard.setKeyListener( keyListener );

		// Asteroiden anlegen
		for( int i = 0; i < asteroidCount; ++i )
		{
			Sprite s = createAsteroid( whiteBoard );
			Thread t = new Thread( s );
			t.start( );
			threads.add( t );
		}
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
		int radius = zufallsgenerator.nextInt( 50 ) + 10;

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
		s.setMoveSpeed( 50 * zufallsgenerator.nextDouble( ) );
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
