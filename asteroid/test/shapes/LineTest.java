package shapes;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Methoden der Line-Klasse
 * 
 * @author Marcus Bauer
 * @version 201103231203
 */
public class LineTest
{
	/**
	 * Gemeinsam genutzte Linie
	 */
	private Line line = null;
	
	/**
	 * Anfangs- und Endpunkt
	 */
	private Point start = null;
	private Point end = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		// Anfangs- und Endpunkt erstellen
		start = new Point( 5, 10 );
		end = new Point( 30, 40 );
		
		// Linie anlegen
		line = new Line( start, end );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}
	
	/**
	 * Testet die Anzahl der Punkte in der Linie
	 */
	@Test
	public void testLineNumberOfPoints()
	{
		ArrayList< Point > points = line.getPoints( );
		
		// Linie sollte nur zwei Punkte enthalten
		assertEquals( points.size( ), 2 );
	}
	
	/**
	 * Überprüft, ob Start- und Endpunkt identisch zu den eingegebenen sind und unabhänging von der Linie verschieben lassen
	 */
	@Test
	public void testStartAndEndPoint( )
	{
		ArrayList< Point > points = line.getPoints( );
		
		// Start- und Endpunkt sollten Kopien sein und nicht dasselbe Objekt
		assertNotSame( points.get( 0 ), start );
		assertNotSame( points.get( 1 ), end );
		
		// Nämlich den Start- und Endpunkt
		assertEquals( points.get( 0 ), start );
		assertEquals( points.get( 1 ), end );
		
		// Sobald man einen der beiden Punkte verschiebt, sollten diese nicht mehr gleich sein
		// (d.h. Änderungen an start/end ändern die Linie nicht)
		start.move( 3, 0 );
		assertThat( points.get( 0 ), not( equalTo( start ) ) );
		
		end.move( 1, 5 );
		assertThat( points.get( 1 ), not( equalTo( end ) ) );
	}
}
