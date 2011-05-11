package shapes;

import java.awt.Color;

/**
 * Klasse Kreis, definiert über Mittelpunkt und Radius
 * 
 * @author Marcus Bauer
 * @version 201103231934
 */
public class Circle extends Shape
{
	/** Radius des Kreises */
	private int radius = 0;

	/** Mittelpunkt des Kreises */
	private Point center;

	/**
	 * Konstruktor für Objekte der Klasse Circle
	 * 
	 * @param radius
	 *            Radius des Kreises
	 * @param center
	 *            Mittelpunkt des Kreises
	 * @throws IllegalArgumentException
	 *             falls Radius negativ oder Mittelpunkt = <code>null</code>
	 */
	public Circle( int radius, Point center ) throws IllegalArgumentException
	{
		if( radius < 0 )
			throw new IllegalArgumentException( "Radius des Kreises ist negativ (" + radius + ")" );
		if( center == null )
			throw new IllegalArgumentException( "Mittelpunkt des Kreises ist <null>" );
		this.radius = radius;
		this.center = center.copy( );
	}

	/**
	 * zeichnet den Kreis auf die Kreisfläche
	 */
	public void draw( )
	{
		getWhiteBoard( ).removeShape( representation );
		representation = getWhiteBoard( ).drawEllipse( center.getX( ), center.getY( ), radius, radius, getColor( ), isSolid( ), 0 );
	}

	/**
	 * zeichnet den Kreis ggf. ausgefüllt auf der Zeichenfläche
	 * 
	 * @param color
	 *            Farbe
	 * @param solid
	 *            wenn true, dann wird die Fläche farbig ausgefüllt
	 */
	public void draw( Color color, boolean solid )
	{
		setColor( color );
		setSolid( solid );
		draw( );
	}

	/**
	 * bewege den Kreis um dx, dy
	 * 
	 * @param dx
	 *            Distanz in x
	 * @param dy
	 *            Distanz in y
	 */
	@Override
	public Drawable move( double dx, double dy )
	{
		center.move( dx, dy );
		draw( );
		return this;
	}

	/**
	 * Gibt den Mittelpunkt des Kreises zurück
	 * 
	 * @return Mittelpunkt des Kreises
	 */
	public Point getCenter( )
	{
		return center.copy( );
	}

	/**
	 * Gibt den Mittelpunkt des Kreises zurück
	 * 
	 * @return Mittelpunkt des Kreises
	 */
	@Override
	public Point getFirstPoint( )
	{
		return center.copy( );
	}

	/**
	 * Gibt den Radius des Kreises zurück
	 * 
	 * @return Radius des Kreises
	 */
	public int getRadius( )
	{
		return radius;
	}

	/**
	 * Vergleicht ob die Kreise gleich sind wenn um Delta verschoben
	 * 
	 * @param other
	 *            das Vergleichsobjekt
	 * @param delta
	 *            Differenz zwischen beiden Figuren
	 * @return true falls gleich, false sonst
	 */
	@Override
	public boolean equalsRelative( Drawable other, Point delta )
	{
		if( this == other )
			return delta.equals( new Point( 0, 0 ) );
		if( other == null )
			return false;
		if( !( other instanceof Circle ) )
			return false;

		// Kreise vergleichen
		Circle circle = (Circle) other;
		if( !getColor( ).equals( circle.getColor( ) ) )
			return false;
		if( isSolid( ) != circle.isSolid( ) )
			return false;
		if( radius != circle.getRadius( ) )
			return false;
		return getCenter( ).move( delta.getX( ), delta.getY( ) ).equals( circle.getCenter( ) );
	}

	/**
	 * Dreht das Objekt um einen bestimmten Winkel
	 * 
	 * @param center
	 *            Punkt, der als Ursprung für die Drehung dient
	 * @param phi
	 *            Winkel, um den der Punkt gedreht werden soll
	 */
	public void rotate( Point center, double phi )
	{
		this.center.rotate( center, phi );
		draw( );
	}
}
