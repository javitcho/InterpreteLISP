import java.util.*;

/**
 * Esta es la lista de expresiones S asociadas (sus definiciones con argumentos)
 * @author 
 */
public class ListaAsociacion
{
    LinkedList<SExp> al;

    public ListaAsociacion()
    {
	al=new LinkedList<SExp>();
    }

	public ListaAsociacion(LinkedList<SExp> a)
	{
		this.al=a;
	}

    /**
     * Devuelve el valor de una pareja asociada (el valor de una variable)
     * @param var El nombre de la variable que se va a buscar
     * @return el valor de la variable buscada
     */
	public SExp GetBindingPair(String var)
	{
		Iterator itr = al.listIterator();
		SExp binding;

		while (itr.hasNext()){
			binding = (SExp)itr.next();
			if (binding.CAR().GetValue().equalsIgnoreCase(var))
				return binding.CDR();
		}
        //Si no está la variable entonces es nula
		return null;
	}

    /**
     * Agrega una pareja a la lista
     * @param aList La lista original
     * @param pars Los parámetros que se van a agregar
     * @param values Los valores que se le darán a los parámetros
     * @return Una nueva lista de asociación
     */
	public ListaAsociacion AddBindingPairs(ListaAsociacion aList, SExp pars, SExp values)
	{
		ListaAsociacion newList = new ListaAsociacion(aList.al);

		SExp pars_t = pars;
		SExp values_t = values;

		while (!pars_t.IsNull() && !values_t.IsNull())	{
			newList.al.addFirst(Lambdi.CONS(pars_t.CAR(), values_t.CAR()));
			pars_t = pars_t.CDR();
			values_t = values_t.CDR();
		}
                this.al = newList.al;
		return newList;
	}
}

