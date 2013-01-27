package lib;

import java.util.*;

//************************************************************************
public class ColaPrioridadTest
{
//------------------------------------------------------------------------
public static void main(String args[])
{
    Comparator<Nodo> cmp = new Comparator<Nodo>()
    {@Override public int compare(Nodo o1, Nodo o2)
    {
        return o1.m_coste - o2.m_coste;
    }};

    ColaPrioridad<Nodo> q = new ColaPrioridad<Nodo>(cmp);
    q.add(new Nodo("tres", 3));
    q.add(new Nodo("dos",  2));
    q.add(new Nodo("uno",  1));
    System.out.println("Cola original");

    for(Nodo n : q)
        System.out.println(n);

    Nodo n1 = new Nodo("tres", 0),
         n2 = q.get(n1);

    q.remove(n2);
    q.add(n1);
    System.out.println("\nCola final");

    for(Nodo n : q)
        System.out.println(n);
}

//************************************************************************
private static class Nodo implements Comparable<Nodo>
{
private String m_estado;
private int m_coste;

//------------------------------------------------------------------------
public Nodo(String estado, int coste)
{
    m_estado = estado;
    m_coste  = coste;
}

//------------------------------------------------------------------------
@Override public int compareTo(Nodo o)
{
    return m_estado.compareTo(o.m_estado);
}

//------------------------------------------------------------------------
@Override public boolean equals(Object obj)
{
    if(obj == null || getClass() != obj.getClass())
        return false; //............................................RETURN
    else
        return m_estado.equals(((Nodo)obj).m_estado);
}

//------------------------------------------------------------------------
@Override public int hashCode()
{
    return m_estado.hashCode();
}

//------------------------------------------------------------------------
@Override public String toString()
{
    return m_estado +": "+ m_coste;
}

} // Nodo

} // ColaPrioridadTest
