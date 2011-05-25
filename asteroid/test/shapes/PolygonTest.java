package shapes;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Klasse Polygon
 * 
 * @author Marcus Bauer
 * @version 201105251200
 */
public class PolygonTest
{
	/** Gemeinsam genutztes Polygon */
	private Polygon poly = null;

	/**
	 * Polygon initalisieren
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp( ) throws Exception
	{
		poly = new Polygon( );
	}

	@After
	public void tearDown( ) throws Exception
	{
		poly = null;
	}

	@Test
	public void testEmptyPolygon( )
	{
		// alle Punkte ermitteln
		ArrayList< Point > points = poly.getPoints( );

		// Darf nicht null sein
		assertNotNull( points );

		// Größe muss 0 sein
		assertEquals( points.size( ), 0 );

		// draw sollte auch funktionieren
		poly.draw( );
	}

	@Test
	public void testGetPoints( ) throws PolygonShapeException
	{
		// Mehrere Punkte erstellen
		ArrayList< Point > points = new ArrayList< Point >( );
		for( int i = 0; i < 30; ++i )
			points.add( new Point( 2 * i, 2 * i ) );
		poly.setPoints( points );

		// Liste erhalten
		points = poly.getPoints( );

		// Darf nicht dasselbe Objekt sein
		assertNotSame( points, poly.getPoints( ) );

		// Muss gleich groß sein
		assertEquals( points.size( ), poly.getPoints( ).size( ) );

		// Und muss dieselben Elemente enthalten
		assertEquals( points, poly.getPoints( ) );
	}

	@Test
	public void testSetPoints( ) throws PolygonShapeException
	{
		// Mehrere Punkte erstellen
		ArrayList< Point > points = new ArrayList< Point >( );
		for( int i = 0; i < 25; ++i )
			points.add( new Point( 2 * i, i ) );
		poly.setPoints( points );

		// Darf nicht dasselbe Objekt sein
		assertNotSame( points, poly.getPoints( ) );

		// Muss gleich groß sein
		assertEquals( points.size( ), poly.getPoints( ).size( ) );

		// Und muss dieselben Elemente enthalten
		assertEquals( points, poly.getPoints( ) );
	}

	@Test
	public void testSetPointsWithExceptions( )
	{
		ArrayList< Point > points = new ArrayList< Point >( );

		// Kein Punkt
		try
		{
			poly.setPoints( points );
			fail( "setPoints mit 0 Punkten sollte eine PolygonShapeException auslösen" );
		}
		catch( PolygonShapeException e )
		{
		}

		// Ein Punkt
		points.add( new Point( 12, 34 ) );
		try
		{
			poly.setPoints( points );
			fail( "setPoints mit 1 Punkt sollte eine PolygonShapeException auslösen" );
		}
		catch( PolygonShapeException e )
		{
		}

		// Zwei Punkte
		points.add( new Point( 12, 34 ) );
		try
		{
			poly.setPoints( points );
		}
		catch( PolygonShapeException e )
		{
			fail( "setPoints mit 2 Punkten sollte keine PolygonShapeException auslösen" );
		}
	}

	/**
	 * Tested addPoint
	 */
	@Test
	public void testAddPoint( )
	{
		// Kein Punkt bis jetzt
		poly.draw( );
		assertNull( poly.representation );

		// Nur ein Punkt
		poly.addPoint( new Point( 50, 100 ) );
		poly.draw( );
		assertNull( poly.representation );

		// Zwei Punkte - sollte gezeichnet werden
		poly.addPoint( new Point( 30, 50 ) );
		poly.draw( );
		assertNotNull( poly.representation );
	}

	@Test
	public void testMoveIntInt( ) throws PolygonShapeException
	{
		// Mehrere Punkte erstellen
		ArrayList< Point > points = new ArrayList< Point >( );
		for( int i = 0; i < 20; ++i )
			points.add( new Point( i, 2 * i ) );
		poly.setPoints( points );

		// Polygon verschieben
		poly.move( 5, 7 );
		assertThat( points, not( equalTo( poly.getPoints( ) ) ) );

		// Punkte auch verschieben, sollte wieder gleich sein
		for( Point p : points )
			p.move( 5, 7 );
		assertThat( points, equalTo( poly.getPoints( ) ) );
	}

	/**
	 * getFirstPoint testen
	 * 
	 * @throws PolygonShapeException
	 */
	@Test
	public void testGetFirstPoint( ) throws PolygonShapeException
	{
		// Leeres Polygon
		assertNotNull( poly.getFirstPoint( ) );
		assertEquals( poly.getFirstPoint( ), new Point( 0, 0 ) );

		// Mehrere Punkte erstellen
		ArrayList< Point > points = new ArrayList< Point >( );
		for( int i = 0; i < 20; ++i )
			points.add( new Point( 5, 2 * i ) );
		poly.setPoints( points );

		// Sollte jetzt den ersten Punkt zurückliefern
		assertEquals( poly.getFirstPoint( ), points.get( 0 ) );
	}

	/**
	 * equalsRelative testen
	 * 
	 * @throws PolygonShapeException
	 */
	@Test
	public void testEqualsRelative( ) throws PolygonShapeException
	{
		Point move = new Point( 0, 0 );

		assertFalse( poly.equalsRelative( null, move ) );
		assertFalse( poly.equalsRelative( new Circle( 50, move ), move ) );

		// Leeres Polygon
		assertTrue( poly.equalsRelative( poly, move ) );
		assertFalse( poly.equalsRelative( poly, move.copy( ).move( 2, 1 ) ) );

		// Punkte hinzufügen
		ArrayList< Point > points = new ArrayList< Point >( );
		for( int i = 0; i < 20; ++i )
			points.add( new Point( i, i ) );
		poly.setPoints( points );

		// Andres Polygon - leer, also sollte nicht gleich sein
		Polygon poly2 = new Polygon( );
		assertFalse( poly.equalsRelative( poly2, move ) );

		// Test mit gleichen Punkten
		poly2.setPoints( points );
		assertTrue( poly.equalsRelative( poly2, move ) );

		// Alle Punkte verschieben
		move = new Point( 3, 5 );
		for( Point p : points )
			p.move( move.getX( ), move.getY( ) );
		poly2.setPoints( points );

		// Kein Verschiebungsvektor: ungleich
		assertFalse( poly.equalsRelative( poly2, new Point( 0, 0 ) ) );

		// mit Verschiebungsvektor
		assertTrue( poly.equalsRelative( poly2, move ) );

		// Andere Farben
		poly.setColor( Color.black );
		poly2.setColor( Color.green );
		assertFalse( poly.equalsRelative( poly2, move ) );
		poly2.setColor( poly.getColor( ) );
		assertTrue( poly.equalsRelative( poly2, move ) );

		// Verschiedene 'solid' parameter
		for( int i = 0; i < 4; ++i )
		{
			// alle möglichen Werte durchprobieren
			boolean a = i >= 2;
			boolean b = i % 2 == 0;
			poly.setSolid( a );
			poly2.setSolid( b );

			// Wenn solid gleich ist, sollte das Polygon auch gleich sein
			assertThat( a == b, is( equalTo( poly.equalsRelative( poly2, move ) ) ) );
		}
	}

	/**
	 * Testet rotate
	 */
	@Test
	public void testRotate( ) throws PolygonShapeException
	{
		ArrayList< Point > points = new ArrayList< Point >( );
		points.add( new Point( 3, 1 ) );
		points.add( new Point( 5, 5 ) );
		points.add( new Point( -2, 4 ) );
		poly.setPoints( points );

		// Um 90° Drehen
		poly.rotate( new Point( 0, 1 ), 90 );
		points = poly.getPoints( );

		assertEquals( new Point( 0, -2 ), points.get( 0 ) );
		assertEquals( new Point( 4, -4 ), points.get( 1 ) );
		assertEquals( new Point( 3, 3 ), points.get( 2 ) );
	}

	/**
	 * Testet getDrawn auf den Standard-Rückgabewert
	 */
	@Test
	public void testDefaultDrawn( )
	{
		assertEquals( poly.getDrawn( ), Shape.DEFAULT_DRAWN );
	}

	/**
	 * Testet setDrawn
	 * 
	 * @throws PolygonShapeException
	 */
	@Test
	public void testSetDrawn( ) throws PolygonShapeException
	{
		// Einize Punkte hinzufügen, da sonst nichts gezeichnet werden kann
		ArrayList< Point > points = new ArrayList< Point >( );
		points.add( new Point( 3, 1 ) );
		points.add( new Point( 5, 5 ) );
		points.add( new Point( -2, 4 ) );
		poly.setPoints( points );

		// Nicht zeichnen
		poly.setDrawn( false );
		poly.draw( );
		assertNull( poly.representation );
		assertEquals( poly.getDrawn( ), false );

		// Nochmal zeichnen
		poly.setDrawn( true );
		poly.draw( );
		assertNotNull( poly.representation );
		assertEquals( poly.getDrawn( ), true );
	}
}
