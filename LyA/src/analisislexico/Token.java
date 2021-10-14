
package analisislexico;

import java.util.*;

public class Token {    
    public String lexema;
    public String clasificacion;
    public int atributo;  
        // 800-806 palabras reservadas
        // 1001 identificadores
        // 333 enteros
        // 334 Flotantes
        // ASCII caracteres especiales
    public int tipo;
    public int prioridad=0;
    public String tipoString;
        // 1  Función
        // 2  Inicial
        // 3  Tipo de dato
        // 4  Real
        // 5  Entero
        // 6  Coma
        // 7  Punto y coma
        // 8  Paréntesis que abre
        // 9  Paréntesis que cierra
        // 10 Asignación
        // 11 Suma
        // 12 Resta
        // 13 Multiplicación
        // 14 División
        // 15 Identificador
        // 16 Final
    
    public Token(Token tok){
        lexema = tok.lexema;
        clasificacion = tok.clasificacion;
        atributo = tok.atributo;  
        tipo = tok.tipo;
        tipoString = tok.tipoString;
    }
    
    public Token(HashMap<String, Integer> mapa, String lexema, String t){
        clasificacion = "Identificador";
        tipoString = t;
        tipo = 15;
        this.lexema = lexema;
        if(mapa.containsKey(lexema))
            atributo= (int)mapa.get(lexema);
        else if(mapa.isEmpty())
            atributo = 1001;
        else
            atributo = mapa.size() + 1001;
    }
    
    //Crea un Token según su atributo
    public Token(String lexema, int atributo, int tipo){
        this.atributo = atributo;
        this.lexema = lexema;
        this.tipo = tipo;
        switch(atributo){
            case 333: clasificacion= tipoString ="Entero"; break;
            case 334: clasificacion= tipoString="Real"; break;
        }
    }
    
    //Crea Token a partir del lexema de palabra reservada
    public Token(String lexema){
        Lenguaje l = new Lenguaje();
        clasificacion = "Palabra reservada";
        this.lexema = lexema;
        for (int i = 0; i < l.reservadas.length; i++) {
            if(l.reservadas[i].equals(lexema)){
                atributo = 800+i;
                switch(i){
                    case 0: tipo = 1; tipoString="Función"; break;
                    case 1: tipo = 3; tipoString="Tipo de dato"; break;
                    case 2: tipo = 3; tipoString="Tipo de dato"; break;
                    case 3: tipo = 1; tipoString="Función"; break;
                    case 4: tipo = 1; tipoString="Función"; break;
                    case 5: tipo = 2; tipoString="Inicial"; break;
                    case 6: tipo =16; tipoString="Final"; break;
                }
                break;
            }                
        }
    }
    
    // LOS CARACTERES SIMPLES
    public Token(String lexema, int atributo){
        this.atributo = atributo;
        clasificacion = "Caracter simple";
        this.lexema = lexema;
        switch(atributo){
            case 44: tipo = 6; tipoString="Coma"; break;
            case 59: tipo = 7; tipoString="Punto y coma"; break;
            case 40: tipo = 8; tipoString="P que abre"; 
            prioridad = 3; break;
            case 41: tipo = 9; tipoString="P que cierra"; break;
            case 61: tipo =10; tipoString="Igual"; break;
            case 43: tipo =11; tipoString="Op suma"; 
            prioridad = 1; break;
            case 45: tipo =12; tipoString="Op resta"; 
            prioridad = 1; break;
            case 42: tipo =13; tipoString="Op multiplicación"; 
            prioridad = 2; break;
            case 47: tipo =14; tipoString="Op división"; 
            prioridad = 2; break;
        }
    }
    
    //Para impresión
    @Override
    public String toString(){
        return lexema+"\t\t"+clasificacion+"\t\t"
                +tipoString+"\t\t"+atributo+"\n";
    }
}