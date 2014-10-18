package ro.ieugen.tva.csv;

import org.junit.Assert;
import org.junit.Test;

public class VatCodeValidatorTest {

    VatCodeValidator sanitizer = new VatCodeValidator();

    @Test(expected = NullPointerException.class)
    public void nullVatCodeThrowsNullPointerException() throws Exception {
        sanitizer.validateAndClean(null);
    }

    @Test
    public void removesRoPrefix() throws Exception {
        String result = sanitizer.validateAndClean("RO12345");
        Assert.assertEquals(result, "12345");

        result = sanitizer.validateAndClean("ro12345");
        Assert.assertEquals(result, "12345");

        result = sanitizer.validateAndClean("rO12345");
        Assert.assertEquals(result, "12345");
    }

    @Test(expected = InvalidVatCodeFormat.class)
    public void throwsIllegalArgumentExceptionIfLengthExceeedsMaxmum() throws Exception {
        sanitizer.validateAndClean("Ro12345678901");
    }

    @Test(expected = InvalidVatCodeFormat.class)
    public void throwsIllegalArgumentExceptionIfCodeContainsOtherCharactersBesidesNumbers() throws Exception {
        sanitizer.validateAndClean("RO1241x");
    }
}