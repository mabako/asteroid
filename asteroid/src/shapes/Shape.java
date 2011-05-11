package shapes;

import java.awt.Color;
import teaching.WhiteBoard;

/**
 * Abstrakte Klasse, die die Zeichenfläche beinhaltet und verwaltet.
 * 
 * @author Marcus Bauer
 * @version 201103230956
 */
public abstract class Shape extends Drawable
{
	/** Whiteboard, das von uns verwaltet wird */
	private static WhiteBoard whiteBoard = new WhiteBoard( );

	/** Farbe der Shape */
	private Color color = new Color( 0, 0, 0 );

	/** Solide Fläche? */
	private boolean solid = false;

	/** Objekt-Repräsentation */
	protected Object representation;

	/**
	 * welche Farbe derzeit benutzt wird
	 * 
	 * @return the Color
	 */
	public final Color getColor( )
	{
		return color;
	}

	/**
	 * Methode, die die Zeichenfläche zurückgibt
	 * 
	 * @return the Whiteboard
	 */
	public final WhiteBoard getWhiteBoard( )
	{
		return whiteBoard;
	}

	/**
	 * Gibt an, ob Fläche ausgefüllt wird
	 * 
	 * @return the solid
	 */
	public final boolean isSolid( )
	{
		return solid;
	}

	/**
	 * Setze Farbe
	 * 
	 * @param color
	 *            the color to set
	 */
	// TODO: Check for setColor(a), a = new Color(...); a != getColor()
	public final void setColor( Color color )
	{
		this.color = color;

		// Erneut zeichnen
		draw( );
	}

	/**
	 * Default-Wert ist false. Wenn Fläche ausgefüllt werden soll, dann true
	 * setzen.
	 * 
	 * @param solid
	 *            the solid to set
	 */
	public final void setSolid( boolean solid )
	{
		this.solid = solid;

		// Erneut zeichnen
		draw( );
	}
}
