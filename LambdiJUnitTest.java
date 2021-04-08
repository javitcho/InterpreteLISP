
import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;

/**
 *
 * @author Javier Mejia, José López, Lorena Beltrán
 */
public class LambdiJUnitTest {

    public LambdiJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testCONS() {
        SExp car = new SExp("x");
        SExp cdr = new SExp("4");
        assertEquals("(x . 4)", Lambdi.CONS(car, cdr).ToStringDotNotation());
    }

    @Test
    public void testPLUS() {
        try {
            assertEquals(new SExp("6").ToStringDotNotation(), Lambdi.PLUS(new SExp("4"), new SExp("2")).CAR().ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMINUS() {
        try {
            assertEquals(new SExp("4").ToStringDotNotation(), Lambdi.MINUS(new SExp("6"), new SExp("2")).CAR().ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testTIMES() {
        try {
            assertEquals(new SExp("12").ToStringDotNotation(), Lambdi.TIMES(new SExp("6"), new SExp("2")).CAR().ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testQUOTIENT() {
        try {
            assertEquals(new SExp("4").ToStringDotNotation(), Lambdi.QUOTIENT(new SExp("8"), new SExp("2")).CAR().ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testREMAINDER() {
        try {
            assertEquals(new SExp("2").ToStringDotNotation(), Lambdi.REMAINDER(new SExp("6"), new SExp("4")).CAR().ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGREATER() {
        try {
            assertEquals(SExp.T, Lambdi.GREATER(new SExp("6"), new SExp("2")));
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testLESS() {
        try {
            assertEquals(SExp.NIL, Lambdi.LESS(new SExp("6"), new SExp("2")));
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testEQ() {
        try {
            assertEquals(SExp.NIL, Lambdi.EQ(new SExp("6"), new SExp("2")));
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testINPUT() {
        byte[] data = "9".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        try {
            assertEquals(new SExp("9").ToStringDotNotation(), Lambdi.INPUT(input).ToStringDotNotation());
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testNextToken() {
        byte[] data = "18".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        try {
            assertEquals("", Lambdi.NextToken(input));
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMoveNextRaw() {
        byte[] data = "A".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        try {
            assertEquals(65, Lambdi.MoveNextRaw(input));
        } catch (Exception ex) {
            Logger.getLogger(LambdiJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
