import Parser.XMLValidator;
import org.junit.Assert;
import org.junit.Test;

public class XMLValidatorTest {
    @Test
    public void validTest() {
        Assert.assertTrue(XMLValidator.isValid("src/test/java/resources/pagesValid.xml", "src/main/resources/pages.xsd"));
    }

    @Test
    public void  invalidTest() {
        Assert.assertFalse(XMLValidator.isValid("src/test/java/resources/pagesInvalid.xml", "src/main/resources/pages.xsd"));
    }
}
