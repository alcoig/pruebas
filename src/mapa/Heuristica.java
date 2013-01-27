package mapa;

import java.util.*;

//************************************************************************
/** Interfaz para implementar heurísticas.
 */
public abstract class Heuristica implements Comparator<Estado>
{
//------------------------------------------------------------------------
/** Compara dos estados según su heurística.
@param a Primer estado a comparar.
@param b Segundo estado a comparar.
@return Un número negativo, cero, o positivo si el primer estado
        tiene una heurística menor, igual o mayor que el segundo.
*/
@Override public int compare(Estado a, Estado b)
{
    double h = getH(a) - getH(b),
           f = getG(a) - getG(b) + h;

    if(Math.abs(f) < 1e-6)
    {
        if(Math.abs(h) < 1e-6)
            return 0;
        else
            return h < 0 ? -1 : 1;
    }
    else
    {
        return f < 0 ? -1 : 1;
    }
}

//------------------------------------------------------------------------
/** Calcula la evaluación de un estado.
*/
public double getF(Estado e)
{
    return getG(e) + getH(e);
}

//------------------------------------------------------------------------
/** Devuelve la distancia recorrida hasta el estado.
*/
public double getG(Estado e)
{
    return e.getDistancia();
}

//------------------------------------------------------------------------
/** Calcula la distancia estimada hasta la solución.
*/
public abstract double getH(Estado e);

} // Heuristica
