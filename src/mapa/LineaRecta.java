package mapa;

//************************************************************************
/** Heurística basada en la línea recta entre dos posiciones.
*/
public class LineaRecta extends Heuristica
{
//------------------------------------------------------------------------
/** Calcula la distancia estimada hasta la solución.
*/
@Override public double getH(Estado e)
{
    return e.getPosicion().distancia(e.getMapa().getObjetivo());
}

} // LineaRecta
