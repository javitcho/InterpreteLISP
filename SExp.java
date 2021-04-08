
/**
 * LISP S-Expression (para el cálculo lambda)
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */

public class SExp {

    // En notación dot, car es la sección antes del punto y cdr la sección despues del punto (address y decrement)
    private SExp car;
    private SExp cdr;

    //Toda Sexpresión tiene un valor (la expresión)
    private String value;
    //Las Sexpresiones pueden o no ser atómicas
    private boolean isAtomic;
    // Expresiones constantes para TRUE Y FALSE
    public final static SExp T = new SExp("T");
    public final static SExp NIL = new SExp("NIL");

    //Esta variable estática facilita la tokenización (es una temporal que se accede de otras funciones)
    private static String pushedToken = null;

    /**
     * Constructor vacío para funciones particulares (en ocasiones hay que
     * construir la expresión en otro lado)
     */
    SExp() {
    }

    /**
     * Constructor para expresiones ATÓMICAS
     *
     * @param valor
     */
    SExp(String valor) {
        value = valor;
        isAtomic = true;
        car = null;
        cdr = null;
    }

    /**
     * Devuelve la S-expresión en notación con puntos
     *
     * @return dot notation
     */
    public String ToStringDotNotation() {
        if (isAtomic) {
            return this.value;
        } else {
            return "(" + car.ToStringDotNotation() + " . " + cdr.ToStringDotNotation() + ")";
        }
    }

    /**
     * Devuelve la expresión S en notación de lista This function is a little
     * off
     *
     * @return
     */
    public String ToStringListNotation() {
        String str;

        if (isAtomic) {
            str = value;
        } else {
            str = "(";
            str += car.ToStringListNotation();
            if (!cdr.IsNull()) {
                if (!cdr.IsAtomic()) {
                    String cdrStr = cdr.ToStringListNotation();
                    str += " " + cdrStr.substring(1, cdrStr.length() - 1);
                } else {
                    str += " . ";
                    str += cdr.ToStringListNotation();
                }
            }
            str += ")";
        }
        return str;
    }

    /**
     * OUTPUT por default es en notación dot (esta función es un overloading del
     * OUTPUT
     *
     * @return La expresión en forma de string en notación dot como un output
     */
    public String OUTPUT() {
        return this.OUTPUT(true);
    }

    /**
     * OUTPUT
     *
     * @param dot Si se utiliza notación dot o no
     * @return La expresión en forma de string (puede ser como dot o como lista)
     * como un output
     */
    public String OUTPUT(boolean dot) {
        if (dot) {
            return this.ToStringDotNotation();
        } else {
            return this.ToStringListNotation();
        }
    }

    /**
     * Hacer la expresión atómica
     *
     * @param isAtom
     */
    public void SetAtom(boolean isAtom) {
        this.isAtomic = isAtom;
    }

    /**
     * Set cdr
     *
     * @param cdr El cdr
     */
    public void SetCDR(SExp cdr) {
        this.cdr = cdr;
    }

    /**
     * Devuelve el cdr
     *
     * @return El cdr
     */
    public SExp CDR() {
        if (isAtomic) {
            return this;
        }
        return cdr;
    }

    /**
     * Modifica el car de una expresión S
     *
     * @param car El nuevo valor del car
     */
    public void SetCAR(SExp car) {
        this.car = car;
    }

    /**
     * Devuelve el car de la expresión S
     *
     * @return el car
     */
    public SExp CAR() {
        if (isAtomic) {
            return this;
        }
        return car;
    }

    /**
     * Devuelve el valor de la expresión S
     *
     * @return El valor de la expresión
     */
    public String GetValue() {
        return value;
    }

    /**
     * Modificar el valor de una expresión S
     *
     * @param value El nuevo valor de la expresión
     */
    public void SetValue(String value) {
        this.value = value;
    }

    /**
     * Longitud de una expresión S
     *
     * @return La longitud de la expresión S
     */
    public int Length() {
        if (IsNull()) {
            return 0;
        }

        if (IsAtomic()) {
            return 1;
        }

        return 1 + cdr.Length();
    }

    /**
     * Si la expresión es atómica
     *
     * @return true cuando es atómica
     */
    public boolean IsAtomic() {
        return isAtomic;
    }

    /**
     * Si el átomo es un entero
     *
     * @return
     */
    public boolean IsInteger() {
        if (!isAtomic) //if not an atom cant be integer
        {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) //if no exception then is int
        {
            return false;
        }
    }

    /**
     * Si el átomo está vacío
     *
     * @return
     */
    public boolean IsNull() {
        if (car == null && cdr == null && value.equalsIgnoreCase("NIL")) //NIL is null too
        {
            return true;
        }
        return false;
    }

}
