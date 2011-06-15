/*
 * WhiteBoard.java
 *
 * First created on 24. Februar 2003, 11:09
 * Revision history
 * $Log: WhiteBoard.java,v $
 * Revision 1.7  2003/04/29 14:36:20  georg
 * Fehler beim  windowExited Flag behoben
 * removeShape geaendert: initiiert kein repaint, sondern vertraut
 * auf die naechste draw... Methode. => reduziertes Flickern.
 * Dafuer neue Methode wipeShape, die repaint aufruft.
 *
 * Revision 1.6  2003/04/19 14:40:32  georg
 * no message
 *
 * Revision 1.5  2003/04/17 18:05:34  georg
 * WhiteBoard wird automatisch neu aufgebaut,
 * falls der User den Frame geschlossen hat.
 *
 * Revision 1.4  2003/04/17 17:37:05  georg
 * Frame-Konstruktion aus Konstruktor ausgelagert
 *
 * Revision 1.3  2003/04/17 11:44:38  georg
 * Umstellung von Vererbung von JComponent auf Verwendung von JComponent
 *
 * Revision 1.2  2003/04/17 09:52:07  georg
 * Umstellung auf Threadsafe Collection
 *
 */
package teaching;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

import java.util.*;

/** Eine grafische Zeichenflaeche, auf der einfache
 * Grundoperationen zum Zeichnen geometrischer Formen
 * ausgefuehrt werden koennen.<br>
 * Es stehen Methoden zum Zeichnen von Punkten, Geraden,
 * Rechtecken, Polygonen, Ellipsen und Parabelsegmenten zur Verfuegung.<br>
 * Alle Zeichenmethoden liefern ein  Objekt zurueck, z.B.
 * {@link WhiteBoard#drawLine(double, double, double, double)}, dass fuer
 * die removeShape-Methode "aufgehoben" werden muss, siehe {@link #removeShape(Object)}
 * @author $Author Georg Beier$
 * @version $Revision: 1.7 $ vom $Date: 2003/04/29 14:36:20 $
 */
public class WhiteBoard {

    /** Das aeussere Fenster */
    private JFrame frame;
    /** Die "Arbeitsflaeche" im aeusseren Fenster */
    private Container container;
    /** inneres Fenster mit Rollbalken */
    private JScrollPane scrollPane;
    /** Liste aller Zeichenelemente */
    private java.util.List<AttributedShape> shapes = Collections.synchronizedList(new ArrayList<AttributedShape>());
    /** Flag das angibt, ob Fenster geschlossen wurde */
    private boolean windowExited = false;
    /** Action Listener Objekt, dass auf das WindowClose
     * Ereignis des aeusseren Frame reagiert.
     */
    private WindowListener closeListener;
    /** Koordinaten der Randpunkte */
    private double minX;
    /** Koordinaten der Randpunkte */
    private double maxX;
    /** Koordinaten der Randpunkte */
    private double minY;
    /** Koordinaten der Randpunkte */
    private double maxY;
    /** Komponente, die die eigentliche Darstellung uebernimmt. */
    private JComponent graphicalComponent = new DrawingArea();
    /** Gibt an, ob die Spielfeldgrenzen neu berechnet werden sollen */
    private static final boolean increaseBounds = false;
    /** aktueller KeyListener */
    private KeyListener keyListener = null;

    /** Legt ein neues WhiteBoard-Objekt an. Dieses wird automatisch
     * in einem eigenen Fenster dargestellt.
     */
    public WhiteBoard() {
        minX = maxX = minY = maxY = 0;
        if(!increaseBounds)
        {
        	maxX = 800;
        	maxY = 600;
        }
        closeListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                windowExited = true;
                System.exit(0);
            }
        };
        initFrame();
    }

    /** Legt alle zum Zeichnen benoetigten Objekte an */
    private void initFrame() {
        frame = new JFrame("Asteroids");
        container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(graphicalComponent);
        scrollPane.getViewport().setBackground(Color.BLACK);
        graphicalComponent.setForeground(Color.GREEN);
        container.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.addWindowListener(closeListener);
        frame.setVisible(true);
        graphicalComponent.repaint();
    }
    
    /**
     * Löscht alle Objekte von der Zeichenfläche
     */
    public synchronized void clear()
    {
    	shapes.clear( );
   		graphicalComponent.repaint();
    }

    /** Neuberechnung der Groesse der Zeichenflaeche. Dazu werden alle Shapes
     * durchgearbeitet.
     * @return gibt an, ob sich die Groesse geaendert hat
     */
    @SuppressWarnings( "unused" )
	private boolean recomputeBounds() {
    	if(!increaseBounds)
    		return false;
        AttributedShape as;
        Rectangle r;
        boolean boundsChanged = (shapes.size() == 0);
        double lminX = 0, lmaxX = 0, lminY = 0, lmaxY = 0;
        double dx, dy;
        int diag;
        Point p;
        synchronized (shapes) {
            for (Iterator<AttributedShape> it = shapes.iterator(); it.hasNext();) {
                as = it.next();
                r = as.shape.getBounds();
                if (as.rotation != 0.0) {
                    p = r.getLocation();
                    dx = r.getWidth();
                    dy = r.getHeight();
                    diag = (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
                    r = new Rectangle(p, new Dimension(diag, diag));
                }
                lminX = Math.min(lminX, r.getMinX());
                lmaxX = Math.max(lmaxX, r.getMaxX());
                lminY = Math.min(lminY, r.getMinY());
                lmaxY = Math.max(lmaxY, r.getMaxY());
            }
        }
        if (this.minX < lminX) {
            boundsChanged = true;
            this.minX = lminX;
        }
        if (this.minY < lminY) {
            boundsChanged = true;
            this.minY = lminY;
        }
        if (this.maxX > lmaxX) {
            boundsChanged = true;
            this.maxX = lmaxX;
        }
        if (this.maxY > lmaxY) {
            boundsChanged = true;
            this.maxY = lmaxY;
        }
        if (boundsChanged) {
            graphicalComponent.revalidate();
        }

        System.out.println("New bounds: " + minX + " " + minY + " " + maxX + " " + maxY);
        return boundsChanged;
    }

    /** Neuberechnung der Groesse der Zeichenflaeche, wenn ein Shape hinzugefuegt wurde
     * @return gibt an, ob sich die Groesse geaendert hat
     * @param s hinzugefuegtes Zeichnungselement
     */
    private boolean recomputeBounds(Shape s) {
        return recomputeBounds(s, 0.);
    }

    /** Neuberechnung der Groesse der Zeichenflaeche, wenn ein gedrehtes Zeichnungselement
     * hinzugefuegt wird.
     * @return gibt an, ob sich die Groesse geaendert hat
     * @param s hinzugefuegtes Zeichnungselement
     * @param rotation Drehung des Zeichenelements
     */
    @SuppressWarnings( "unused" )
	private boolean recomputeBounds(Shape s, double rotation) {
    	if(!increaseBounds)
    		return false;
    	
        Rectangle r = s.getBounds();
        if (rotation != 0.0) {
            Point p = r.getLocation();
            double dx = r.getWidth();
            double dy = r.getHeight();
            int diag = (int) Math.ceil(Math.sqrt(dx * dx + dy * dy));
            p.translate(-diag / 2, -diag / 2);
//            r = new Rectangle(p, new Dimension(diag, diag));
            r = new Rectangle(p, new Dimension(10 * diag / 7, 10 * diag / 7));
        }
        double x1 = r.getMinX();
        double x2 = r.getMaxX();
        double y1 = r.getMinY();
        double y2 = r.getMaxY();
        boolean boundsChanged = false;
        if (minX > x1) {
            boundsChanged = true;
            minX = x1;
        }
        if (minY > y1) {
            boundsChanged = true;
            minY = y1;
        }
        if (maxX < x2) {
            boundsChanged = true;
            maxX = x2;
        }
        if (maxY < y2) {
            boundsChanged = true;
            maxY = y2;
        }
        if (boundsChanged) {
            graphicalComponent.revalidate();
        }
        
        System.out.println("New bounds: " + minX + " " + minY + " " + maxX + " " + maxY);
        return boundsChanged;
    }

    /** Zeichnet eine Linie in default-Farbe.
     * @param xfrom x Startkoordinate
     * @param yfrom y Startkoordinate
     * @param xto x Zielkoordinate
     * @param yto y Zielkoordinate
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawLine(double xfrom, double yfrom, double xto, double yto) {
        return drawLine(xfrom, yfrom, xto, yto, Color.BLACK);
    }

    /** Zeichnet eine farbige Linie.
     * @param xfrom x Startkoordinate
     * @param yfrom y Startkoordinate
     * @param xto x Zielkoordinate
     * @param yto y Zielkoordinate
     * @param color Linienfarbe. {@link java.awt.Color}
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawLine(double xfrom, double yfrom, double xto, double yto, Color color) {
        Shape s = new Line2D.Double(xfrom, yfrom, xto, yto);
        rebuild();
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, 0., false);
        shapes.add(as);
        recomputeBounds(s);
        container.repaint();
        return as;
    }

    /** Zeichnet einen parabolischen Bogen in default-Farbe.
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     * @param xfrom x Startkoordinate
     * @param yfrom y Startkoordinate
     * @param xto x Zielkoordinate
     * @param yto y Zielkoordinate
     * @param excentricity maximale Auslenkung des Bogens aus der Verbindungsgerade
     */
    public Object drawArc(double xfrom, double yfrom, double xto, double yto, double excentricity) {
        return drawArc(xfrom, yfrom, xto, yto, excentricity, Color.BLACK, false);
    }

    /** Zeichnet einen farbigen, evtl. gefuellten parabolischen Bogen.
     * @param xfrom x Startkoordinate
     * @param yfrom y Startkoordinate
     * @param xto x Zielkoordinate
     * @param yto y Zielkoordinate
     * @param excentricity maximale Auslenkung des Bogens aus der Verbindungsgerade
     * @param color Linienfarbe. {@link java.awt.Color}
     * @param solid true, wenn Zeichnungsobjekt gefuellt werden soll.
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawArc(double xfrom, double yfrom, double xto, double yto, double excentricity, Color color, boolean solid) {
        double xs, ys, d, dx, dy, r, exx, exy;
        rebuild();
        dx = xto - xfrom;
        dy = yto - yfrom;
        d = Math.sqrt(dx * dx + dy * dy);
        r = excentricity;
        exx = dy * r / d;
        exy = dx * r / d;
        xs = ((xfrom + xto) / 2) - 2 * exx;
        ys = ((yfrom + yto) / 2) + 2 * exy;
        Shape s = new QuadCurve2D.Double(xfrom, yfrom, xs, ys, xto, yto);
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, 0., solid);
        shapes.add(as);
        recomputeBounds(s);
        container.repaint();
        return as;
    }

    /** Zeichnet einen schwarzen Punkt
     * @param x Koordinate
     * @param y Koordinate
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawPoint(double x, double y) {
        return drawPoint(x, y, Color.BLACK);
    }

    /** Zeichnet einen farbigen Punkt
     * @param x Koordinate
     * @param y Koordinate
     * @param color Linienfarbe. {@link java.awt.Color}
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawPoint(double x, double y, Color color) {
        rebuild();
        Shape s = new Ellipse2D.Double(x - 1, y - 1, 2, 2);
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, 0., true);
        shapes.add(as);
        recomputeBounds(s);
        container.repaint();
        return as;
    }

    /** Zeichnet eine schwarze Ellipse
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param hx x Achse
     * @param hy y Achse
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawEllipse(double x, double y, double hx, double hy) {
        return drawEllipse(x, y, hx, hy, Color.BLACK, false, 0.0);
    }

    /** Zeichnet eine farbige Ellipse
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param hx x Achse
     * @param hy y Achse
     * @param color Linienfarbe. {@link java.awt.Color}
     * @param solid true, wenn Zeichnungsobjekt gefuellt werden soll.
     * @param rotation Drehung des Objekts, Winkel im Bogenmass!
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawEllipse(double x, double y, double hx, double hy, Color color, boolean solid, double rotation) {
        rebuild();
        Shape s = new Ellipse2D.Double(x - hx, y - hy, 2 * hx, 2 * hy);
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, rotation, solid);
        shapes.add(as);
        recomputeBounds(s, rotation);
        container.repaint();
        return as;
    }

    /** Zeichnet einen farbigen Kreis
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param radius Radius
     * @param color Linienfarbe. {@link java.awt.Color}
     * @param solid true, wenn Zeichnungsobjekt gefuellt werden soll.
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawCircle(double x, double y, double radius, Color color, boolean solid) {
        return drawEllipse(x, y, radius, radius, color, solid, 0.0);
    }

    /** Zeichnet einen schwarzen Kreis
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param radius Radius
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawCircle(double x, double y, double radius) {
        return drawEllipse(x, y, radius, radius, Color.BLACK, false, 0.0);
    }

    /** Zeichnet ein schwarzes Polygon
     * Achtung: Obwohl die Eckpunkte als <code>double</code> angegeben werden,
     * wird intern mit <code>int</code> gerechnet und entsprechend gerundet!
     * @param x Eckpunkt-Koordinaten x
     * @param y Eckpunkt-Koordinaten y
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawPolygon(double[] x, double[] y) {
        return drawPolygon(x, y, Color.BLACK, false, 0.0);
    }

    /** Zeichnet ein farbiges Polygon
     * Achtung: Obwohl die Eckpunkte als <code>double</code> angegeben werden,
     * wird intern mit <code>int</code> gerechnet und entsprechend gerundet!
     * @param x Eckpunkt-Koordinaten x
     * @param y Eckpunkt-Koordinaten y
     * @param color Linienfarbe. {@link java.awt.Color}
     * @param solid true, wenn Zeichnungsobjekt gefuellt werden soll.
     * @param rotation Drehung des Objekts, Winkel im Bogenmass!
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawPolygon(double[] x, double y[], Color color, boolean solid, double rotation) {
        rebuild();
        int[] ix = new int[x.length], iy = new int[y.length];
        int pl = Math.min(x.length, y.length);
        for (int i = 0; i < pl; i++) {
            ix[i] = (int) x[i];
            iy[i] = (int) y[i];
        }
        Shape s = new java.awt.Polygon(ix, iy, pl);
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, rotation, solid);
        shapes.add(as);
        recomputeBounds(s, rotation);
        container.repaint();
        return as;
    }

    /** Zeichnet ein schwarzes Rechteck
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param hx x Seitenlaenge
     * @param hy y Seitenlaenge
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawRectangle(double x, double y, double hx, double hy) {
        return drawRectangle(x, y, hx, hy, Color.BLACK, false, 0.0);
    }

    /** Zeichnet ein farbiges Rechteck
     * @param x Mittelpunkts-Koordinate
     * @param y Mittelpunkts-Koordinate
     * @param hx x Seitenlaenge
     * @param hy y Seitenlaenge
     * @param color Linienfarbe. {@link java.awt.Color}
     * @param solid true, wenn Zeichnungsobjekt gefuellt werden soll.
     * @param rotation Drehung des Objekts, Winkel im Bogenmass!
     * @return Referenz auf das intern verwendete Zeichnungsobjekt. Wird benoetigt, um das
     * Objekt wieder zu loeschen.
     */
    public Object drawRectangle(double x, double y, double hx, double hy, Color color, boolean solid, double rotation) {
        rebuild();
        Shape s = new Rectangle2D.Double(x - hx, y - hy, 2 * hx, 2 * hy);
        AttributedShape as = new WhiteBoard.AttributedShape(s, color, rotation, solid);
        shapes.add(as);
        recomputeBounds(s, rotation);
        container.repaint();
        return as;
    }

    /** Loescht ein Objekt nur aus der internen Speicherstruktur.
     * gObjekt wird nicht von der Zeichenflaeche geloescht, dies geschieht
     * erst beim naechsten redraw. Dadurch wird ein Flickern beim Bewegen von
     * Objekten vermieden.
     * @param o Referenz auf das interne Zeichenobjekt
     */
    public void removeShape(Object o) {
        shapes.remove(o);
    }

    /** Loescht ein Objekt aus der internen Speicherstruktur und von
     * der Zeichenflaeche. Fuehrt zu Bildschirmflickern, wenn diese Methode
     * zum Bewegen von Objekten verwendet wird.
     * @param o Referenz auf das interne Zeichenobjekt
     */
    public void wipeShape(Object o) {
        if (shapes.remove(o)) {
            recomputeBounds();
            graphicalComponent.repaint();
        }
    }

    /** Baut Windows wieder auf, falls der Frame geschlossen wurde. */
    private void rebuild() {
        if (windowExited) {
            windowExited = false;
            initFrame();
        }
    }

    /** Interne Klasse zum Abspeichern der Zeichenobjekte mit verschiedenen Eigenschaften
     */
    public static class AttributedShape {

        Color color;
        double rotation;
        Shape shape;
        boolean isFilled;

        AttributedShape(Shape s) {
            this(s, Color.BLACK, 0.0, false);
        }

        AttributedShape(Shape s, Color c, double rot, boolean solid) {
            shape = s;
            color = c;
            rotation = rot;
            isFilled = solid;
        }
    }

    /** Innere Klasse stellt die aktuelle Zeichenflaeche zur Verfuegung. */
    private class DrawingArea extends JComponent {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1895038473047301509L;

		/** stellt die Zeichenflaeche mit allen enthaltenen grafischen
         * Objekten neu dar. Wird automatisch aufgerufen, wenn das
         * Window neu gezeichnet werden muss. Wird auch indirekt ueber repaint() oder
         * revalidate aufgerufen.
         * @param g neues, vom Windowsystem bereitgestelltes Grafics-Objekt, auf dem das Whiteboard
         * neu aufgebaut wird.
         */
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            AttributedShape as;
            double x = 0.0, y = 0.0;
            Rectangle r;
            Rectangle clip = g.getClipBounds();
            double ysize = clip.getHeight();
            double xt = Math.min(minX - 1, 0);
            double yt = Math.max(maxY + 1, ysize + Math.min(0., minY));
            g2.scale(1.0, -1.0);
            g2.translate(-xt, -yt);
            synchronized (shapes) {
                for (Iterator<AttributedShape> it = shapes.iterator(); it.hasNext();) {
                    as = it.next();
                    g2.setPaint(as.color);
                    if (as.rotation != 0.0) {
                        r = as.shape.getBounds();
                        x = r.getCenterX();
                        y = r.getCenterY();
                        g2.rotate(as.rotation, x, y);
                    }
                    if (as.isFilled) {
                        g2.fill(as.shape);
                    } else {
                        g2.draw(as.shape);
                    }
                    if (as.rotation != 0.0) {
                        g2.rotate(-as.rotation, x, y);
                    }
                }
            }
        }

        /** gibt gewuenschte Groesse in Bildschirmeinheiten an. Wird von ScrollPane benutzt, um
         * Rollbalken zu berechnen.
         * @return Groesse der Zeichenflaeche.
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension((int) (maxX - minX + 3), (int) (maxY - minY + 3));
        }
    }

	/**
	 * @return the minX
	 */
	public double getMinX( )
	{
		return minX;
	}

	/**
	 * @return the maxX
	 */
	public double getMaxX( )
	{
		return maxX;
	}

	/**
	 * @return the minY
	 */
	public double getMinY( )
	{
		return minY;
	}

	/**
	 * @return the maxY
	 */
	public double getMaxY( )
	{
		return maxY;
	}
	
	/**
	 * Setzt einen KeyListener für das JFrame
	 * @param keyListener der zu setzende KeyListener
	 */

	public void setKeyListener( KeyListener keyListener )
	{
		frame.removeKeyListener( this.keyListener );
		frame.addKeyListener( keyListener );
		this.keyListener = keyListener;
	}
}