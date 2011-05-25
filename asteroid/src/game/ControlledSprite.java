package game;

import java.awt.Color;

/**
 * Klasse für das Raumschiff als Sprite, welches man kontrollieren kann
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105252316
 */
public class ControlledSprite extends Sprite
{
	public ControlledSprite( double x, double y, int radius )
	{
		super( x, y, radius );
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
}
