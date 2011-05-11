package shapes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Methoden der Figur-Klasse
 * 
 * @author Marcus Bauer
 * @version 201104220244
 */
public class FigureTest
{
	Figure snowMan = null;

	@Before
	public void setUp() throws Exception
	{
		snowMan = Figure.snowMan( );
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Erstellt einen Schneemann
	 */
	@Test
	public void testSnowMan()
	{
		assertNotNull( snowMan );
	}

	/**
	 * Zeichnet den Schneemann
	 */
	@Test
	public void testDraw()
	{
		// Erstellen
		assertNotNull( snowMan );
		
		// Zeichnen
		snowMan.draw( );
	}
	
	/**
	 * Test für getShapes
	 */
	@Test
	public void testGetShapes( )
	{
		Figure figure = new Figure( );
		
		// Sollte noch kein Drawable haben
		assertEquals( figure.getShapes( ).size( ), 0 );
		
		// Neue Figur einfügen
		figure.addShape( new Circle( 20, new Point( 50, 50 ) ) );
		
		// Jetzt sollte ein Drawable enthalten sein
		assertEquals( figure.getShapes( ).size( ), 1 );
		
		// Entfernen aus der von getShapes( ) zurückgelieferten Liste soll die Figur nicht ändern
		figure.getShapes( ).remove( 0 );
		assertEquals( figure.getShapes( ).size( ), 1 );
	}
	
	@Test
	public void testEqualsRelative( )
	{
		// Triviale Fälle
		Point move = new Point( 0, 0 );
		assertTrue( snowMan.equalsRelative( snowMan, move ) );
		assertFalse( snowMan.equalsRelative( snowMan, new Point( 5, 7 ) ) );
		assertFalse( snowMan.equalsRelative( null, move ) );
		assertFalse( snowMan.equalsRelative( new Circle( 20, new Point( 0, 100 ) ), move ) );
		
		Figure snowMan2 = Figure.snowMan( );
		assertTrue( snowMan.equalsRelative( snowMan2, move ) );
		
		// andere Figur mit unterschiedlichen Shapes
		Figure f = new Figure( );
		f.addShape( new Line( new Point( 5, 5 ), new Point( 7, 8 ) ) );
		f.addShape( new Circle( 25, new Point( 33, 22 ) ) );
		f.addShape( new Circle( 30, new Point( 44, 22 ) ) );
		assertFalse( f.equalsRelative( snowMan, move ) );
		
		// Reihenfolge sollte egal sein
		Figure f2 = new Figure( );
		f2.addShape( new Circle( 30, new Point( 44, 22 ) ) );
		f2.addShape( new Line( new Point( 5, 5 ), new Point( 7, 8 ) ) );
		f2.addShape( new Circle( 25, new Point( 33, 22 ) ) );
		
		// Figur verschieben
		f2.move( 2, 2 );
		assertFalse( f.equalsRelative( f2, new Point( 1, 1 ) ) );
		assertTrue( f.equalsRelative( f2, new Point( 2, 2 ) ) );
	}
	
	@Test
	public void testEqualsIgnorePosition( )
	{
		// Triviale Fälle
		assertTrue( snowMan.equalsIgnorePosition( snowMan ) );
		assertFalse( snowMan.equalsIgnorePosition( null ) );
		
		Figure snowMan2 = Figure.snowMan( );
		assertTrue( snowMan.equalsIgnorePosition( snowMan2 ) );
		
		// Schneemann verschieben, sollte immernoch gleich sein
		snowMan.move( 3, 5 );
		assertTrue( snowMan2.equalsIgnorePosition( snowMan ) );
		
		// andere Figur mit unterschiedlich vielen Shapes
		snowMan.addShape( new Circle( 30, new Point( 10, 10 ) ) );
		assertFalse( snowMan.equalsIgnorePosition( snowMan2 ) );
	}

	/**
	 * Ruft die main-Methode auf
	 * @throws InterruptedException 
	 */
	@Test
	public void testMain() throws InterruptedException
	{
		Figure.main(new String[0]);
	}
	
	/**
	 * Testet getFirstPoint()
	 */
	@Test
	public void testGetFirstPoint( )
	{
		Figure f = new Figure( );
		assertEquals( f.getFirstPoint( ), new Point( 0, 0 ) );
		
		// Erste Shape
		Point p = new Point( 20, 40 );
		f.addShape( new Circle( 5, p ) );
		assertEquals( f.getFirstPoint( ), p );
		assertNotSame( f.getFirstPoint( ), p );
	}
}
