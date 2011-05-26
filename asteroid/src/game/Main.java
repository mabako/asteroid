package game;

import gui.GameKeyListener;

/**
 * Hauptklasse für das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105251600
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
		GameKeyListener keyListener = new GameKeyListener( );
		
		// Spiel-Instanz erstellen
		Game game = new Game( keyListener );

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
