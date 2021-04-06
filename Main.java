import java.io.*;

/**
 * Clase principal
 * @author 
 */
public class Main
{
    private final static String PROMPT_CHARS = ">>> ";
    private final static String ERROR_CHARS="**ERROR** ";
    public static void main(String args[])
    {
        BufferedInputStream standIn = new BufferedInputStream(System.in);

        //Nuestros objetos, la lista de variables, de funciones, y jamie, el intérprete
        ListaDefinicion dList = new ListaDefinicion();
        ListaAsociacion aList = new ListaAsociacion();
        Interprete jamie = new Interprete();
        boolean more = true;
        while (more)  //while there is more input to read
        {
            try
            {
                System.out.print(PROMPT_CHARS);

                SExp input = Lambdi.INPUT(standIn);
                if (input == null)
                {
                    more = false; //no more to read
                } else
                {
                    SExp output = jamie.EVAL(input, aList, dList, true);

                    //all output is in list notation
                    System.out.println(output.ToStringDotNotation());
                }

            } catch (Exception e)
            {
                //all errors are in the form of exceptions that
                //are synthesized up to this level to be printed
                System.out.println(ERROR_CHARS + e.getMessage());
            }
        }
    }
}

