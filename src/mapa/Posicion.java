package mapa;

//************************************************************************
/** Clase para almacenar una posición del mapa.
*/
public class Posicion implements Comparable<Posicion>
{
private int m_fila, m_columna;

//------------------------------------------------------------------------
/** Construye una posición a partir de su fila y columna.
@param fila Fila de la posición.
@param columna Columna de la posición.
*/
public Posicion(int fila, int columna)
{
    m_fila = fila;
    m_columna = columna;
}

//------------------------------------------------------------------------
/** Construye un objeto a partir de su representación como cadena.
@param filaComaColumna Fila y columna separadas por una coma.
*/
public Posicion(String filaComaColumna)
{
    int coma = filaComaColumna.indexOf(',');
    m_fila = Integer.parseInt(filaComaColumna.substring(0, coma));
    m_columna = Integer.parseInt(filaComaColumna.substring(coma + 1));
}

//------------------------------------------------------------------------
/** Obtiene la fila de la posición.
@return Fila de la posición.
*/
public int getFila()
{
    return m_fila;
}

//------------------------------------------------------------------------
/** Obtiene la columna de la posición.
@return Columna de la posición.
*/
public int getColumna()
{
    return m_columna;
}

//------------------------------------------------------------------------
/** Comprueba si una posición es vecina de esta posición.
@param p Posición que se desea comprobar si es vecina.
@return <code>true</code> si la posición es vecina.
*/
public boolean vecina(Posicion p)
{
    return distancia2(p) <= 2;
}

//------------------------------------------------------------------------
/** Calcula la distancia entre esta posición y otra.
@param p Posición con la que hay que calcular la distancia.
@return Distancia calculada.
*/
public double distancia(Posicion p)
{
    return Math.sqrt(distancia2(p));
}

//------------------------------------------------------------------------
private int distancia2(Posicion p)
{
    int df = m_fila - p.m_fila,
        dc = m_columna - p.m_columna;

    return df*df + dc*dc;
}

//------------------------------------------------------------------------
/** Comprueba si dos posiciones son iguales.
@param obj Posición que hay que comparar.
@return <code>true</code> si las posiciones son iguales.
*/
@Override public boolean equals(Object obj)
{
    if(obj == null || getClass() != obj.getClass())
        return false; //............................................RETURN

    Posicion p = (Posicion)obj;
    return m_fila == p.m_fila && m_columna == p.m_columna;
}

//------------------------------------------------------------------------
/** Calcula el código hash de la posición para que pueda ser usada
como clave en clases como HashMap.
@return Código hash calculado.
*/
@Override public int hashCode()
{
    return 13 * (13 * 7 + m_fila) + m_columna;
}

//------------------------------------------------------------------------
/** Compara esta posición con otra.
*/
@Override public int compareTo(Posicion p)
{
    int r = m_fila - p.m_fila;
    return r == 0 ? m_columna - p.m_columna : r;
}

//------------------------------------------------------------------------
/** Convierte la posición en cadena.
@return Fila y columna separados por una coma.
*/
@Override public String toString()
{
    return m_fila +","+ m_columna;
}

} // Posicion
