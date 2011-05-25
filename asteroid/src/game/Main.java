package game;

/**
 * Hauptklasse für das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105251156
 */
public class Main
{

	/**
	 * Main-Methode
	 * 
	 * @param args
	 */
	public static void main( String[ ] args )
	{
		// Spiel-Instanz erstellen
		Game game = new Game( );

		// Thread dafür starten
		Thread thread = new Thread( game );
		thread.start( );

		// Auf Ende des Spieles warten
		try
		{
			thread.join( );
		}
		catch( InterruptedException e )
		{
		}
	}
}
