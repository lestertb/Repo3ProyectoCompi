package BackJava.BackJava.AC;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.LinkedList;

public class TablaSimbolosClass {

    private static TablaSimbolosClass inst = null;

    LinkedList<Object> tablaClass;

    public int nivelActual = 0;

    public String nombreClass;

    public TablaSimbolosClass() {
        tablaClass = new LinkedList<Object>();
    }

    public static TablaSimbolosClass getInstance(){
        if(inst == null){
            inst = new TablaSimbolosClass();
        }
        return inst;
    }

    public void insertar(Token id, Type tipo, ParserRuleContext decl)
    {
        //no se puede insertar un elemento repetido en el mismo nivel
        Ident i = new Ident(id,tipo,nivelActual,decl, null);
        tablaClass.addFirst(i);
    }

    public Ident buscar(String nombre)
    {
        Ident temp = null;
        try {
            for(Object id : tablaClass)
                if (((Ident)id).tok.getText().equals(nombre))
                    return ((Ident)id);
        } catch (Exception ignored){ }
        return temp;
    }

    public void openScope(){
        nivelActual++;
    }

    public void closeScope(){
        try{
            tablaClass.removeIf(n -> (((Ident)n).nivel == nivelActual));
            nivelActual--;
        }catch (Exception ignored){ }
    }

    //Aquí intentar devolver las variables que se crearon segun el nombre de la clase para asignarlas a una variable
    //Y poder accederla tipo var.x
    public void obtenerListaVariables(){

    }

    public void imprimir() {
        System.out.println("----- INICIO TABLA CLASS: "+ nombreClass + "------");
        for (int i = 0; i < tablaClass.size(); i++) {
            Token s = (Token) ((Ident) tablaClass.get(i)).tok;
            System.out.println("Nombre: " + s.getText() + " - " +"Nivel: " + ((Ident) tablaClass.get(i)).nivel + " - " +" Tipo: "+((Ident) tablaClass.get(i)).type);
        }
        System.out.println("----- FIN TABLA CLASS: "+ nombreClass + "------\n");
    }

}