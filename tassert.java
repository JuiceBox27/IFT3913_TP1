import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class tassert {

    @Test
    // The `testIsLocBlank()` method is a test case that checks whether the `IsLoc()` method returns
    // `false` when given an empty string as input.
    public void testIsLocBlank() {
        assertFalse(tloc.IsLoc(""));
    }

    @Test
    // The `testIsLocSingleComment()` method is a test case that checks whether the `IsLoc()` method
    // returns `false` when given a single-line comment as input.
    public void testIsLocSingleComment() {
        assertFalse(tloc.IsLoc("//"));
    }

    @Test
    // The `testIsLocMultilineComment()` method is a test case that checks whether the `IsLoc()` method
    // returns `false` when given a multiline comment as input.
    public void testIsLocMultilineComment() {
        assertFalse(tloc.IsLoc("/* \n Multiline comment on single line. \n */"));
    }

    @Test
    // The `testIsLocMultilineCommentOnSingleLine()` method is a test case that checks whether the
    // `IsLoc()` method returns `false` when given a single-line comment enclosed within multiline
    // comment symbols as input.
    public void testIsLocMultilineCommentOnSingleLine() {
        assertFalse(tloc.IsLoc("/* Multiline comment on single line. */"));
    }

    @Test
    // The `testIsLocMultilineCommentInSingleLoc()` method is a test case that checks whether the
    // `IsLoc()` method returns `false` when given a line of code that contains a multiline comment.
    public void testIsLocMultilineCommentInSingleLoc() {
        assertFalse(tloc.IsLoc("int /* Comment in myInt declaration. */ myInt = 0;"));
    }

    @Test 
    // The `testIsLocMultilineCommentWithLoc()` method is a test case that checks whether the `IsLoc()`
    // method returns `false` when given a line of code that contains a multiline comment.
    public void testIsLocMultilineCommentWithLoc() {
        assertFalse(tloc.IsLoc("int /*\r\n" + //
                "        Multiline comment in myInt2 declaration. */ myInt2 = 0;"));
    }
}
