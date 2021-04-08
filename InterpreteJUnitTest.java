
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */
public class InterpreteJUnitTest {

    ListaDefinicion dList;
    ListaAsociacion aList;
    Interprete jamie;

    public InterpreteJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dList = new ListaDefinicion();
        aList = new ListaAsociacion();
        jamie = new Interprete();
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    //Esta función se prueba mejor desde la consola
    @Test
    public void testEVAL() {
        SExp op = Lambdi.CONS(new SExp("QUOTE"), new SExp("4"));
        SExp res = null;
        try {
            res = jamie.EVAL(op, aList, dList, true);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(new SExp("4").ToStringDotNotation(), res.ToStringDotNotation());
    }

    @Test
    public void testEVLIS() {
        SExp op = Lambdi.CONS(Lambdi.CONS(new SExp("PLUS"), Lambdi.CONS(new SExp("4"), Lambdi.CONS(new SExp("5"), SExp.NIL))), SExp.NIL);
        op.SetAtom(false);
        SExp res = null;
        try {
            res = jamie.EVLIS(op, aList, dList);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(new SExp("9").ToStringDotNotation(), res.CAR().ToStringDotNotation());
    }

    @Test
    public void testEVCON() {
        SExp op = Lambdi.CONS(Lambdi.CONS(Lambdi.CONS(new SExp("EQ"), Lambdi.CONS(SExp.T, Lambdi.CONS(SExp.T, SExp.NIL))), SExp.T), SExp.NIL);
        op.SetAtom(false);
        SExp res = null;
        try {
            res = jamie.EVCON(op, aList, dList);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(SExp.T.ToStringDotNotation(), res.ToStringDotNotation());
    }

    @Test
    public void testAPPLY() {
        SExp f = new SExp("+");
        SExp x = Lambdi.CONS(new SExp("2"), Lambdi.CONS(new SExp("3"), SExp.NIL));
        SExp res = null;
        try {
            res = jamie.APPLY(f, x, aList, dList);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(new SExp("5").ToStringDotNotation(), res.ToStringDotNotation());
    }

    @Test
    public void testCheckParamCount() {
        SExp f = new SExp("+");
        SExp x = Lambdi.CONS(new SExp("2"), Lambdi.CONS(new SExp("3"), SExp.NIL));
        boolean res = false;
        try {
            res = jamie.CheckParamCount(f.GetValue(), x, 2);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(true, res);
    }

    @Test
    public void testConditionalValidation() {
        SExp condExp = Lambdi.CONS(Lambdi.CONS(new SExp("EQ"), Lambdi.CONS(SExp.T, Lambdi.CONS(SExp.T, SExp.NIL))), SExp.T);
        boolean res = false;
        try {
            res = jamie.ConditionalValidation(condExp);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(false, res);
    }

    @Test
    public void testDefunValidation() {
        SExp condExp = Lambdi.CONS(Lambdi.CONS(new SExp("DEFUN"), Lambdi.CONS(SExp.T, Lambdi.CONS(SExp.T, SExp.NIL))), SExp.T);
        boolean res = false;
        try {
            res = jamie.DefunValidation(condExp);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(false, res);
    }

    @Test
    public void testIsAtomInValidFormat() {
        String ID = "9";
        boolean res = false;
        try {
            res = jamie.IsAtomInValidFormat(ID);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(true, res);
    }

    @Test
    public void testIsIdentifierInValidFormat() {
        String ID = "PLUS";
        boolean res = false;
        try {
            res = jamie.IsIdentifierInValidFormat(ID);
        } catch (Exception ex) {
            Logger.getLogger(InterpreteJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(true, res);
    }

}
