package shapes;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Methoden der Circle-Klasse
 * 
 * @author Marcus Bauer
 * @version 201105251209
 */
public class CircleTest
{
	/**
	 * Circle-Objekt
	 */
	private Circle circle = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp( ) throws Exception
	{
		circle = new Circle( 20, new Point( 50, 50 ) );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown( ) throws Exception
	{
		circle = null;
	}

	/**
	 * Testet den Konstruktor
	 */
	@Test
	public void testConstructor( ) throws IllegalArgumentException
	{
		// Radius und Mittelpunkt überprüfen
		Circle c = new Circle( 50, new Point( 30, 30 ) );
		assertEquals( 50, c.getRadius( ) );
		assertEquals( new Point( 30, 30 ), c.getCenter( ) );
	}

	/**
	 * Unsinniger Konstruktor ohne Mittelpunkt
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testConstructorWithoutCenter( )
	{
		new Circle( 20, null );
	}

	/**
	 * Unsinniger Konstruktor mit negativem Radius
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testConstructorWithNegativeRadius( )
	{
		new Circle( -5, new Point( 5, 5 ) );
	}

	/**
	 * Testet move
	 */
	@Test
	public void testMove( )
	{
		Point center = circle.getCenter( );

		// Nicht verschieben - nur move aufrufen
		circle.move( 0, 0 );
		assertEquals( circle.getCenter( ), center );

		// Verschieben
		circle.move( 3, 5 );
		assertThat( circle.getCenter( ), not( equalTo( center ) ) );

		// Wieder zurückverschieben
		circle.move( -3, -5 );
		assertEquals( circle.getCenter( ), center );
	}

	/**
	 * Zwei Kreise mit gleichem Mittelpunkt, einen verschieben, sollten anderen
	 * Mittelpunkt haben
	 */
	@Test
	public void testEinMittelpunkt( )
	{
		Point center = new Point( 50, 70 );
		Circle c1 = new Circle( 20, center );
		Circle c2 = new Circle( 40, center );

		// Mittelpunkte sollten nicht auf dasselbe Objekt zeigen/immer Kopien
		// sein
		assertNotSame( c1.getCenter( ), c1.getCenter( ) );
		assertNotSame( c1.getCenter( ), c2.getCenter( ) );

		// Mittelpunkt sollte gleich sein
		assertEquals( c1.getCenter( ), c1.getCenter( ) );
		assertEquals( c1.getCenter( ), c2.getCenter( ) );

		// Einen Kreis verschieben
		c1.move( 3, -5 );

		// Mittelpunkte sollten nicht mehr gleich sein
		assertThat( c1.getCenter( ), not( equalTo( c2.getCenter( ) ) ) );

		// Anderen Kreis ebenfalls verschieben
		c2.move( 3, -5 );

		// Sollte wieder gleiche Mittelpunkte haben
		assertEquals( c1.getCenter( ), c2.getCenter( ) );
	}

	/**
	 * getFirstPoint und getCenter sollten gleiche Punkte zurückliefern
	 */
	@Test
	public void testGetFirstPoint( )
	{
		// Mittelpunkte via getCenter( ) und getFirstPoint( ) sollten nicht auf
		// dasselbe Objekt zeigen
		assertNotSame( circle.getCenter( ), circle.getFirstPoint( ) );
		assertNotSame( circle.getFirstPoint( ), circle.getFirstPoint( ) );

		// Kreis verschieben
		circle.move( 3, -5 );

		// Sollte noch gleich sein
		assertEquals( circle.getCenter( ), circle.getFirstPoint( ) );
	}

	/**
	 * draw mit farben und solid testen
	 */
	@Test
	public void testDraw( )
	{
		circle.draw( Color.red, true );

		// Farbe prüfen
		assertEquals( circle.getColor( ), Color.red );
		assertThat( circle.getColor( ), not( equalTo( Color.blue ) ) );

		// solid prüfen
		assertTrue( circle.isSolid( ) );

		// Andere farbe und nicht solid zeichnen
		circle.draw( Color.green, false );

		// Farbe prüfen
		assertEquals( circle.getColor( ), Color.green );
		assertThat( circle.getColor( ), not( equalTo( Color.red ) ) );

		// solid prüfen
		assertFalse( circle.isSolid( ) );
	}

	/**
	 * equalsRelative testen
	 */
	@Test
	public void testEqualsRelative( )
	{
		// Radius überprüfen
		Point move = new Point( 0, 0 );
		Circle c1 = new Circle( circle.getRadius( ) + 1, circle.getCenter( ) );
		assertFalse( c1.equalsRelative( circle, move ) );

		// Nicht-Kreis-Objekte sollten nicht überprüft werden können
		assertFalse( c1.equalsRelative( null, move ) );
		assertFalse( c1.equalsRelative( new Line( new Point( 1, 1 ), new Point( 3, 3 ) ), move ) );

		// Sollte um (0, 0) verschoben gleich sein
		Circle c2 = new Circle( circle.getRadius( ), circle.getCenter( ) );
		assertTrue( c2.equalsRelative( c2, move ) );
		assertTrue( c2.equalsRelative( circle, move ) );

		// Um einen anderen als 0, 0 verschoben nicht
		move = new Point( 3, 5 );
		assertFalse( c2.equalsRelative( c2, move ) );

		// Wenn beide verschoben werden, sollte es allerdings funktionieren
		c2.move( move.getX( ), move.getY( ) );
		assertTrue( circle.equalsRelative( c2, move ) );

		// Kreis nochmal verschieben - sollte jetzt ungleich sein
		c2.move( 2, -2 );
		assertFalse( circle.equalsRelative( c2, move ) );
		c2.move( -2, 2 );
		assertTrue( circle.equalsRelative( c2, move ) );

		// Andere Farben
		circle.setColor( Color.black );
		c2.setColor( Color.green );
		assertFalse( circle.equalsRelative( c2, move ) );
		c2.setColor( circle.getColor( ) );
		assertTrue( circle.equalsRelative( c2, move ) );

		// Verschiedene 'solid' parameter
		for( int i = 0; i < 4; ++i )
		{
			// alle möglichen Werte durchprobieren
			boolean a = i >= 2;
			boolean b = i % 2 == 0;
			circle.setSolid( a );
			c2.setSolid( b );

			// Wenn solid gleich ist, sollte das Polygon auch gleich sein
			assertThat( a == b, is( equalTo( circle.equalsRelative( c2, move ) ) ) );
		}
	}

	/**
	 * Testet rotate( )
	 */
	@Test
	public void testRotate( )
	{
		// Um anderen Punkt als (0, 0) drehen
		circle.rotate( new Point( 10, 20 ), 180 );
		assertEquals( new Point( -30, -10 ), circle.getCenter( ) );
		circle.rotate( new Point( 10, 20 ), 180 );
		assertEquals( new Point( 50, 50 ), circle.getCenter( ) );
	}

	/**
	 * Testet getDrawn auf den Standard-Rückgabewert
	 */
	@Test
	public void testDefaultDrawn( )
	{
		assertEquals( circle.getDrawn( ), Shape.DEFAULT_DRAWN );
	}

	/**
	 * Testet setDrawn
	 */
	@Test
	public void testSetDrawn( )
	{
		// Nicht zeichnen
		circle.setDrawn( false );
		circle.draw( );
		assertNull( circle.representation );
		assertEquals( circle.getDrawn( ), false );

		// Nochmal zeichnen
		circle.setDrawn( true );
		circle.draw( );
		assertNotNull( circle.representation );
		assertEquals( circle.getDrawn( ), true );
	}
}
