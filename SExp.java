/**
 * LISP S-Expression (para el cálculo lambda)
 */

public class SExp
{
    // En notación dot, car es la sección antes del punto y cdr la sección despues del punto
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
     * Constructor vacío para funciones particulares (en ocasiones hay que construir la expresión en otro lado)
     */
    SExp(){}

    /**
     * Constructor para expresiones ATÓMICAS
     * @param valor
     */
    SExp(String valor)
    {
        value = valor;
        isAtomic = true;
        car = null;
        cdr = null;
    }


    /**
     * Devuelve la S-expresión en notación con puntos
     * @return dot notation
     */
    public String ToStringDotNotation()
    {
        if (isAtomic)
        {
            return this.value;
        }
        else
        {
            return "("+car.ToStringDotNotation()+" . "+cdr.ToStringDotNotation()+")";
        }
    }



    /**
     * Devuelve la expresión S en notación de lista
     * This function is a little off
     * @return
     */
    public String ToStringListNotation()
    {
        String str;

        if (isAtomic)
        {
            str = value;
        } else
        {
            str = "(";
            str += car.ToStringListNotation();
            if (!cdr.IsNull())
            {
                if (!cdr.IsAtomic())
                {
                    String cdrStr = cdr.ToStringListNotation();
                    str += " " + cdrStr.substring(1, cdrStr.length() - 1);
                } else
                {
                    str += " . ";
                    str += cdr.ToStringListNotation();
                }
            }
            str += ")";
        }
        return str;
    }

    /**
     * OUTPUT por default es en notación dot (esta función es un overloading del OUTPUT
     * @return La expresión en forma de string en notación dot como un output
     */
    public String OUTPUT()
    {
        return this.OUTPUT(true);
    }

    /**
     * OUTPUT 
     * @param dot Si se utiliza notación dot o no
     * @return La expresión en forma de string (puede ser como dot o como lista) como un output
     */
    public String OUTPUT(boolean dot)
    {
        if(dot)
        {
            return this.ToStringDotNotation();
        }
        else
        {
            return this.ToStringListNotation();
        }
    }

    /**
     * Hacer la expresión atómica
     * @param isAtom
     */
    public void SetAtom(boolean isAtom)
    {
        this.isAtomic = isAtom;
    }

    /**
     * Set the cdr
     * @param cdr  The value of the cdr
     */
    public void SetCDR(SExp cdr)
    {
        this.cdr = cdr;
    }

    /**
     * Get the CDR
     * @return The CDR
     */
    public SExp CDR()
    {
        return cdr;
    }

    /**
     * Set the car
     * @param car The value for the car
     */
    public void SetCAR(SExp car)
    {
        this.car = car;
    }

    /**
     * Get the car
     * @return The car
     */
    public SExp CAR()
    {
        return car;
    }

    /**
     * Get the value of an atomic s expression
     * @return The value
     */
    public String GetValue()
    {
        return value;
    }

    /**
     * Set the value of an atomic S expression
     * @param value The new value
     */
    public void SetValue(String value)
    {
        this.value = value;
    }

    /**
     * Get the length of an S-Expression, primarily used for parameter list
     * checks
     * @return The length of the s expression
     */
    public int Length()
    {
        if (IsNull())
        {
            return 0;
        }

        if (IsAtomic())
        {
            return 1;
        }

        return 1 + cdr.Length();
    }
    
    /**
     * Is the S expression atomic
     * @return true=atomic
     */
    public boolean IsAtomic()
    {
        return isAtomic;
    }

    /**
     * Is the atom an integer
     * @return
     */
    public boolean IsInteger()
    {
        if (!isAtomic)  //if not an atom cant be integer
        {
            return false;
        }
        try
        {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e)  //if no exception then is int
        {
            return false;
        }
    }

    /**
     * Is the atom null
     * @return
     */
    public boolean IsNull()
    {
        if (car == null && cdr == null && value.equalsIgnoreCase("NIL"))  //NIL is null too
        {
            return true;
        }
        return false;
    }
    
}

