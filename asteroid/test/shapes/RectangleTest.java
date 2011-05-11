package shapes;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Methoden der Rectangle-Klasse
 * 
 * @author Marcus Bauer
 * @version 201103162319
 */
public class RectangleTest
{
	/**
	 * Gemeinsam genutztes Rechteck
	 */
	private Rectangle rect = null;

	/**
	 * Ausgangspunkt (links unten) und Längen
	 */
	private Point bottomLeft = null;
	private int lengthX = 13;
	private int lengthY = 17;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp( ) throws Exception
	{
		// Ausgangspunkt erstellen
		bottomLeft = new Point( 50, 37 );

		// Rechteck anlegen
		rect = new Rectangle( bottomLeft, lengthX, lengthY );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown( ) throws Exception
	{
	}

	/**
	 * Testet die Anzahl der Punkte in dem Rechteck
	 */
	@Test
	public void testRectangleNumberOfPoints( )
	{
		ArrayList< Point > points = rect.getPoints( );

		// Linie sollte nur zwei Punkte enthalten
		assertEquals( points.size( ), 4 );
	}

	/**
	 * Überprüft, ob Start- und Endpunkt identisch zu den eingegebenen sind und
	 * unabhänging von der Linie verschieben lassen
	 */
	@Test
	public void testStartAndEndPoint( )
	{
		ArrayList< Point > points = rect.getPoints( );

		// Sollte nicht denselben Startpunkt haben
		assertNotSame( points.get( 0 ), bottomLeft );

		// Punkte sollen korrekt sein
		assertEquals( points.get( 0 ), bottomLeft );
		assertEquals( points.get( 1 ), bottomLeft.copy( ).move( lengthX, 0 ) );
		assertEquals( points.get( 2 ), bottomLeft.copy( ).move( lengthX, lengthY ) );
		assertEquals( points.get( 3 ), bottomLeft.copy( ).move( 0, lengthY ) );

		// Sobald man den Punkt verschiebt, sollte dieser nicht mehr gleich sein
		// (d.h. Änderungen am Punkt ändern das Rechteck nicht)
		bottomLeft.move( 3, 0 );
		assertThat( points.get( 0 ), not( equalTo( bottomLeft ) ) );
	}
}
