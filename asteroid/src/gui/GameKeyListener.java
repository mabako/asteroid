package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Key-Listener für das Spiel
 * 
 * @author Marcus Bauer (mabako@gmail.com)
 * @version 201105261615
 */
public class GameKeyListener implements KeyListener
{
	/**
	 * Tasten, die im Spiel gedrückt werden können
	 */
	public static final int KEY_DUMMY = 0;
	public static final int KEY_DOWN = 1;
	public static final int KEY_UP = 2;
	public static final int KEY_LEFT = 3;
	public static final int KEY_RIGHT = 4;

	/** Zwischenspeicher, ob Taste gedrückt ist, oder nicht */
	private boolean pressedKeys[] = new boolean[ 5 ];

	/**
	 * Initalisiert die Tasten
	 */
	public GameKeyListener( )
	{
		for( int i = 0; i < pressedKeys.length; ++i )
			pressedKeys[i] = false;
	}

	/**
	 * Liefert die intern zugeordnete Taste zurück
	 * 
	 * @param e
	 *            das KeyEvent
	 * @return interner Keycode, oder KEY_DUMMY falls keiner
	 */
	private int getKeyForEvent( KeyEvent e )
	{
		switch( e.getKeyCode( ) )
		{
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				return KEY_UP;

			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				return KEY_DOWN;

			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				return KEY_LEFT;

			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				return KEY_RIGHT;
		}

		return KEY_DUMMY;
	}

	/**
	 * Event für Taste wird gedrückt - setzt eine der Tasten des Spiels auf
	 * aktiv
	 */
	@Override
	public synchronized void keyPressed( KeyEvent e )
	{
		pressedKeys[getKeyForEvent( e )] = true;
	}

	/**
	 * Event für Taste wird losgelassen - setzt eine der Tasten des Spiels auf
	 * inaktiv
	 */
	@Override
	public synchronized void keyReleased( KeyEvent e )
	{
		pressedKeys[getKeyForEvent( e )] = false;
	}

	@Override
	public synchronized void keyTyped( KeyEvent e )
	{
	}

	/**
	 * Liefert zurück, ob die Pseudo-Taste gedrückt ist
	 * 
	 * @param key
	 *            Taste, die geprüft werden soll
	 * @return true falls Taste gedrückt, false sonst
	 */
	public synchronized boolean isKeyPressed( int key )
	{
		return pressedKeys[key];
	}
}
