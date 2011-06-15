package game;

import gui.GameKeyListener;

import java.awt.Color;

/**
 * Klasse für das Raumschiff als Sprite, welches man kontrollieren kann
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105261642
 */
public class ControlledSprite extends Sprite
{
	/** Winkel, um der bei Drücken von links/rechts pro Sekunde gedreht wird */
	private static final int ROTATE_ANGLE = 90;

	/** Differenz, die maximal pro Sekunde hinzugefügt oder abgezogen wird */
	private static final double SPEED_DIFF_MOVE = 1.5;

	/** Differenz, die ohne Beschleunigung hinzugefügt oder abgezogen wird */
	private static final double SPEED_DIFF_IDLE = 0.25;

	/** Maximale Geschwindigkeit */
	private static final double MAX_SPEED = 200;

	/** KeyListener des Spiels */
	private GameKeyListener keyListener;

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
	 *            Radius der BoundingBox
	 * @param keyListener
	 *            der KeyListener des Spiels
	 */
	public ControlledSprite( Game game, double x, double y, int radius, GameKeyListener keyListener )
	{
		super( game, x, y, radius );
		this.keyListener = keyListener;
	}

	/**
	 * Erstellt das Raumschiff als Figur
	 */
	public void createShip( )
	{
		int radius = getBoundingBox( ).getRadius( );

		addPoint( radius, 0 );
		addPoint( radius, 150 );
		addPoint( radius, 210 );

		// Farbe einstellen
		getPhysical( ).setColor( Color.GREEN );
		getPhysical( ).setSolid( true );
	}

	/**
	 * Berechnet die Kollisionen
	 */
	@Override
	protected void updateCollisions( )
	{
		for( Sprite s : getGame( ).getAsteroids( ) )
		{
			if( collidesWith( s ) )
			{
				getGame( ).stop( );
			}
		}
	}

	/**
	 * Gibt die evtl. geänderte Bewegungsgeschwindigkeit zurück
	 */
	@Override
	public double getMoveSpeed( )
	{
		double speed = super.getMoveSpeed( );
		boolean keyUp = keyListener.isKeyPressed( GameKeyListener.KEY_UP );
		boolean keyDown = keyListener.isKeyPressed( GameKeyListener.KEY_DOWN );

		// keine oder beide Tasten gedrückt
		if( keyUp == keyDown )
		{
			// Falls rückwärts fliegt...
			if( speed < 0 )
				// Geschwindigkeit erhöhen (bis auf 0)
				speed = Math.min( speed + SPEED_DIFF_IDLE, 0 );
			else
				// sonst Geschwindigkeit verringern (bis auf 0)
				speed = Math.max( speed - SPEED_DIFF_IDLE, 0 );
		}
		else if( keyUp )
			speed = Math.min( MAX_SPEED, speed + SPEED_DIFF_MOVE );
		else
			speed = Math.max( -MAX_SPEED, speed - SPEED_DIFF_MOVE );

		setMoveSpeed( speed );
		return speed;
	}

	/**
	 * Dreht das Objekt und ändert die Bewegungsrichtung
	 * 
	 * @param rotation
	 *            um welchen Winkel gedreht werden soll
	 */
	@Override
	protected void updateRotation( double rotation )
	{
		setAngle( getAngle( ) + rotation );
	}

	/**
	 * Liefert den Winkel zurück, um den das Schiff pro Sekunde gedreht werden
	 * soll
	 * 
	 * @return Drehwinkel
	 */
	@Override
	public double getRotateAngle( )
	{
		boolean keyLeft = keyListener.isKeyPressed( GameKeyListener.KEY_LEFT );
		boolean keyRight = keyListener.isKeyPressed( GameKeyListener.KEY_RIGHT );

		// Falls keine oder beide Tasten gleichzeitig gedrückt werden, keine
		// Rotation
		if( keyLeft == keyRight )
			return 0;
		else if( keyLeft )
			return -ROTATE_ANGLE;
		else
			return ROTATE_ANGLE;
	}
}
