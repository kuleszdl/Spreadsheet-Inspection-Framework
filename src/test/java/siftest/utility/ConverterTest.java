package siftest.utility;

import org.junit.Assert;
import org.junit.Test;
import sif.utility.Converter;

import static sif.utility.Converter.columnToInteger;
import static sif.utility.Converter.columnToString;

public class ConverterTest {


    @Test
    public void extendConverter() {
        TestConverter c = new TestConverter();
        Assert.assertTrue(c.testMe());
    }

    @Test
    public void testColumnConversion() {
        Assert.assertEquals(1, columnToInteger("A"));
        Assert.assertEquals("A", columnToString(1));
        Assert.assertEquals(26, columnToInteger("Z"));
        Assert.assertEquals("Z", columnToString(26));
        Assert.assertEquals(27, columnToInteger("AA"));
        Assert.assertEquals("AA", columnToString(27));
        Assert.assertEquals(53, columnToInteger("BA"));
        Assert.assertEquals("BA", columnToString(53));
        Assert.assertEquals(702, columnToInteger("ZZ"));
        Assert.assertEquals("ZZ", columnToString(702));
        Assert.assertEquals(703, columnToInteger("AAA"));
        Assert.assertEquals("AAA", columnToString(703));
    }

    private class TestConverter extends Converter {

        public Boolean testMe() {
            return true;
        }
    }
}


