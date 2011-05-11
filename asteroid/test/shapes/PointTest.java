package shapes;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Methoden der Point-Klasse
 * 
 * @author Marcus Bauer
 * @version 201103232045
 */
public class PointTest
{
	/**
	 * Gemeinsam genutzter Punkt
	 */
	private Point point = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp( ) throws Exception
	{
		point = new Point( 30, 20 );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown( ) throws Exception
	{
	}

	/**
	 * Testet getX
	 */
	@Test
	public void testGetX( )
	{
		// Siehe setUp
		assertEquals( point.getX( ), 30, Point.DELTA );
	}

	/**
	 * testet getY
	 */
	@Test
	public void testGetY( )
	{
		// Siehe setUp
		assertEquals( point.getY( ), 20, Point.DELTA );
	}

	/**
	 * testet move
	 */
	@Test
	public void testMove( )
	{
		// Punkt auf x-achse verschieben
		double x = point.getX( );
		point.move( 10, 0 );
		assertEquals( point.getX( ) - 10, x, Point.DELTA );

		// Punkt auf y-achse verschieben
		double y = point.getY( );
		point.move( 0, 25 );
		assertEquals( point.getY( ) - 25, y, Point.DELTA );

		// Punkt auf beiden achsen verschieben
		x = point.getX( );
		y = point.getY( );
		point.move( 20, 130 );
		assertEquals( point.getX( ) - 20, x, Point.DELTA );
		assertEquals( point.getY( ) - 130, y, Point.DELTA );

		// Punkt nicht verschieben - sollte immernoch gleich sein
		x = point.getX( );
		y = point.getY( );
		point.move( 0, 0 );
		assertEquals( point.getX( ), x, Point.DELTA );
		assertEquals( point.getY( ), y, Point.DELTA );
	}

	/**
	 * testet clone
	 */
	@Test
	public void testCopy( )
	{
		Point kopie = point.copy( );

		// Punkte sind nicht dieselben Objekte
		assertNotSame( point, kopie );

		// Trotzdem: point.equals(copy)
		assertEquals( point, kopie );

		// Punkt verschieben, dann erneuter equal-test
		point.move( 5, 0 );
		assertThat( point, not( equalTo( kopie ) ) );

		// Um Y verschieben
		kopie = point.copy( );
		assertEquals( point, kopie );
		point.move( 0, -3 );
		assertThat( point, not( equalTo( kopie ) ) );
	}

	/**
	 * Testet equals
	 */
	@Test
	public void testEquals( )
	{
		// Punkt sollte immer mit sich gleich sein
		assertTrue( point.equals( point ) );

		// Sollte niemals mit nem Null-Objekt gleich sein
		assertFalse( point.equals( null ) );

		// Sollte niemals mit einem nicht-Point Objekt gleich sein
		assertFalse( point.equals( new Object( ) ) );

		// Punkt mit denselben Koordinaten
		assertEquals( point, new Point( point.getX( ), point.getY( ) ) );

		// Punkt mit unterschiedlicher x-Koordinate
		assertThat( point, not( equalTo( new Point( point.getX( ) + 1, point.getY( ) ) ) ) );

		// Punkt mit unterschiedlicher y-Koordinate
		assertThat( point, not( equalTo( new Point( point.getX( ), point.getY( ) + 1 ) ) ) );

		// Punkt mit beiden Koordinaten unterschiedlich
		assertThat( point, not( equalTo( new Point( point.getX( ) + 1, point.getY( ) + 1 ) ) ) );
	}

	/**
	 * Testet rotate
	 */
	@Test
	public void testRotate( )
	{
		Point p = new Point( 1000, 1000 );

		// Dreht den Punkt 45° um Koordinatenursprung
		p.rotate( new Point( 0, 0 ), 45 );
		assertEquals( new Point( 1414, 0 ), new Point( (int) p.getX( ), p.getY( ) ) );

		// Dreht den Punkt 15x 15° um Koordinatenursprung => 45° + 225° = 270°
		// FIXME: Praktikumsanleitung sagt (1000, 1000) als Punkt?
		for( int i = 0; i < 15; ++i )
			p.rotate( new Point( 0, 0 ), 15 );
		assertEquals( new Point( -1000, 1000 ), p );

		// Um anderen Punkt als (0, 0) drehen
		p = new Point( 5, 10 );

		// 180°
		p.rotate( new Point( 2, 2 ), 180 );
		assertEquals( new Point( -1, -6 ), p );

		// mehrere Drehungen sollten zum gleichen Ergebnis führen
		p.rotate( new Point( 2, 2 ), 73.15 );
		Point p2 = new Point( 5, 10 );
		p2.rotate( new Point( 2, 2 ), 180 + 73.15 );
		assertEquals( p2, p );
	}

	/**
	 * Testet rotate, wenn man um sich selbst dreht
	 */
	@Test
	public void testRotateSelf( )
	{
		for( double phi = 0; phi <= 360; phi += 0.1 )
		{
			Point p = point.copy( );
			p.rotate( point, phi );
			assertEquals( "Punkte sind bei phi = " + phi + " ungleich: (" + p.getX( ) + ", " + p.getY( ) + ") und (" + point.getX( ) + ", " + point.getY( ) + ") ", p, point );
		}
	}

	/**
	 * Testet rotate mit ungültigem Mittelpunkt
	 */
	@Test
	public void testRotateNoPoint( )
	{
		try
		{
			point.rotate( null, 30 );
			fail( "Punkt konnte mit 'null' als Mittelpunkt gedreht werden" );
		}
		catch( IllegalArgumentException e )
		{
		}
	}
}
