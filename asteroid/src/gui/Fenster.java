package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Kleines Fenster zur Lebensanzeige
 * 
 * @author Mathias Kuschel
 * @version 201106151242
 */
public class Fenster extends JFrame
{
	/** Serial (automatisch generiert) */
	private static final long serialVersionUID = 2939982606311199148L;

	/** Label für Leben */
	JLabel lives = new JLabel( );

	/**
	 * Erstellt ein neues Fenster
	 */
	public Fenster( )
	{
		// Konstruktor von JFrame
		super( );
		
		// Programm wird beim Schließen des Fensters beendet
		setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		// Immer sichtbar - d.h. auch über dem Asteroids-Fenster
		setAlwaysOnTop( true );

		// Text setzen & zum JFrame hinzufügen
		lives.setText( "Leben: ?" );
		add( lives );

		// Fenster zeigen
		pack( );
		setVisible( true );
	}

	/**
	 * Setzt die Anzahl der Leben bzw. zeigt Game Over bei 0
	 * @param lives Anzahl der Leben
	 */
	public void setLives( int lives )
	{
		this.lives.setText( lives == 0 ? "Game Over" : ( "Leben: " + lives ) );
	}
}
