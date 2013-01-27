package mapa;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

//************************************************************************
/** Clase que gestiona los mapas.
*/
public class Mapa
{
private String m_fichero;
private int m_filas, m_columnas, m_pesos[];
private HashSet<Posicion> m_gasolineras = new HashSet<Posicion>();
private LinkedList<Posicion> m_camino;
private double m_capacidadDeposito;
private Posicion m_inicio, m_objetivo;

public static final Color m_colores[] = new Color[10];

static
{
    for(int i = 0; i < m_colores.length; i++)
    {
        int n = 255 - 16 * i;
        m_colores[i] = new Color(n, n, n);
    }
}

//------------------------------------------------------------------------
/** Construye un mapa para aplicar los algoritmos de búsqueda.
@param ficheroMapa Fichero que contiene el mapa.
@param capacidadDeposito Capacidad del depósito de gasolina.
@throws IOException
*/
public Mapa(String ficheroMapa, double capacidadDeposito,
            Posicion inicio, Posicion objetivo)
    throws IOException
{
    leerMapa(ficheroMapa);
    m_capacidadDeposito = capacidadDeposito;
    m_inicio   = inicio;
    m_objetivo = objetivo;
}

//------------------------------------------------------------------------
/** Construye un mapa con un camino para visualizarlo por pantalla.
@param ficheroMapa Fichero que contiene el mapa.
@param ficheroCamino Fichero que contiene el camino.
@throws IOException
*/
public Mapa(String ficheroMapa, String ficheroCamino) throws IOException
{
    leerMapa(ficheroMapa);

    if(ficheroCamino != null)
        leerCamino(ficheroCamino);
}

//------------------------------------------------------------------------
private void leerMapa(String fichero) throws IOException
{
    String filas = "filas=",
           cols  = "columnas=",
           gas   = "gasolinera=";

    m_fichero = fichero;
    BufferedReader in = new BufferedReader(new FileReader(fichero));
    String line;
    int iFila = 0;

    while((line = in.readLine()) != null)
    {
        line = line.trim();

        if(line.startsWith(filas))
            m_filas = Integer.parseInt(line.substring(filas.length()));
        else if(line.startsWith(cols))
            m_columnas = Integer.parseInt(line.substring(cols.length()));
        else if(line.startsWith(gas))
            m_gasolineras.add(new Posicion(line.substring(gas.length())));
        else if(!line.startsWith("%") && line.length() > 0)
            leerFila(line, iFila++);
        else
            throw new IOException("Formato erróneo:\n"+ line);
    }

    in.close();
}

//------------------------------------------------------------------------
/** Indica un camino para dibujarlo.
*/
public void setCamino(LinkedList<Posicion> camino)
{
    m_camino = camino;
}

//------------------------------------------------------------------------
private void leerCamino(String fichero) throws IOException
{
    m_camino = new LinkedList<Posicion>();
    BufferedReader in = new BufferedReader(new FileReader(fichero));
    String line;

    while((line = in.readLine()) != null)
    {
        line = line.trim();

        if(line.length() > 0)
            m_camino.add(new Posicion(line));
    }

    in.close();
}

//------------------------------------------------------------------------
private void leerFila(String linea, int iFila) throws IOException
{
    if(m_filas == 0)
        throw new IOException("No se ha indicado el número de filas.");
    if(m_columnas == 0)
        throw new IOException("No se ha indicado el número de columnas.");

    if(m_pesos == null)
        m_pesos = new int[m_filas * m_columnas];

    int n = linea.length();

    if(n != m_columnas)
        throw new IOException("El número de columnas no coincide.");
    if(iFila >= m_filas)
        throw new IOException("El número de filas no coincide.");

    for(int i = 0; i < n; i++)
    {
        char c = linea.charAt(i);
        int p;

        if(c == '#')
            p = Integer.MAX_VALUE; // Obstáculo.
        else if(c >= '1' && c <= '9')
            p = c - '0';
        else
            throw new IOException("Celda errónea: "+ c);

        m_pesos[iFila * m_columnas + i] = p;
    }
}

//------------------------------------------------------------------------
/** Obtiene la capacidad del depósito de gasolina.
*/
public double getCapacidadDeposito()
{
    return m_capacidadDeposito;
}

//------------------------------------------------------------------------
/** Obtiene la posicion inicial de la búsqueda.
*/
public Posicion getInicio()
{
    return m_inicio;
}

//------------------------------------------------------------------------
/** Obtiene la posición objetivo de la búsqueda.
*/
public Posicion getObjetivo()
{
    return m_objetivo;
}

//------------------------------------------------------------------------
/** Indica si hay una gasolinera en la posición indicada.
*/
public boolean getGasolinera(Posicion p)
{
    return m_gasolineras.contains(p);
}

//------------------------------------------------------------------------
/** Indica si hay una gasolinera en la posición indicada.
*/
public boolean getGasolinera(int fila, int columna)
{
    return getGasolinera(new Posicion(fila, columna));
}

//------------------------------------------------------------------------
/** Devuelve el peso de una posición.
*/
public int getPeso(Posicion p)
{
    return getPeso(p.getFila(), p.getColumna());
}

//------------------------------------------------------------------------
/** Devuelve el peso de una posición.
*/
public int getPeso(int fila, int columna)
{
    assert fila >= 0 && fila < m_filas;
    assert columna >= 0 && columna < m_columnas;
    return m_pesos[fila * m_columnas + columna];
}

//------------------------------------------------------------------------
/** Indica si una posición es válida, es decir, si está dentro de los
límites del mapa y no hay un obstáculo en ella.
*/
public boolean posicionValida(int fila, int columna)
{
    return fila >= 0 && fila < m_filas &&
           columna >= 0 && columna < m_columnas &&
           !getObstaculo(fila, columna);
}

//------------------------------------------------------------------------
/** Indica si hay un obstáculo en la posición indicada.
*/
private boolean getObstaculo(int fila, int columna)
{
    return getPeso(fila, columna) == Integer.MAX_VALUE;
}

//------------------------------------------------------------------------
/** Convierte el mapa en texto.
*/
@Override public String toString()
{
    StringBuilder sb = new StringBuilder();
    sb.append("     Tamaño: "+ m_filas +" x "+ m_columnas +"\n");
    sb.append("Gasolineras:");

    for(Posicion p : m_gasolineras)
        sb.append(" ("+ p +")");

    sb.append('\n');

    for(int i = 0; i < m_filas; i++)
    {
        for(int j = 0; j < m_columnas; j++)
        {
            int peso = m_pesos[i * m_columnas + j];

            sb.append(peso == Integer.MAX_VALUE
                      ? "#" : Integer.toString(peso));
        }

        sb.append('\n');
    }

    return sb.toString();
}

//------------------------------------------------------------------------
private void dibujar(Graphics2D g, int width, int height)
{
    int d = 64,
        w = m_columnas * d,
        h = m_filas * d;

    double sx = (double)width / (double)(w+1),
           sy = (double)height / (double)(h+1),
           scale = Math.min(sx, sy);

    double tx = sy > sx ? 0 : (width - sy * w) / 2,
           ty = sx > sy ? 0 : (height - sx * h) / 2;

    g.transform(AffineTransform.getTranslateInstance(tx, ty));
    g.transform(AffineTransform.getScaleInstance(scale, scale));

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);

    for(int i = 0; i < m_filas; i++)
    {
        for(int j = 0; j < m_columnas; j++)
        {
            int y = i * d,
                x = j * d;

            int peso = m_pesos[i * m_columnas + j];

            Color c = peso == Integer.MAX_VALUE
                      ? Color.darkGray : m_colores[peso];

            g.setColor(c);
            g.fillRect(x, y, d, d);
        }
    }

    g.setColor(Color.black);
    g.setStroke(new BasicStroke(1));

    for(int i = 0; i <= m_filas; i++)
    {
        for(int j = 0; j <= m_columnas; j++)
        {
            int y = i * d,
                x = j * d;

            g.drawLine(x, 0, x, h);
            g.drawLine(0, y, w, y);
        }
    }

    g.setStroke(new BasicStroke(3));

    if(m_camino != null && !m_camino.isEmpty())
    {
        Iterator<Posicion> it = m_camino.iterator();
        g.setColor(Color.magenta);
        Posicion p = it.next();
        dibujarPunto(g, p, d);

        while(it.hasNext())
        {
            Posicion q = it.next();
            dibujarPunto(g, q, d);
            dibujarLinea(g, p, q, d);
            p = q;
        }
    }

    g.setFont(new Font("SansSerif", Font.PLAIN, 30));

    for(int i = 0; i < m_filas; i++)
    {
        for(int j = 0; j < m_columnas; j++)
        {
            int y = i * d + 30,
                x = j * d + 5;

            int peso = m_pesos[i * m_columnas + j];

            if(peso != Integer.MAX_VALUE)
            {
                g.setColor(Color.black);
                g.drawString(Integer.toString(peso), x, y);
            }

            if(getGasolinera(i, j))
            {
                g.setColor(Color.blue);
                g.drawString("G", x + 30, y + 27);
            }
        }
    }
}

//------------------------------------------------------------------------
private void dibujarPunto(Graphics2D g, Posicion p, int d)
{
    int x = p.getColumna() * d + d / 2,
        y = p.getFila()    * d + d / 2;

    g.fillRect(x-4, y-4, 8, 8);
}

//------------------------------------------------------------------------
private void dibujarLinea(Graphics2D g, Posicion p, Posicion q, int d)
{
    g.drawLine(p.getColumna() * d + d / 2,
               p.getFila()    * d + d / 2,
               q.getColumna() * d + d / 2,
               q.getFila()    * d + d / 2);
}

//------------------------------------------------------------------------
/** Muestra el mapa en una ventana.
*/
public void mostrar()
{
    JFrame f = new JFrame(m_fichero);
    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JComponent c = new JComponent()
    {@Override public void paint(Graphics g)
    {
        Dimension d = getSize();
        dibujar((Graphics2D)g, d.width, d.height);
    }};

    f.getContentPane().add(c);
    f.setSize(800, 600);
    f.setLocationRelativeTo(null);
    f.setVisible(true);
}

//------------------------------------------------------------------------
/** Muestra un mapa y opcionalmente un camino guardados en ficheros.
*/
public static void main(String args[]) throws IOException
{
    if(args.length == 0)
    {
        System.out.println("Parámetros: mapa [camino]");
        System.exit(0);
    }

    String mapa   = args[0],
           camino = args.length==2 ? args[1] : null;

    Mapa m = new Mapa(mapa, camino);
    System.out.println(m);
    m.mostrar();
}

} // Mapa
