import java.util.*;

/**
 * Esta es la lista de expresiones S definidas por el usuario (funciones, variables, etc)
 * @author 
 */
public class ListaDefinicion
{
    private LinkedList<SExp> dl=new LinkedList<SExp>();

	public SExp ADD(SExp def)
	{
		dl.addFirst(Lambdi.CONS(def.CAR(),Lambdi.CONS(def.CDR().CAR(), def.CDR().CDR().CAR())));
		return def.CAR();
	}

	public SExp GetFunctionDefinition(String fName)
	{
        if(dl.size()==0)
        {
            return null;
        }
		Iterator itr = dl.listIterator();
		while (itr.hasNext())	{
			SExp def = (SExp)itr.next();
			if (def.CAR().GetValue().equalsIgnoreCase(fName)){
				return def.CDR();
			}
		}
		return null;
	}

}

