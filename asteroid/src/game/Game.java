package game;

import java.util.Random;

/**
 * Das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105252316
 */
public final class Game implements Runnable
{
	public Game( )
	{
		Sprite s = new Sprite( 100, 100, new Random( ).nextInt( 50 ) + 30 );
		s.createAsteroid( );
		new Thread( s ).start( );
		
		ControlledSprite cs = new ControlledSprite( 300, 300, 15 );
		cs.createShip( );
		cs.setAngle( 20 );
		cs.setMoveSpeed( 1 );
		new Thread( cs ).start( );
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
