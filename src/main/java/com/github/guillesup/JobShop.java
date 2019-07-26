package com.github.guillesup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JobShop {

    private static int pos;

    public static void main(String[] args) {

        Scanner archivo = null;

        try {

              /*archivo = new Scanner(new File("prueba.txt"));
            FileReader r = new FileReader("prueba.txt");*/


            archivo = new Scanner(new File("abz6-1.3.txt"));
            FileReader r = new FileReader("abz6-1.3.txt");


            BufferedReader gr = new BufferedReader(r);
            String firstInformation = gr.readLine();

            // la variable determina el numero de maquinas la variable numero1 determina el numero de pedidos
            String[] arrayFirstInformation = firstInformation.split(" ");
            int numeroPedidos = Integer.parseInt(arrayFirstInformation[0]);

            // la variable determina el numero de maquinas la variable numero2 determina el numero de Maquinas
            int numeroMaquinas = Integer.parseInt(arrayFirstInformation[1]);

            // la variable determina el numero de maquinas la variable numero3 determina el numero de operaciones
            int numeroOperaciones = Integer.parseInt(arrayFirstInformation[2]);

            //arreglos que contienen los pesos y los dudate de los diez pedidos o numeroPedidos
            double[] pesos = new double[numeroPedidos];
            //primera, segunda, tercera.... machine por pedidos.

            int[] dudate = new int[numeroPedidos];
            int[] dudateAux = new int[numeroPedidos];
            int[][] matrizTiempos = new int[numeroPedidos][numeroPedidos];
            int[][] matrizMaquinas = new int[numeroPedidos][numeroPedidos];

            for (int i = 0; i < numeroPedidos; ++i) {

                //se llama la informacion en el archivo y se organiza en una arreglo que contiene una porcion
                String information = gr.readLine();

                String[] linea = information.split(" ");
                dudate[i] = Integer.parseInt(linea[1]);
                pesos[i] = Double.parseDouble(linea[2]);
                int operationpedido = Integer.parseInt(linea[3]);
                int numeroM = 0;
                int numeroT = 0;
                for (int j = 4; j < (numeroPedidos * 2 + 4); j++) {

                    if (j % 2 == 0) {
                        matrizMaquinas[i][numeroM] = Integer.parseInt(linea[j]);
                        numeroM++;
                    } else {
                        matrizTiempos[i][numeroT] = Integer.parseInt(linea[j]);
                        numeroT++;
                    }

                }
                dudateAux[i] = Integer.parseInt(linea[1]);
            }
            System.out.println();
            //ordenamiento de los pedidos

            int[] ord = new int[numeroPedidos];
            int post;

            for (int n = 0; n < numeroPedidos; n++) {
                int menor = dudateAux[n];
                post = n;
                for (int k = 0; k < numeroPedidos; k++) {
                    if (pesos[n] > pesos[k]) {

                        break;
                    } else {

                        if (pesos[n] == pesos[k]) {

                            if (dudateAux[k] < menor) {

                                menor = dudateAux[k];
                                post = k;

                            }

                        }
                    }
                }
                ord[n] = post;
                dudateAux[post] = 1000000000;

            }

            for (int i = 0; i < ord.length; i++) {
                System.out.println("Posicion:" + " " + ord[i] + "\n");
            }

            for (int i = 0; i < ord.length; i++) {
                int j = ord[i];

            }
            // contenedora es donde se guardan los #identidad de los nodos.
            ArrayList<ArrayList> contenedora = new ArrayList<>();
            // for anidado donde se crea un arraylist de dos posiciones que al generar una #identidad lo ira guardando en el arraylist contenedora.
            for (int i = 0; i < numeroPedidos; i++) {
                for (int j = 0; j < numeroPedidos; j++) {
                    ArrayList<Integer> direccion = new ArrayList<>();
                    direccion.add(ord[j]);
                    direccion.add(i);
                    contenedora.add(direccion);
                }

            }

            System.out.println();
            //-------------------------- Crear rutas por maquinas-------------------------------
            //la ruta es una secuencia de #identidad a seguir.
            // se crea un arraylist llamado Maquinas que guardara las rutas por maquinas.
            ArrayList[] maquinas = new ArrayList[numeroMaquinas];
            // se crea un arraylist llamado rutasMaquina que guardara toda la ruta en general.
            for (int i = 0; i < numeroMaquinas; i++) {
                ArrayList<ArrayList> rutaMaquina = new ArrayList<>();
                for (int j = 0; j < contenedora.size(); j++) {
                    // direccion el cual contiene dos posiciones sera igual a algun #identidad guardado en el Arraylist contenedora.
                    ArrayList<Integer> direccion = contenedora.get(j);
                    if (matrizMaquinas[direccion.get(0)][direccion.get(1)] == (i + 1)) {
                        rutaMaquina.add(direccion);
                    }

                }
                maquinas[i] = rutaMaquina;
                System.out.println("" + maquinas[i]);
            }

            // -------------------------------- Empezar a recorrer el gráfico ----------------------------
            // se crean las matrices de los tiempos finales e iniciales.
            int[][] tiempoInicio = new int[numeroPedidos][numeroPedidos];
            int[][] tiempoFinal = new int[numeroPedidos][numeroPedidos];
            int[][] MatrizArcos = new int[numeroPedidos][numeroPedidos];
            ArrayList[][] MatrizColas = new ArrayList[numeroPedidos][numeroPedidos];


            for (int i = 0; i < contenedora.size(); i++) {


                ArrayList<Integer> direccion = contenedora.get(i);
                //tiempo de Procesamiento, Inicio y Fin.
                int tiempoI = 0;
                int tiempoP = 0, tiempoF = 0;
                //tiempo de
                int tiempoFPredecesorXM = 0;
                int tiempoFPredecesorXP = 0;
                int maquina = matrizMaquinas[direccion.get(0)][direccion.get(1)];
                int Arcopredecesor = 0;
                int NArco = 0;

                // Buscar el antecesor por ruta de máquinas
                for (int j = 0; j < maquinas[maquina - 1].size(); j++) {
                    if (maquinas[maquina - 1].get(j).equals(direccion) && j > 0) {
                        ArrayList<Integer> direccionA = (ArrayList<Integer>) maquinas[maquina - 1].get(j - 1);
                        tiempoFPredecesorXM = tiempoFinal[direccionA.get(0)][direccionA.get(1)];
                        Arcopredecesor = MatrizArcos[direccionA.get(0)][direccionA.get(1)];

                        System.out.println();
                    }

                }
                if (direccion.get(1) != 0) {
                    tiempoFPredecesorXP = tiempoFinal[direccion.get(0)][direccion.get(1) - 1];
                    Arcopredecesor = MatrizArcos[direccion.get(0)][direccion.get(1) - 1];
                }
                // Hora dde inicio
                tiempoI = Math.max(tiempoFPredecesorXM, tiempoFPredecesorXP);
                // Tiempo de procesamiento
                tiempoP = matrizTiempos[direccion.get(0)][direccion.get(1)];
                // tiempo Final
                tiempoF = tiempoI + tiempoP;
                // guardar el tiempo final en la matriz
                tiempoFinal[direccion.get(0)][direccion.get(1)] = tiempoF;
                // Guardar tiempo de inicio
                tiempoInicio[direccion.get(0)][direccion.get(1)] = tiempoI;

                NArco = Arcopredecesor + 1;

                MatrizArcos[direccion.get(0)][direccion.get(1)] = NArco;

                System.out.println(direccion + "--Máquina--" + maquina + "-- tiempo de inicio--" + tiempoInicio[direccion.get(0)][direccion.get(1)] + "--tiempoFinal--" + tiempoFinal[direccion.get(0)][direccion.get(1)] + "--Arcos--" + MatrizArcos[direccion.get(0)][direccion.get(1)]);
            }

            int[] tiempoFinalizacionPedido = new int[numeroPedidos];
            int[] Tardanza = new int[numeroPedidos];
            for (int i = 0; i < tiempoFinalizacionPedido.length; i++) {
                tiempoFinalizacionPedido[i] = tiempoFinal[i][numeroPedidos - 1];
                Tardanza[i] = Math.max(0, tiempoFinalizacionPedido[i] - dudate[i]);

                System.out.println("Job: " + i + "--Tiempo finalización -- " + tiempoFinalizacionPedido[i] + " Tardanza:" + Tardanza[i]);


            }

            //aqui empieza la prueba
            ArrayList[] rutaCritica = new ArrayList[numeroMaquinas];
            //ES ESTE FOR,CUANDO SUCEDE ESTO (direccion.get(1)== numeroPedidos-1) NECESITO QUE CORRA NORMAL LO QUE CONTIENE EL FOR, PERO APENAS ENCUENTRA
            //UN DIRECCIONA, QUIERO QUE ESTA SE VUELVA DIRECCION PARA ENTRAR A TODOS LAS CONDICIONES Y LLENAR LAS RUTAS POR PEDIDOS.
            for (int i = numeroPedidos - 1; i >= 0; i--) {
                ArrayList<ArrayList> rutaPedido = new ArrayList<>();


                for (int j = contenedora.size() - 1; j >= 0; j--) {

                    ArrayList<Integer> direccion = contenedora.get(j);

                    if (direccion.get(1) == numeroPedidos - 1 && i == direccion.get(0)) {

                        rutaPedido.add(direccion);
                    }

                    int maquina = matrizMaquinas[direccion.get(0)][direccion.get(1)];
                    ArrayList<Integer> direccionAUX = new ArrayList<>();


                    for (int m = 0; m < maquinas[maquina - 1].size(); m++) {

                        if (maquinas[maquina - 1].get(m).equals(direccion) && m > 0) {
                            ArrayList<Integer> direccionA = (ArrayList<Integer>) maquinas[maquina - 1].get(m - 1);

                            int timeID = tiempoInicio[direccion.get(0)][direccion.get(1)];
                            int timeFA = tiempoFinal[direccionA.get(0)][direccionA.get(1)];
                            if (timeID == timeFA && i == direccion.get(0)) {
                                rutaPedido.add(direccionA);
                            }
                            direccionAUX = direccionA;

                        }
                        if (i == direccion.get(0)) {

                            int direccionA0 = direccion.get(0);
                            int direccionA1 = direccion.get(1) - 1;
                            int timeID = tiempoInicio[direccion.get(0)][direccion.get(1)];
                            int timeFA = tiempoFinal[direccionA0][direccionA1];

                            if (timeID == timeFA) {
                                direccionAUX.add(0, direccionA0);
                                direccionAUX.add(1, direccionA1);

                                rutaPedido.add(direccionAUX);

                            }
                        }
                    }
                }
                maquinas[i] = rutaPedido;
                for (int j = 0; j < numeroPedidos; j++) {

                    System.out.println();
                }
            }

            //aqui acaba la prueba

            for (int i = contenedora.size() - 1; i >= 0; i--) {
                ArrayList colas = new ArrayList();

                for (int j = 0; j < numeroPedidos; j++) {
                    colas.add(-1);
                }

                ArrayList<Integer> direccion = contenedora.get(i);
                if (direccion.get(1) == numeroPedidos - 1) {
                    int pos = direccion.get(0);

                    colas.set(pos, 0);


                } else {
                    int maquina = matrizMaquinas[direccion.get(0)][direccion.get(1)];

                    //se encarga de sacar  las colas del predecesor por maquinas del nodo actual.

                    for (int j = 0; j < maquinas[maquina - 1].size(); j++) {

                        if (maquinas[maquina - 1].get(j).equals(direccion) && j < numeroPedidos - 1) {
                            ArrayList<Integer> direccionA = (ArrayList<Integer>) maquinas[maquina - 1].get(j + 1);

                            int valorAnteriorcola = matrizTiempos[direccionA.get(0)][direccionA.get(1)];
                            ArrayList colasAnterior = MatrizColas[direccionA.get(0)][direccionA.get(1)];

                            for (int k = 0; k < colasAnterior.size(); k++) {
                                int Ncola = (int) colasAnterior.get(k);

                                if (Ncola != -1) {
                                    int valorcola = Ncola + valorAnteriorcola;

                                    if ((int) colas.get(k) < valorcola) {
                                        colas.set(k, valorcola);
                                    }

                                }

                            }

                        }

                        //sacar las colas respecto al predecesor por linea de pedido del nodo actual
                        int direccionA0 = 0;
                        int direccionA1 = 0;
                        int valorAnteriorcola = 0;
                        int tiempoIni2 = 0;

                        if (direccion.get(1) < numeroPedidos - 1) {

                            direccionA0 = direccion.get(0);
                            direccionA1 = direccion.get(1) + 1;
                            valorAnteriorcola = matrizTiempos[direccionA0][direccionA1];
                            tiempoIni2 = tiempoInicio[direccionA0][direccionA1];


                            ArrayList colasAnterior = MatrizColas[direccionA0][direccionA1];

                            for (int k = 0; k < colasAnterior.size(); k++) {
                                int Ncola = (int) colasAnterior.get(k);

                                if (Ncola != -1) {
                                    int valorcola = Ncola + valorAnteriorcola;

                                    if ((int) colas.get(k) < valorcola) {
                                        colas.set(k, valorcola);
                                    }

                                }
                            }

                        }

                    }
                }

                MatrizColas[direccion.get(0)][direccion.get(1)] = colas;
                System.out.println(direccion.get(0) + " - " + direccion.get(1) + " --colas--" + MatrizColas[direccion.get(0)][direccion.get(1)]);

            }


            System.out.println();
        } catch (IOException e) {
            System.out.println("ARCHIVO NO ENCONTRADO");
        }

    }

}