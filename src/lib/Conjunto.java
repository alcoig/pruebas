package lib;

import java.util.*;

//************************************************************************
public class Conjunto<T> implements Iterable<T>
{
private HashMap<T,T> m_map = new HashMap<T,T>();

//------------------------------------------------------------------------
/** Añade un estado al conjunto. El estado no debe existir.
@param e Estado que se añade al conjunto.
*/
public void add(T e)
{
    if(m_map.put(e, e) != null)
        throw new AssertionError();
}

//------------------------------------------------------------------------
/** Obtiene un estado idéntico si existe.
@param e Estado con el que se buscará un estado idéntico.
@return Estado idéntico si existe o null si no existe.
*/
public T get(T e)
{
    return m_map.get(e);
}

//------------------------------------------------------------------------
/** Elimina un estado del conjunto. El estado debe existir.
@param e Estado a eliminar.
*/
public void remove(T e)
{
    if(m_map.remove(e) != e)
        throw new AssertionError();
}

//------------------------------------------------------------------------
/** Indica si el conjunto está vacío.
@return True si el conjunto está vacío.
*/
public boolean isEmpty()
{
    return m_map.isEmpty();
}

//------------------------------------------------------------------------
/** Obtiene el tamaño de la cola.
@return Tamaño de la cola.
*/
public int size()
{
    return m_map.size();
}

//------------------------------------------------------------------------
@Override public Iterator<T> iterator()
{
    return m_map.keySet().iterator();
}

} // Conujunto
