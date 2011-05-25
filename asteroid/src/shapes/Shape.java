package shapes;

import java.awt.Color;
import teaching.WhiteBoard;

/**
 * Abstrakte Klasse, die die Zeichenfläche beinhaltet und verwaltet.
 * 
 * @author Marcus Bauer
 * @version 201105241407
 */
public abstract class Shape extends Drawable
{
	/** Standard-Wert für drawn */
	protected static final boolean DEFAULT_DRAWN = true;

	/** Standard-Fabe für Shapes */
	protected static final Color DEFAULT_COLOR = Color.WHITE;

	/** Whiteboard, das von uns verwaltet wird */
	private static WhiteBoard whiteBoard = new WhiteBoard( );

	/** Farbe der Shape */
	// TODO Test für getColor/setColor, ob diese Standard-Farbe auch gesetzt werden kann und Shapes unabhänging von der Farbe geändert werden können
	private Color color = DEFAULT_COLOR;

	/** Solide Fläche? */
	private boolean solid = false;

	/** Objekt-Repräsentation */
	protected Object representation;

	/** Speichert den Zustand, ob das Objekt momentan gezeichnet werden soll */
	private boolean drawn = DEFAULT_DRAWN;

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

	/**
	 * Setzt das Drawable auf <i>wird gezeichnet</i> (true) oder <i>wird nicht
	 * gezeichnet</i> (false).
	 * 
	 * @param drawn
	 *            true für <i>wird gezeichnet</i>, false sonst.
	 */
	public void setDrawn( boolean drawn )
	{
		this.drawn = drawn;

		draw( );
	}

	/**
	 * Liefert zurück, ob das Objekt gezeichnet wird (und dementsprechend auf
	 * WhiteBoard existiert)
	 * 
	 * @return <code>true</code> falls Objekt gezeichnet wird,
	 *         <code>false</code> sonst
	 */
	public boolean getDrawn( )
	{
		return drawn;
	}
}
