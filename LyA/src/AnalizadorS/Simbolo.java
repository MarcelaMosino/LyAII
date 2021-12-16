package AnalizadorS;

import analisislexico.*;
import java.util.*;

public class Simbolo extends Token{
    //Lexema\t\tToken\t\tTipo\t\tValor\t\tRepeticiones\tLineas
    Lista<Integer> lineas = new Lista("Renglones");
    int repeticiones=0;  
    public Object tipo;

    public Simbolo(Token tok) {
        super(tok);
        if(tok.tipo==4 && !tok.clasificacion.equals("Identificador"))
            tipo = Float.parseFloat(tok.lexema);
        else if(tok.tipo==5 && !tok.clasificacion.equals("Identificador"))
            tipo = Integer.parseInt(tok.lexema);
        else
            tipo= super.atributo;
    }
    
    public void addRenglon(int renglon){
        repeticiones++;
        lineas.add(renglon);
    }
    
    @Override
    public String toString(){
        return super.lexema+"\t\t"+super.clasificacion+"\t\t"
                +super.tipoString+"\t\t"+tipo+"\t\t"
                +repeticiones+"\t"+lineas.aRenglon();
    }
}
