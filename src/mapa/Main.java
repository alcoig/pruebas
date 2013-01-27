package mapa;

import java.io.IOException;

import lib.ColaPrioridad;
import lib.Conjunto;
import lib.Operador;

//************************************************************************
/** Clase ejecutable para la b√∫squeda en mapas. */
public class Main {

	private static int s_nodosExplorados;
	private static long s_milis;
	private static int sizeMax;

	/**
	 * Implementamos A*
	 * 
	 * @params estado inicial, heuristica y tipo 1/Lineas Rectas 2 todos
	 **/
	private static Estado busquedaHeuristica(Estado ini, final Heuristica f) {
		s_nodosExplorados = 0;
		s_milis = System.currentTimeMillis();
		sizeMax = 0;
		Conjunto<Estado> repetidos = new Conjunto<Estado>();
		ColaPrioridad<Estado> abiertos = new ColaPrioridad<Estado>(f);
		abiertos.add(ini);
		repetidos.add(ini);
		while (!abiertos.isEmpty()) {
			Estado actual = abiertos.poll();
			if (actual.esObjetivo())
				return actual;
			for (Operador o : Operadores.values()) {
				Estado e = (Estado) o.run(actual);
				if (e != null && repetidos.get(e) == null) {
					abiertos.add(e);
					repetidos.add(e);
				} else if (repetidos.get(e) != null) {
					// tratamiento de nodos repetidos
					Estado repetido = repetidos.get(e);
					if (f.compare(e, repetido) < 0) {
						repetidos.remove(repetido);
						repetidos.add(e);
						abiertos.remove(repetido);
						abiertos.add(e);
					}
				}
				if (e != null) {
					if (e.esObjetivo())
						break;
					s_nodosExplorados++;
					if (sizeMax < abiertos.size()) {
						sizeMax = abiertos.size();
					}
				}
			}
		}

		return null;
	}


	// ------------------------------------------------------------------------
	public static void main(String args[]) throws IOException {
		if (args.length != 6) {
			System.out.println("Parametros:  ficheroMapa capacidadDeposito "
					+ "filaInicio columnaInicio filaFin columnaFin");
			System.exit(0);
		}

		int ip = 0;
		String fichMapa = args[ip++];
		double deposito = Double.parseDouble(args[ip++]);
		int filaIni = Integer.parseInt(args[ip++]), colIni = Integer
				.parseInt(args[ip++]), filaFin = Integer.parseInt(args[ip++]), colFin = Integer
				.parseInt(args[ip++]);

		Posicion ini = new Posicion(filaIni, colIni), fin = new Posicion(
				filaFin, colFin);

		Mapa mapa = new Mapa(fichMapa, deposito, ini, fin);
		
		Estado estadoIni = new Estado(mapa);
		
		//Estado estadoFin = busquedaHeuristica(estadoIni, new LineaRecta());
		
		Estado estadoFin = busquedaHeuristica(estadoIni, new LineaRecta());

		if (estadoFin == null)
			System.out.println("No se ha encontrado el camino.");
		else
			mapa.setCamino(estadoFin.getCamino());
		long milis = System.currentTimeMillis() - s_milis;
		
		//imprimimos estadísticas
		System.out.println("Nodos explorados: -->" + s_nodosExplorados);
		System.out.println("Milisegundos: -->" + milis);
		System.out.println("Tamaño máximo lista abiertos -->" + sizeMax);
		mapa.mostrar();
	}

} // Main
