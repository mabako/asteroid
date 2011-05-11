package shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Ein Schneemann
 * 
 * @author Marcus Bauer
 * @version 201103232104
 */
public class Figure extends Drawable
{
	private List< Drawable > drawables = new ArrayList< Drawable >( );

	/**
	 * Zeichnet einen Schneemann auf das Whiteboard
	 */
	static Figure snowMan( )
	{
		Figure snowMan = new Figure( );

		// Kopf des Schneemanns, inklusive Augen, Hut usw.
		Circle kopf = new Circle( 60, new Point( 200, 460 ) );
		kopf.setColor( Color.black );
		snowMan.addShape( kopf );

		for( int x = 180; x <= 220; x += 40 )
		{
			Circle auge = new Circle( 10, new Point( x, 480 ) );
			auge.setColor( Color.black );
			auge.setSolid( true );
			snowMan.addShape( auge );
		}

		Circle nase = new Circle( 14, new Point( 200, 460 ) );
		nase.setColor( Color.orange );
		nase.setSolid( true );
		snowMan.addShape( nase );

		Rectangle mund = new Rectangle( new Point( 180, 420 ), 40, 10 );
		mund.setColor( Color.black );
		mund.setSolid( true );
		snowMan.addShape( mund );

		Rectangle hut = new Rectangle( new Point( 180, 520 ), 40, 40 );
		hut.setColor( Color.black );
		hut.setSolid( true );
		snowMan.addShape( hut );

		Line hutschnur = new Line( new Point( 140, 520 ), new Point( 260, 520 ) );
		hutschnur.setColor( hut.getColor( ) );
		snowMan.addShape( hutschnur );

		// Bauch inkl. paar Knöpfe und Arme
		Circle bauch = new Circle( 80, new Point( 200, 320 ) );
		bauch.setColor( Color.black );
		snowMan.addShape( bauch );

		Line linkerArm = new Line( new Point( 120, 320 ), new Point( 40, 400 ) );
		linkerArm.setColor( Color.black );
		snowMan.addShape( linkerArm );

		Line rechterArm = new Line( new Point( 280, 320 ), new Point( 360, 400 ) );
		rechterArm.setColor( Color.black );
		snowMan.addShape( rechterArm );

		for( int y = 360; y >= 280; y -= 40 )
		{
			Circle knopf = new Circle( 7, new Point( 200, y ) );
			knopf.setColor( Color.black );
			knopf.setSolid( true );
			snowMan.addShape( knopf );
		}

		// Sowas wie Beine
		Circle rumpf = new Circle( 120, new Point( 200, 120 ) );
		rumpf.setColor( Color.black );
		snowMan.addShape( rumpf );

		snowMan.draw( );
		return snowMan;
	}

	/**
	 * Fügt ein weiteres Shape zur Figur hinzu
	 * 
	 * @return the Figure
	 */
	public Figure addShape( Drawable aDrawable )
	{
		// TODO: Erstellt keine Kopie, d.h. Objekte können nachträglich geändert
		// werden
		drawables.add( aDrawable );
		return this;
	}

	/**
	 * Zeichnet die Elemente auf das WhiteBoard
	 */
	public void draw( )
	{
		for( Drawable d : drawables )
			d.draw( );
	}

	/**
	 * Bewegt die Figur um dx, dy
	 * 
	 * @param dx
	 *            Distanz in x
	 * @param dy
	 *            Distanz in y
	 * @return das Polygon
	 */
	@Override
	public Drawable move( double dx, double dy )
	{
		for( Drawable d : drawables )
			d.move( dx, dy );
		return this;
	}

	/**
	 * Gibt eine Kopie der Liste aller drawables zurück
	 */
	public List< Drawable > getShapes( )
	{
		List< Drawable > temp = new ArrayList< Drawable >( );
		for( Drawable drawable : drawables )
			temp.add( drawable );
		return temp;
	}

	/**
	 * Vergleicht ob die Figuren gleich sind, wenn um delta verschoben
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
		if( !( other instanceof Figure ) )
			return false;

		Figure figure = (Figure) other;
		List< Drawable > otherDrawables = figure.getShapes( );
		if( otherDrawables.size( ) != drawables.size( ) )
			return false;

		for( Drawable drawable : drawables )
		{
			boolean found = false;

			// Falls ein gleiches Element existiert, entfernen
			for( Drawable otherDrawable : otherDrawables )
			{
				if( drawable.equalsRelative( otherDrawable, delta ) )
				{
					otherDrawables.remove( otherDrawable );
					found = true;
					break;
				}
			}

			// Falls kein passendes Element gefunden, false zurückgeben
			if( !found )
				return false;
		}

		return true;
	}

	/**
	 * Vergleicht Figur mit einer anderen, unabhängig von der Position der
	 * Figuren
	 */
	public boolean equalsIgnorePosition( Figure figure )
	{
		if( this == figure )
			return true;
		if( figure == null )
			return false;

		List< Drawable > other = figure.getShapes( );
		if( drawables.size( ) != other.size( ) )
			return false;

		Point relative = new Point( figure.getFirstPoint( ).getX( ) - getFirstPoint( ).getX( ), figure.getFirstPoint( ).getY( ) - getFirstPoint( ).getY( ) );
		return equalsRelative( figure, relative );
	}

	/**
	 * Gibt den ersten Punkt der Figur zurück
	 * 
	 * @return erster Punkt, oder (0, 0) falls es keinen solchen Punkt gibt
	 */
	@Override
	public Point getFirstPoint( )
	{
		if( drawables.size( ) == 0 )
			return new Point( 0, 0 );

		return drawables.get( 0 ).getFirstPoint( );
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
		for( Drawable drawable : drawables )
			drawable.rotate( center, phi );

		draw( );
	}
}
