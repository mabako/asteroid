package shapes;

/**
 * Exception für ungültige Polygone
 * 
 * @author Marcus Bauer
 * @version 201103231203
 */
public class PolygonShapeException extends Exception
{
	/**
	 * Serial
	 */
	private static final long serialVersionUID = -4953404114348736829L;

	/**
	 * Konstruktor für die Exception mit Meldung
	 * 
	 * @param string
	 *            Fehlermeldung
	 */
	public PolygonShapeException( String string )
	{
		super( string );
	}
}
