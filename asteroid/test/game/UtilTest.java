package game;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import shapes.Point;

/**
 * Testet die Util-Klasse
 * 
 * @author Marcus Bauer
 * @version 201105252200
 */
public class UtilTest
{
	/** Gemeinsam genutzter Punkt */
	Point p;
	
	/**
	 * Vor jedem Test einen Punkt erstellen
	 */
	@Before
	public void setUp( )
	{
		p = new Point( 100, 200 );
	}

	/**
	 * Testet getPointInFrontOf mit Ursprung (0, 0)
	 */
	@Test
	public void testGetPointInFrontOf( )
	{
		// Berechnet die Entfernungen
		for( double distance = 0; distance < 10; ++ distance )
			for( double angle = 0; angle < 360; ++ angle )
				assertEquals( Util.getPointInFrontOf( distance, angle ).distanceTo( new Point( 0, 0 ) ), distance, Point.DELTA );
		
		// Tests für einzelne Werte
		assertThat( Util.getPointInFrontOf( 3, 0 ), equalTo( new Point( 0, 3 ) ) );
		assertThat( Util.getPointInFrontOf( -2, 0 ), equalTo( new Point( 0, -2 ) ) );
		assertThat( Util.getPointInFrontOf( 2, 180 ), equalTo( new Point( 0, -2 ) ) );
		
		// Auf X-Achse drehen
		assertThat( Util.getPointInFrontOf( 4, 90 ), equalTo( new Point( 4, 0 ) ) );
		
		// gar nicht drehen
		assertThat( Util.getPointInFrontOf( 0, 123 ), equalTo( new Point( 0, 0 ) ) );
	}

	/**
	 * Testet getPointInFrontOf mit einem anderen Ursprung als (0, 0)
	 */
	@Test
	public void testGetPointInFrontOfPoint( )
	{
		assertThat( Util.getPointInFrontOf( p, 2, 0 ), equalTo( p.copy( ).move( 0, 2 ) ) );
		assertThat( Util.getPointInFrontOf( p, 3, 90 ), equalTo( p.copy( ).move( 3, 0 ) ) );
		assertThat( Util.getPointInFrontOf( p, 4, 180 ), equalTo( p.copy( ).move( 0, -4 ) ) );
		assertThat( Util.getPointInFrontOf( p, 5, 270 ), equalTo( p.copy( ).move( -5, 0 ) ) );
	}
}
