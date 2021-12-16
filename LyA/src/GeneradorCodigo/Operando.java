package GeneradorCodigo;

import analisislexico.Token;

public class Operando {
    int tipo;
    String lexema;
    
    public Operando(Token tok){
        this.tipo = getTipo(tok);
        this.lexema = tok.lexema;
    }
    
    public Operando(String lexema, int tipo){
        if(tipo==4) this.tipo = 0;
        else        this.tipo = 1;
        this.lexema = lexema;
    }
    
    public int getTipo(Token tok){
        try{
            Double.parseDouble(tok.lexema);
            if(tok.tipo==4) return 2;
            else            return 3;
        }catch(NumberFormatException e){
            if(tok.tipo==4) return 0;
            else            return 1;
        }
    }
    
    public String getDeclaracion(){
        String ret="no se";
        switch(tipo){
            case 0: ret = "ldvr "+lexema; break;
            case 1: ret = "ldve "+lexema; break;
            case 2: ret = "ldcr "+lexema; break;
            case 3: ret = "ldce "+lexema; break;
        }return ret;
    }
}
