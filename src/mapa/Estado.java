package mapa;

import java.io.*;
import java.util.*;

//************************************************************************
/** Estado en la búsqueda del mapa.
*/
public class Estado implements Comparable<Estado>
{
private Mapa     m_mapa;
private Estado   m_padre;
private Posicion m_posicion;  // Posición que representa el estado.
private double   m_gasolina;  // Gasolina disponible.

private double   m_distancia; // Coste acumulado hasta la posición.
                              // Representa la función g(n).

//------------------------------------------------------------------------
/** Construye un estado.
@param padre Estado padre.
@param posicion Posición vecina.
*/
public Estado(Estado padre, Posicion posicion)
{
    assert padre.m_posicion.vecina(posicion);
    m_mapa = padre.m_mapa;
    m_padre = padre;
    m_posicion = posicion;

    double d = padre.m_posicion.distancia(posicion),
           p = m_mapa.getPeso(padre.m_posicion),
           q = m_mapa.getPeso(posicion),
           r = (d * (p + q)) / 2.;

    m_distancia = padre.m_distancia + r;
    m_gasolina  = padre.m_gasolina - r;

    if(gasolinaSuficiente() && m_mapa.getGasolinera(posicion))
        m_gasolina = m_mapa.getCapacidadDeposito();
}

//------------------------------------------------------------------------
/** Construye el estado inicial de un mapa.
@param mapa Mapa al que hace referencia el estado.
*/
public Estado(Mapa mapa)
{
    m_mapa     = mapa;
    m_posicion = mapa.getInicio();
    m_gasolina = mapa.getCapacidadDeposito();
}

//------------------------------------------------------------------------
/** Obtiene la distancia recorrida hasta este estado.
*/
public double getDistancia()
{
    return m_distancia;
}

//------------------------------------------------------------------------
/** Obtiene la gasolina disponible en el estado.
*/
public double getGasolina()
{
    return m_gasolina;
}

//------------------------------------------------------------------------
/** Comprueba si ha habido gasolina suficiente para llegar a este estado.
@return <code>true</code> si ha habido gasolina suficiente.
*/
final public boolean gasolinaSuficiente()
{
    return m_gasolina > -1e-6;
}

//------------------------------------------------------------------------
/** Obtiene el mapa asociado al estado.
*/
public Mapa getMapa()
{
    return m_mapa;
}

//------------------------------------------------------------------------
/** Obtiene la posicion referenciada por el estado.
*/
public Posicion getPosicion()
{
    return m_posicion;
}

//------------------------------------------------------------------------
/** Indica si el estado es el objetivo.
*/
public boolean esObjetivo()
{
    return m_posicion.equals(m_mapa.getObjetivo());
}

//------------------------------------------------------------------------
/** Obtiene el camino recorrido hasta el estado.
*/
public LinkedList<Posicion> getCamino()
{
    LinkedList<Posicion> camino = new LinkedList<Posicion>();
    getCamino(camino);
    return camino;
}

//------------------------------------------------------------------------
private void getCamino(LinkedList<Posicion> camino)
{
    if(m_padre != null)
        m_padre.getCamino(camino);

    camino.add(m_posicion);
}

//------------------------------------------------------------------------
/** Guarda el camino recorrido en un fichero de texto.
@param fichero Fichero donde guardar el camino.
*/
public void guardarCamino(String fichero) throws IOException
{
    BufferedWriter out = new BufferedWriter(new FileWriter(fichero));
    guardarCamino(out);
    out.close();
}

//------------------------------------------------------------------------
private void guardarCamino(BufferedWriter out) throws IOException
{
    if(m_padre != null)
        m_padre.guardarCamino(out);

    out.write(m_posicion.getFila() +","+ m_posicion.getColumna() +"\n");
}

//------------------------------------------------------------------------
/** Calcula el código hash de este estado para poderlo usar como clave
en clases como HashMap.
@return Código hash de este estado.
*/
@Override public int hashCode()
{
    return m_posicion.hashCode();
}

//------------------------------------------------------------------------
/** Comprueba si este estado es igual a otro.
@param obj Estado a comparar.
@return <code>true</code> si los estados son iguales.
*/
@Override public boolean equals(Object obj)
{
    return obj == null || !(obj instanceof Estado)
           ? false : compareTo((Estado)obj) == 0;
}

//------------------------------------------------------------------------
/** Compara este estado con otro.
*/
@Override public int compareTo(Estado e)
{
    int r = m_posicion.compareTo(e.m_posicion);

    if(r != 0)
        return r; //................................................RETURN

    if(Math.abs(m_gasolina - e.m_gasolina) < 1e-6)
        return 0; //................................................RETURN

    return m_gasolina < e.m_gasolina ? -1 : 1;
}

} // Estado
