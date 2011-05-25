package game;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shapes.Circle;
import shapes.Point;
import shapes.Polygon;
import shapes.PolygonShapeException;

/**
 * Testet die Methoden der Sprite-Klasse
 * 
 * @author Marcus Bauer
 * @version 201105252236
 */
public class SpriteTest
{
	/** Start-Position für Sprite */
	private final static double x = 15.3;

	/** Start-Position für Sprite */
	private final static double y = 13.21;

	/** Start-Radius für Shape */
	private final static int radius = 20;

	/** Gemeinsam genutzter Sprite */
	Sprite sprite = null;

	/**
	 * Legt ein Sprite an
	 */
	@Before
	public void setUp( )
	{
		sprite = new Sprite( x, y, radius );
	}

	/**
	 * Setzt Sprite auf null
	 */
	@After
	public void tearDown( )
	{
		sprite = null;
	}

	/**
	 * Testet die Bounding-Box - sollte ursprünglichen Punkt und Radius
	 * zurückgeben
	 */
	@Test
	public void testGetBoundingBox( )
	{
		// BoundingBox sollte zunächst existieren
		Circle boundingBox = sprite.getBoundingBox( );
		assertNotNull( boundingBox );

		// BoundingBox sollte einen Mittelpunkt haben
		assertNotNull( boundingBox.getCenter( ) );
		// ... der unserem angegebenen entspricht
		assertEquals( boundingBox.getCenter( ), new Point( x, y ) );

		// Und der Radius sollte stimmen
		assertEquals( boundingBox.getRadius( ), radius );
	}

	/**
	 * Testet das Polygon - da noch keine Punkte enthalten sind, sollte das zwar
	 * existieren, aber noch nicht gezeichnet werden
	 */
	@Test
	public void testGetPhysical( )
	{
		Polygon poly = sprite.getPhysical( );

		// Polygon soll existieren
		assertNotNull( poly );

		// Und keine Punkte haben
		assertEquals( poly.getPoints( ).size( ), 0 );
	}

	/**
	 * Testet das erstellen von Asteroiden
	 */
	@Test
	public void testCreateAsteroid( )
	{
		// Asteroid erstellen
		sprite.createAsteroid( );

		// Mittelpunkt
		Circle boundingBox = sprite.getBoundingBox( );
		Point center = boundingBox.getCenter( );
		int radius = boundingBox.getRadius( );

		// Überprüfen, ob alle Punkte innerhalb des Radius liegen
		ArrayList< Point > points = sprite.getPhysical( ).getPoints( );

		// Sollte mindestens 8 Punkte haben, da 15° <= winkel <= 45°
		assertTrue( points.size( ) >= 8 );

		for( Point p : points )
		{
			// Abstand berechnen
			assertTrue( p.distanceTo( center ) <= radius );
		}
	}

	/**
	 * Testet addPoint
	 */
	@Test
	public void testAddPoint( )
	{
		assertEquals( sprite.getPhysical( ).getPoints( ).size( ), 0 );
		for( int i = 1; i < 10; ++i )
		{
			sprite.addPoint( (float) sprite.getBoundingBox( ).getRadius( ) / i, i * 20 );
			assertEquals( sprite.getPhysical( ).getPoints( ).size( ), i );
		}
	}

	/**
	 * Testet addPoint mit ungültigem Radius
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testAddPointNegativeRadius( )
	{
		sprite.addPoint( -1, 30 );
	}

	/**
	 * Testet addPoint mit ungültigem Radius
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testAddPointTooLargeRadius( )
	{
		sprite.addPoint( sprite.getBoundingBox( ).getRadius( ) + 0.5, 30 );
	}

	/**
	 * Tested addPoint mit ungültigem Winkel
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testAddPointTooSmallPhi( )
	{
		sprite.addPoint( sprite.getBoundingBox( ).getRadius( ), -1 );
	}

	/**
	 * Tested addPoint mit ungültigem Winkel
	 */
	@Test( expected = IllegalArgumentException.class )
	public void testAddPointTooLargePhi( )
	{
		sprite.addPoint( sprite.getBoundingBox( ).getRadius( ), 360 );
	}
	
	/**
	 * Testet get- und setMoveSpeed
	 */
	@Test
	public void testGetSetMoveSpeed( )
	{
		sprite.setMoveSpeed( 1.0 );
		assertEquals( sprite.getMoveSpeed( ), 1.0, Point.DELTA );
		
		sprite.setMoveSpeed( 0.01 );
		assertEquals( sprite.getMoveSpeed( ), 0.01, Point.DELTA );
		
		sprite.setMoveSpeed( 0.007 );
		assertEquals( sprite.getMoveSpeed( ), 0.007, Point.DELTA );
	}
	
	/**
	 * Testet get- und setAngle
	 * @throws PolygonShapeException 
	 */
	@Test
	public void testGetSetAngle( ) throws PolygonShapeException
	{
		sprite.createAsteroid( );
		
		// Winkel setzen und vergleichen
		sprite.setAngle( 200 );
		assertEquals( sprite.getAngle( ), 200, Point.DELTA );
		
		// Polygon kopieren
		Polygon poly = new Polygon( );
		poly.setPoints( sprite.getPhysical( ).getPoints( ) );

		// BoundingBox kopieren
		Circle boundingBox = new Circle( sprite.getBoundingBox( ).getRadius( ), sprite.getBoundingBox( ).getCenter( ) );
		
		// Winkel ändern
		sprite.setAngle( 318 );
		
		// Polygone sollten sich unterscheiden
		assertFalse( poly.equalsRelative( sprite.getPhysical( ), new Point( 0, 0 ) ) );
		
		// Wohingegen die BoundingBox gleich bleibt
		assertEquals( boundingBox.getCenter( ), sprite.getBoundingBox( ).getCenter( ) );
		assertEquals( boundingBox.getRadius( ), sprite.getBoundingBox( ).getRadius( ) );
	}
}
