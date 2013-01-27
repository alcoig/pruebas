package mapa;

import lib.*;

//************************************************************************
public enum OperadoresRectos implements Operador<Estado>
{
//------------------------------------------------------------------------
norte {@Override public Estado run(Estado e)
{
    Posicion p = e.getPosicion();
    return newEstado(e, p.getFila() - 1, p.getColumna());
}},


//------------------------------------------------------------------------
oeste {@Override public Estado run(Estado e)
{
    Posicion p = e.getPosicion();
    return newEstado(e, p.getFila(), p.getColumna() - 1);
}},


//------------------------------------------------------------------------
sur {@Override public Estado run(Estado e)
{
    Posicion p = e.getPosicion();
    return newEstado(e, p.getFila() + 1, p.getColumna());
}},


//------------------------------------------------------------------------
este {@Override public Estado run(Estado e)
{
    Posicion p = e.getPosicion();
    return newEstado(e, p.getFila(), p.getColumna() + 1);
}},

;

//------------------------------------------------------------------------
private static Estado newEstado(Estado padre, int fila, int columna)
{
    if(!padre.getMapa().posicionValida(fila, columna))
        return null; //.............................................RETURN

    Posicion posicion = new Posicion(fila, columna);
    Estado estado = new Estado(padre, posicion);
    return estado.gasolinaSuficiente() ? estado : null;
}

} // Operadores
