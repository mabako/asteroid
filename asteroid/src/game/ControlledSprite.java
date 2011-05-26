package game;

import gui.GameKeyListener;

import java.awt.Color;

/**
 * Klasse für das Raumschiff als Sprite, welches man kontrollieren kann
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105261621
 */
public class ControlledSprite extends Sprite
{
	/** Winkel, um der bei Drücken von links/rechts pro Sekunde gedreht wird */
	private static final int ROTATE_ANGLE = 90;

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
	 * Dreht das Objekt und ändert die Bewegungsrichtung
	 * 
	 * @param rotation
	 *            um welchen Winkel gedreht werden soll
	 */
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
