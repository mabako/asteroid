package game;

import java.util.Random;

/**
 * Das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105251156
 */
public class Game implements Runnable
{
	public Game( )
	{
		new Sprite( 100, 100, new Random( ).nextInt( 50 ) + 30 );
	}

	/**
	 * Thread-Funktion
	 */
	@Override
	public void run( )
	{
		while( true )
		{
			try
			{
				Thread.sleep( 100 );
			}
			catch( InterruptedException e )
			{
				break;
			}
		}
	}
}
