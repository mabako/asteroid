package game;

import java.awt.Color;

/**
 * Klasse für das Raumschiff als Sprite, welches man kontrollieren kann
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105252357
 */
public class ControlledSprite extends Sprite
{
	public ControlledSprite( Game game, double x, double y, int radius )
	{
		super( game, x, y, radius );
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
}
