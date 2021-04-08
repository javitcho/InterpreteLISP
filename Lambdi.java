
import java.io.IOException;
import java.io.InputStream;

/**
 * Esta clase realiza el cálculo lambda de las S expresiones, llamada Lambdi la
 * calculadora lambda. (Y la tokenizadora) Los métodos de esta clase son
 * estáticos para nunca instanciarla, y así evitar problemas al tokenizar la
 * entrada.
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */
public class Lambdi {

    //Esta variable facilita la tokenización (es una temporal que se accede de otras funciones)
    private static String pushedToken = null;

    /**
     * Las expresiones S se pueden construir usando la función CONS de LISP
     *
     * @param s1 el car
     * @param s2 el cdr
     * @return La S expresión construida
     */
    public static SExp CONS(SExp s1, SExp s2) {
        SExp s = new SExp();
        s.SetCAR(s1);
        s.SetCDR(s2);
        return s;
    }

    /**
     * La suma de enteros en LISP se maneja a nivel de expresiones S.
     *
     * @param s1
     * @param s2
     * @return La suma de s1 y s2 como expresión S
     * @throws java.lang.Exception
     */
    public static SExp PLUS(SExp s1, SExp s2) throws Exception {
        try {
            if (s1.IsInteger() && s2.IsInteger()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                return new SExp(String.valueOf(n1 + n2));
            } else if (s2.IsNull()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = 0;
                return new SExp(String.valueOf(n1 + n2));
            } else if (s1.IsInteger()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(PLUS(s2.CAR(), s2.CDR()).GetValue());
                return new SExp(String.valueOf(n1 + n2));
            }
            {
                throw new Exception("ERROR IN PLUS: Integers only");
            }
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN PLUS: Integers only");
        }
    }

    /**
     * La resta de enteros en LISP se maneja a nivel de expresiones S
     *
     * @param s1 minuendo
     * @param s2 sustraendo
     * @return La resta s1-s2
     * @throws java.lang.Exception
     */
    public static SExp MINUS(SExp s1, SExp s2)
            throws Exception {
        try {
            if (s1.IsAtomic() && s2.IsAtomic()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                return new SExp(String.valueOf(n1 - n2));
            } else {
                throw new Exception("ERROR IN MINUS: Integers only");
            }
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN MINUS: Integers only");
        }
    }

    /**
     * La multiplicación en LISP se maneja a nivel de expresiones S
     *
     * @param s1
     * @param s2
     * @return la multiplicación s1 x s2
     * @throws java.lang.Exception
     */
    public static SExp TIMES(SExp s1, SExp s2) throws Exception {
        try {
            if (s1.IsInteger() && s2.IsInteger()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                return new SExp(String.valueOf(n1 * n2));
            } else if (s2.IsNull()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = 1;
                return new SExp(String.valueOf(n1 * n2));
            } else if (s1.IsInteger()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(TIMES(s2.CAR(), s2.CDR()).GetValue());
                return new SExp(String.valueOf(n1 * n2));
            }
            {
                throw new Exception("ERROR IN TIMES: Integers only");
            }
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN TIMES: Integers only");
        }
    }

    /**
     * la división en LISP se maneja a nivel de expresiones S
     *
     * @param s1 dividendo
     * @param s2 divisor
     * @return la división (de enteros) s1/s2
     * @throws java.lang.Exception
     */
    public static SExp QUOTIENT(SExp s1, SExp s2) throws Exception {
        try {
            if (s1.IsAtomic() && s2.IsAtomic()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                return new SExp(String.valueOf(n1 / n2));
            } else {
                throw new Exception("ERROR IN QUOTIENT: Integers only");
            }
        } catch (ArithmeticException e) {
            throw new Exception("ERROR IN QUOTIENT: Division by zero");
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN QUOTIENT: Integers only");
        }
    }

    /**
     * los residuos en LISP también se maneja a nivel de expresiones S
     *
     * @param s1 dividendo
     * @param s2 divisor
     * @return s1 mod s2
     * @throws java.lang.Exception
     */
    public static SExp REMAINDER(SExp s1, SExp s2) throws Exception {
        try {
            if (s1.IsAtomic() && s2.IsAtomic()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                return new SExp(String.valueOf(n1 % n2));
            } else {
                throw new Exception("ERROR IN REMAINDER: Integers only");
            }
        } catch (ArithmeticException e) {
            throw new Exception("ERROR IN REMAINDER: Division by zero");
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN REMAINDER: Integers only");
        }
    }

    /**
     * Lambdi puede comparar expresiones S por la relación MAYOR QUE
     *
     * @param s1
     * @param s2
     * @return La expresión S mayor (en su valor entero)
     * @throws java.lang.Exception
     */
    public static SExp GREATER(SExp s1, SExp s2) throws Exception {
        try {
            if (s1.IsAtomic() && s2.IsAtomic()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                if (n1 > n2) {
                    return SExp.T;
                } else {
                    return SExp.NIL;
                }
            } else {
                throw new Exception("ERROR IN GREATER: Integers only");
            }
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN GREATER: Integers only");
        }
    }

    /**
     * Lambdi puede comparar expresiones S por la relación MENOR QUE
     *
     * @param s1
     * @param s2
     * @return La expresión S menor (en su valor entero)
     * @throws java.lang.Exception
     */
    public static SExp LESS(SExp s1, SExp s2)
            throws Exception {
        try {
            if (s1.IsAtomic() && s2.IsAtomic()) {
                int n1 = Integer.parseInt(s1.GetValue());
                int n2 = Integer.parseInt(s2.GetValue());
                if (n1 < n2) {
                    return SExp.T;
                } else {
                    return SExp.NIL;
                }
            } else {
                throw new Exception("ERROR IN LESS: Integers only");
            }
        } catch (NumberFormatException e) {
            throw new Exception("ERROR IN LESS: Integers only");
        }
    }

    /**
     * Compara por igualdad dos expresiones S
     *
     * @param s1
     * @param s2
     * @return True o False (en su forma de expresión S)
     * @throws java.lang.Exception
     */
    public static SExp EQ(SExp s1, SExp s2)
            throws Exception {
        if (!s1.IsAtomic() || !s2.IsAtomic()) //Eq only compares atoms
        {
            throw new Exception("ERROR IN EQ: Atoms only");
        } else {
            if (s1.GetValue().equalsIgnoreCase(s2.GetValue())) {
                return SExp.T;
            } else {
                return SExp.NIL;
            }
        }
    }

    /**
     * Esta es la función tokenizadora toma un inputstream y devuelve la
     * expresión S correspondiente.
     *
     * @param in el inputstream
     * @return devuelve la expresión S
     * @throws Exception
     * @throws IOException
     */
    public static SExp INPUT(InputStream in) throws Exception, IOException {
        SExp s;
        String token = NextToken(in);

        if (token.equalsIgnoreCase("")) {
            return null;
        }

        if (token.equalsIgnoreCase("(")) {
            String next = NextToken(in);

            // Los paréntesis vacíos () se toman como NIL (nulo o Falso)
            if (next.equalsIgnoreCase(")")) {
                return SExp.NIL;
            }

            PushedToken(next);

            s = new SExp();
            s.SetCAR(INPUT(in));

            token = NextToken(in);

            if (token.equals(".")) {
                s.SetCDR(INPUT(in));
                token = NextToken(in);
                //Si no hay cdr enviamos un error
                if (!token.equals(")")) {
                    throw new Exception("ERROR IN INPUT: Expected ')'");
                } else {
                    s.SetAtom(false);  //anotamos que no es atómica la expresión
                    s.SetValue("");   //Las no atómicas no tienen valor
                }
            } else //Si no hay punto (.) entonces esto está en notación de lista
            {
                PushedToken(token);
                s.SetCDR(INPUTList(in));
            }
        } else //Si no hay paréntesis la expresión es atómica
        {
            //Si no hay puntos ni paréntesis es atómica
            if (token.equals(")") || token.equals(".")) {
                throw new Exception("ERROR IN INPUT: '" + token + "' is a bad s expression");
            } else {
                s = new SExp(token);
            }
        }
        return s;
    }

    /**
     * Lee expresiones S en notación de lista
     *
     * @param in el inputstream
     * @return la expresión S correspondiente
     * @throws java.lang.Exception
     * @throws java.io.IOException
     */
    public static SExp INPUTList(InputStream in) throws Exception, IOException {
        SExp s = new SExp();
        String token = NextToken(in);

        if (token.equals(")")) {
            return SExp.NIL;
        } else {
            if (token.equals(".")) {
                throw new Exception("ERROR IN INPUT: misplaced '.'");
            }
        }
        PushedToken(token);

        s.SetCAR(INPUT(in));

        token = NextToken(in);
        if (token.equals(".")) {
            s.SetCDR(INPUT(in));
            token = NextToken(in);
            if (!token.equals(")")) {
                throw new Exception("ERROR IN INPUT: Ended in middle of s expression");
            }
        } else {
            PushedToken(token);
            s.SetCDR(INPUTList(in));
        }
        return s;
    }

    /**
     * Función auxiliar para pasar al siguiente token en un inputstream
     *
     * @param in el inputstream
     * @return el siguiente token
     * @throws Exception
     */
    public static String NextToken(InputStream in) throws Exception {
        String token = "";

        if (pushedToken != null) {
            token = pushedToken;
            pushedToken = null;
            return token;
        }

        int c = MoveNextRaw(in);

        if (c == -1) {
            return "";
        }

        if (c == '(' || c == ')' || c == '.') {
            token = String.valueOf((char) c);
        } else {
            do {
                token += ((char) c);
                in.mark(2);  //on reset comes back here
                c = in.read();
            } while (c != ')' && c != '(' && c != -1 && c != '\n' && c != '\t' && c != ' ');

            if (c == ')' || c == '(' || c == '.') {
                in.reset();
            }
        }
        return token;
    }

    /**
     * Función tokenizadora en crudo (solo divide la entrada)
     *
     * @param in Inputstream
     * @return The raw values
     * @throws java.lang.Exception
     */
    public static int MoveNextRaw(InputStream in) throws Exception {
        int b;
        do {
            b = in.read();
        } while (b == ' ' || b == '\t' || b == '\n');
        //Ignoramos espacios, tabulaciones, y corridas de carrete. El último hace posible leer varias líneas

        return b;
    }

    /**
     * Función auxiliar para pasar al siguiente token
     *
     * @param token string tokenizada
     */
    public static void PushedToken(String token) {
        pushedToken = token;
    }

}
