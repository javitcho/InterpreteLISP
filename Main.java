
import java.io.*;

/**
 * Clase principal
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */
public class Main {

    //Estos son para que se mire bien la salida
    private final static String PROMPT_CHARS = ">>> ";
    private final static String ERROR_CHARS = "**ERROR** ";

    public static void main(String args[]) {
        BufferedInputStream standIn = new BufferedInputStream(System.in);

        //Nuestros objetos, la lista de variables, de funciones, y jamie, el intérprete
        ListaDefinicion dList = new ListaDefinicion();
        ListaAsociacion aList = new ListaAsociacion();
        Interprete jamie = new Interprete();
        boolean more = true;
        while (more) //Sigue leyendo mientras haya algo que leer
        {
            try {
                System.out.print(PROMPT_CHARS);

                SExp input = Lambdi.INPUT(standIn);
                //Imprimir la notación para reconocer los elementos de la expresión S es útil para rastrear el funcionamiento del programa
                //System.out.println(input.ToStringDotNotation());
                if (input == null) {
                    more = false; //Cuando se acaba el input ya no hay nada que leer
                } else {
                    SExp output = jamie.EVAL(input, aList, dList, true);

                    //La salida se da en notación dot (car.cdr)
                    System.out.println(output.ToStringDotNotation());
                }

            } catch (Exception e) {
                //Los errores suben hasta aquí y aquí se imprime el mensaje.
                System.out.println(ERROR_CHARS + e.getMessage());
            }
        }
    }
}
