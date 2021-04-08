
/**
 * Esta es la clase intérprete, que integra todos los elementos para su función EVAL (evaluar una entrada)
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */
public class Interprete {

    private final static String[] LISP_PRIMITIVES
            = {
                "CAR", "CDR", "CONS", "ATOM", "EQ", "EQUAL", "NULL", "INT", "PLUS", "MINUS", "TIMES", "QUOTIENT", "REMAINDER", "LESS", "GREATER", "DEFUN", "SETQ", "=", "<", ">", "+", "-", "*", "/", "%", "MOD"
            };

    /**
     * Evalúa una expresión S
     *
     * @param s The s expression to evaluate
     * @param aList The current association list
     * @param dList The current definition list
     * @param topLevel True if top level, if not top level no defuns allowed
     * @return The evlauated s expression
     * @throws java.lang.Exception
     */
    public SExp EVAL(SExp s, ListaAsociacion aList, ListaDefinicion dList, boolean topLevel) throws Exception {
        //Solo es realmente interesante el caso atómico (sino se procesa hasta llegar a los átomos)
        if (s.IsAtomic()) {
            //Los enteros y lógicos solo se devuelven
            if (s.IsInteger() || s.GetValue().equalsIgnoreCase("T") || s.GetValue().equalsIgnoreCase("NIL") || s == SExp.NIL || s == SExp.T) {
                return s;
            }

            //Aquí se procesan los identificadores
            if (IsAtomInValidFormat(s.GetValue())) {
                SExp binding = aList.GetBindingPair(s.GetValue());
                if (binding != null) {
                    return binding;
                } else {
                    throw new Exception("ERROR IN EVAL: " + s.GetValue() + " is not bound");
                }
            } else {
                throw new Exception("ERROR IN EVAL: '" + s.GetValue() + "' is not a valid identifier");
            }
        } else //Para el caso no atómico vamos a separar el car del cdr
        {
            //El car es atómico cuando se define una función o una variable
            if (!s.CAR().IsAtomic()) {
                throw new Exception("ERROR IN EVAL: '" + s.CAR().toString() + "' is an illegal function name");
            }

            //Aquí vamos en los casos particulares COND, QUOTE, y DEFUN (DEFUN solo funciona en top level)
            if (s.CAR().GetValue().equalsIgnoreCase("COND")) {
                ConditionalValidation(s);
                return EVCON(s.CDR(), aList, dList);

            }

            if (s.CAR().GetValue().equalsIgnoreCase("QUOTE")) {
                CheckParamCount("QUOTE", s.CDR(), 1);
                return s.CDR().CAR();

            }

            if (s.CAR().GetValue().equalsIgnoreCase("DEFUN")) {
                if (!topLevel) {
                    throw new Exception("ERROR IN EVAL: No Nested Defuns allowed");
                }
                DefunValidation(s);
                return dList.ADD(s.CDR());
            }

            String f = s.CAR().GetValue();

            if (s.CAR().GetValue().equalsIgnoreCase("SETQ")) {
                if (!topLevel) {
                    throw new Exception("ERROR IN EVAL: No Nested Setqs allowed");
                }
                CheckParamCount("SETQ", s.CDR(), 2);
                aList.AddBindingPairs(aList, s.CDR().CAR(), s.CDR().CDR());
                return s.CDR().CAR().CAR();
            }

            //También revisamos el caso particular de los primitivos
            //Por ahora solo se verifica pero luego se aplicará 
            boolean isLISP_builtin = false;
            for (int i = 0; i < LISP_PRIMITIVES.length; i++) {
                if (f.equalsIgnoreCase(LISP_PRIMITIVES[i])) {
                    isLISP_builtin = true;
                    break;
                }
            }

            //También revisamos si el car está en la lista de funciones definidas por el usuario
            //Solo verificamos luego se aplicará
            if (!isLISP_builtin) {
                SExp def = dList.GetFunctionDefinition(f);
                //El nulo nos dice que no está definida la función
                if (def == null) {
                    throw new Exception("ERROR IN EVAL: '" + f + "' is not defined");
                }
            }

            //el cdr no puede ser atómico
            if (s.CDR().IsAtomic()) {
                throw new Exception("ERROR IN EVAL: '" + s.CAR().GetValue() + "' has bad arguments");
            }

            //Por último aplicamos el car al cdr en forma de lista (todo en LISP son listas)
            return APPLY(s.CAR(), EVLIS(s.CDR(), aList, dList), aList, dList);
        }
    }

    /**
     * LISP evaluate list function
     *
     * @param l The list as s expression to evaluate
     * @param aList The current association list
     * @param dList The current definition list
     * @return The evaluated list
     * @throws java.lang.Exception
     */
    public SExp EVLIS(SExp l, ListaAsociacion aList, ListaDefinicion dList) throws Exception {
        if (l.IsNull()) {
            return SExp.NIL;
        } else {
            return Lambdi.CONS(EVAL(l.CAR(), aList, dList, false), EVLIS(l.CDR(), aList, dList));
        }
    }

    /**
     * LISP evaluate conditional function
     *
     * @param be The condtions, operation pairs
     * @param aList The current association list
     * @param dList The current definition list
     * @return The evaluated conditional
     * @throws java.lang.Exception
     */
    public SExp EVCON(SExp be, ListaAsociacion aList, ListaDefinicion dList) throws Exception {
        if (be.IsNull()) {
            throw new Exception("ERROR IN EVCON: All conditionals cannot be null");
        }

        //if the current conditional is true, then evaluate its expression
        if (Lambdi.EQ(EVAL(be.CAR().CAR(), aList, dList, false), SExp.T).GetValue().equals("T")) {
            return EVAL(be.CAR().CDR().CAR(), aList, dList, false);
        } else //get the next conditional in the list
        {
            return EVCON(be.CDR(), aList, dList);
        }

    }

    /**
     * Aplicar una funcion f a argumentos x
     *
     * @param f The function to apply
     * @param x the arguments
     * @param aList The current association list
     * @param dList The current definition list
     * @return f(x)
     * @throws java.lang.Exception
     */
    public SExp APPLY(SExp f, SExp x, ListaAsociacion aList, ListaDefinicion dList) throws Exception {
        String fName = f.GetValue();

        //Recorremos todas las posibilidades para f
        if (fName.equalsIgnoreCase("CAR")) {
            CheckParamCount("CAR", x, 1);
            if (x.CAR().IsAtomic()) {
                throw new Exception("ERROR IN APPLY: CAR cannot be performed on atom");
            }
            return x.CAR().CAR();
        }

        if (fName.equalsIgnoreCase("CDR")) {
            CheckParamCount("CDR", x, 1);
            if (x.CAR().IsAtomic()) {
                throw new Exception("ERROR IN APPLY: CDR cannot be performed on atom");
            }
            return x.CAR().CDR();
        }

        if (fName.equalsIgnoreCase("CONS")) {
            CheckParamCount("CONS", x, 2);
            return Lambdi.CONS(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("EQ") | fName.equalsIgnoreCase("=") | fName.equalsIgnoreCase("EQUAL")) {
            CheckParamCount("EQ", x, 2);
            return Lambdi.EQ(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("ATOM")) {
            if (x.CAR().IsAtomic()) {
                return SExp.T;
            } else {
                return SExp.NIL;
            }
        }
        if (fName.equalsIgnoreCase("NULL")) {
            if (x.CAR().IsNull()) {
                return SExp.T;
            } else {
                return SExp.NIL;
            }
        }

        if (fName.equalsIgnoreCase("INT")) {
            CheckParamCount("INT", x, 1);
            if (x.CAR().IsInteger()) {
                return SExp.T;
            } else {
                return SExp.NIL;
            }
        }

        if (fName.equalsIgnoreCase("PLUS") | fName.equalsIgnoreCase("+")) {
            //Ahora se soportan sumas de n enteros
            //CheckParamCount("PLUS", x, 2);
            return Lambdi.PLUS(x.CAR(), x.CDR());
        }

        if (fName.equalsIgnoreCase("MINUS") | fName.equalsIgnoreCase("-")) {
            CheckParamCount("MINUS", x, 2);
            return Lambdi.MINUS(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("TIMES") | fName.equalsIgnoreCase("*")) {
            //Ahora se soportan productos de n enteros
            //CheckParamCount("TIMES", x, 2);
            return Lambdi.TIMES(x.CAR(), x.CDR());
        }

        if (fName.equalsIgnoreCase("QUOTIENT") | fName.equalsIgnoreCase("/")) {
            CheckParamCount("QUOTIENT", x, 2);
            return Lambdi.QUOTIENT(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("REMAINDER") | fName.equalsIgnoreCase("%") | fName.equalsIgnoreCase("MOD")) {
            CheckParamCount("REMAINDER", x, 2);
            return Lambdi.REMAINDER(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("LESS") | fName.equalsIgnoreCase("<")) {
            CheckParamCount("LESS", x, 2);
            return Lambdi.LESS(x.CAR(), x.CDR().CAR());
        }

        if (fName.equalsIgnoreCase("GREATER") | fName.equalsIgnoreCase(">")) {
            CheckParamCount("GREATER", x, 2);
            return Lambdi.GREATER(x.CAR(), x.CDR().CAR());
        }

        //Si no es una función de las anteriores, debe de ser una función definida por el usuario
        SExp def = dList.GetFunctionDefinition(fName);

        SExp pars = def.CAR();
        SExp body = def.CDR();

        CheckParamCount(fName, x, pars.Length());

        //Evaluar la función
        return EVAL(body, aList.AddBindingPairs(aList, pars, x), dList, false);

    }

    /**
     * Asegura que los parámetros son suficientes
     *
     * @param funcName El nombre de la función
     * @param paramList La lista de parámetros
     * @param num El número esperado de parámetros
     * @return si los parámetros son válidos
     * @throws java.lang.Exception
     */
    public boolean CheckParamCount(String funcName, SExp paramList, int num) throws Exception {
        if (paramList.Length() != num) {
            throw new Exception(funcName + " expects " + num + " parameters, but " + paramList.Length() + " were provided");
        }
        return true;
    }

    /**
     * Función auxiliar para validar condiciones
     *
     * @param condExp la expresión condicional a validar
     * @return la validez de la condición
     * @throws java.lang.Exception
     */
    public boolean ConditionalValidation(SExp condExp) throws Exception {

        SExp expList = condExp.CDR();

        if (expList.IsAtomic()) {
            throw new Exception("ERROR IN EVAL: conditonal cannot be atomic");
        }

        int len = expList.Length();

        for (int i = 0; i < len; i++) {
            SExp cond = expList.CAR();
            if (cond.Length() != 2) //Solo tiene que tener la condición y la expresión
            {
                throw new Exception("ERROR IN EVAL: condtional is not in good form");
            }
            expList = expList.CDR();
        }
        return true;
    }

    /**
     * Función auxiliar para validar la definición de funciones
     *
     * @param def La definición de una función en forma de expresión S
     * @return La validez de la función
     * @throws java.lang.Exception
     */
    public boolean DefunValidation(SExp def) throws Exception {
        if (def.Length() != 4) {
            throw new Exception("ERROR IN EVAL: function definiton is not in good form");
        }

        SExp fName = def.CDR().CAR();

        if (!IsIdentifierInValidFormat(fName.GetValue()) || !fName.IsAtomic()) {
            throw new Exception("ERROR IN EVAL: function name is bad");
        }

        SExp parameterList = def.CDR().CDR().CAR();

        if (parameterList.IsAtomic()) {
            throw new Exception("ERROR IN EVAL: parameter list is bad");
        }

        int len = parameterList.Length();

        for (int i = 0; i < len; i++) {
            SExp par = parameterList.CAR();
            if (!par.IsAtomic() || !IsIdentifierInValidFormat(par.GetValue())) {
                throw new Exception("ERROR IN EVAL: '" + par.ToStringListNotation() + "' is a bad parameter");
            }
            parameterList = parameterList.CDR();
        }
        return true;
    }

    /**
     * Función auxiliar para validar átomos (enteros o identificadores)
     *
     * @param ID el candidato a átomo
     * @return la validez de ID
     */
    public boolean IsAtomInValidFormat(String ID) {
        try {
            Integer.parseInt(ID);
            return true;
        } catch (Exception e) {
            return IsIdentifierInValidFormat(ID);
        }
    }

    /**
     * Función auxiliar para validar la forma de un identificador
     *
     * @param ID el candidato a identificador
     * @return la validez de ID
     */
    public boolean IsIdentifierInValidFormat(String ID) {
        //first char must be a letter
        if (!Character.isLetter(ID.charAt(0))) {
            return false;
        }
        //subsequent characters can be letters or digits, but no punctuation
        for (int i = 1; i < ID.length(); i++) {
            if (!Character.isLetterOrDigit(ID.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
