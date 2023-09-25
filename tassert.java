import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

public class tassert {

    /* 
    Oups I have misread the TP1.. we do not need unit tests here. I will keep them until I create a branch for them. 
    In case we need them further down the line.
    */
    @Test
    // The `testIsLocBlank()` method is a test case that checks whether the `IsLoc()` method returns
    // `false` when given an empty string as input.
    public void testParseLocBlank() {
        assertTrue(tloc.parseLoc("", new LinkedList<String>()).isBlank());
    }

    public void testParseLocSpacesAndTabs() {
        assertTrue(tloc.parseLoc("\s\t", new LinkedList<String>()).isBlank());
    }

    @Test
    // The `testIsLocSingleComment()` method is a test case that checks whether the `IsLoc()` method
    // returns `false` when given a single-line comment as input.
    public void testParseLocSingleComment() {
        assertTrue(tloc.parseLoc("//This is a single comment", new LinkedList<String>()).isBlank());
    }

    public void testParseLocSingleCommentWithCode() {
        assertTrue(tloc.parseLoc("int myInt = 0; //This is a single comment in a valid line of code", new LinkedList<String>()) == "int myInt = 0;");
    }

    @Test
    // The `testIsLocMultilineComment()` method is a test case that checks whether the `IsLoc()` method
    // returns `false` when given a multiline comment as input.
    public void testParseLocCommentBlock() {
        assertTrue(tloc.parseLoc("/* Multiline comment on single line. */", new LinkedList<String>()).isBlank());
    }
    
    // @Test
    // // The `testIsLocMultilineCommentInSingleLoc()` method is a test case that checks whether the
    // // `IsLoc()` method returns `false` when given a line of code that contains a multiline comment.
    // public void testParseLocCommentBlockInLoc() {
    //     assertFalse(tloc.parseLoc("int /* Comment in myInt declaration. */ myInt = 0;", new LinkedList<String>()) == "int myInt = 0;");
    // }

}
