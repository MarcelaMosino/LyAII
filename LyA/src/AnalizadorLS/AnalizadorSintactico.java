

package AnalizadorLS;

import analisislexico.AnalizadorLexico;
import analisislexico.Lista;
import analisislexico.Token;
import gramatica.Gramatica;
import java.io.IOException;
import java.util.Scanner;
import pila.Nodo;
import pila.Pila;


public class AnalizadorSintactico {
    static String impresion = "Corrida de Analizador Léxico Sintáctico";
    public AnalizadorLexico al; 
    Archivo salida;
    Gramatica gr; 
    Pila<Integer> pila = new Pila(); //Pila para análisis Léxico-Sintáctico
    int[][] predict ={ //Matriz predictiva
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 3, 0, 3, 0, 0, 3, 0, 0, 0, 3, 3, 0, 0, 0},
        {0, 0, 5, 0, 4, 0, 4, 0, 0, 4, 0, 0, 0, 4, 4, 0, 0, 0},
        {0, 0, 0, 0, 7, 0, 8, 0, 0, 9, 0, 0, 0, 6, 6, 0, 0, 0},
        {0, 0, 0, 0,10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0,12, 0, 0, 0, 0,12, 0,11, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0,13, 0, 0, 0, 0, 0, 0,13,13, 0, 0, 0, 0, 0},
        {0, 0, 0,15, 0, 0, 0, 0,15, 0, 0, 0, 0, 0, 0,14,14,14},
        {0, 0, 0, 0,17, 0, 0,16, 0, 0, 0,18,19, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,20,21, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,22,23,24}};
    
    public AnalizadorSintactico(String nomArchivo, String s) throws IOException{ 
        al = new AnalizadorLexico(nomArchivo); 
        salida = new Archivo(s);
        gr = new Gramatica("gramatica1.txt");
        gr.leeGramatica(); //Almacena producciones, terminales y no terminales
        //en las estructuras para el análisis
    }
    
    public void analizar() throws IOException{
        Lista<Integer> pPro; //Auxiliar para recorrer la producción
        Nodo<Integer> auxImpresion; //Auxiliar para recorrer la producción
        int x,a; //X se obtiene de la pila A del analizador léxico
        
        pila.push(gr.getSInicial());//Apila el simbolo inicial de la gramática
        Token tokAn =al.nuevoToken();//Obtiene token del analizador léxico
        a = tokAn.atributo;//Usamos el atributo para comparaciones
        impresion+=("\n\nLee de archivo: "+tokAn.lexema);
        
        while(!pila.isEmpty()){
            x = pila.peek(); 
            impresion+=("\nTope pila: "+pila.peek());
            impresion+=("\nX "+x+"   A: "+a);
            
            //Si el tope de pila es no terminal
            if(x>899 && x<1000){//Los no terminales corresponden a 900+
                    impresion+=("\nMatriz devuelve: "+(matriz(x,a)-1));
                if(matriz(x,a)!=0){//Si hay producción en la matriz
                    pPro = gr.producciones[matriz(x,a)-1];
                    pila.pop();
                    auxImpresion = pPro.primero;
                    while(auxImpresion!=null){//Se apila la prod al revés
                        if(auxImpresion.contenido!=0){
                            pila.push(auxImpresion.contenido);
                            impresion+=("\nEntra a pila "+auxImpresion.contenido);
                        }auxImpresion = auxImpresion.siguiente;
                    }
                }else
                    error(); //matriz devuelve 0
            }else{
                //Si es un terminal
                if(x==a){//El tope de pila y la lectura son iguales
                    impresion+="\nSaca "+pila.pop()+" de la pila";
                    if(al.entrada.hasNext())
                        tokAn = al.nuevoToken();//Se lee nuevo token
                    a = tokAn.atributo;//Usamos el atributo
                    if(a>=1000) a= 1001;
                    impresion+=("\n\nLee de archivo "+tokAn.lexema);;
                }else
                    error(); //Se esperaba otro valor
            }
        }if(pila.isEmpty() && !al.entrada.hasNext())
            impresion+= "\n\nSin errores léxicos o sintácticos";
        salida.escribir(impresion+"\n\n"+al);
    }
    
    public int matriz(int x, int a)throws IOException{
        x -= 900;
        a = gr.buscaEntero(a);//Busca el índice del terminal
        return predict[x][a];//Regresa el numero de producción
    }
    
    public void error() throws IOException{
        //Al encontrar errores, Imprime error, la pila y cierra el flujo
        System.out.println("Error Sintáctico");
        System.out.println("Pila"+pila);
        al.entrada.close();
        System.exit(0);
    }    
}
