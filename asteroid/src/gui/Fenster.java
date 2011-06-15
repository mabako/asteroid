package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Fenster extends JFrame
{

	/**
	 * Serial (automatisch generiert)
	 */
	private static final long serialVersionUID = 2939982606311199148L;

	/** Label für Leben */
	JLabel lives = new JLabel( );

	/**
	 * Erstellt ein neues Fenster
	 */
	public Fenster( )
	{
		super( );
		setAlwaysOnTop( true );

		lives.setText( "Leben: ?" );
		add( lives );

		pack( );
		setVisible( true );
	}

	public void setLives( int lives )
	{
		this.lives.setText( lives == 0 ? "Game Over" : ( "Leben: " + lives ) );
	}
}
