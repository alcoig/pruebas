package mapa;

//************************************************************************
public class Diagonal extends Heuristica
{
//------------------------------------------------------------------------
@Override public double getH(Estado e)
{
    Posicion a = e.getPosicion(),
             b = e.getMapa().getObjetivo();

    int f = Math.abs(a.getFila() - b.getFila()),
        c = Math.abs(a.getColumna() - b.getColumna()),
        d = Math.min(f, c),
        r = Math.max(f-d, c-d);

    return 1.4142 * d + r;
}

} // Diagonales
