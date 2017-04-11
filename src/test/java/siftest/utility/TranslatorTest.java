package siftest.utility;

import org.junit.Assert;
import org.junit.Test;
import sif.utility.Translator;

public class TranslatorTest {

    private class TestTranslator extends Translator {

        public Boolean testMe() {
            return true;
        }
    }

    @Test
    public void extendTranslatorTest() {
        TestTranslator t = new TestTranslator();
        Assert.assertTrue(t.testMe());
    }

    @Test
    public void invalidKeyTest() {
        String key = "some weird key";
        String test = Translator.tl(key);
        Assert.assertEquals(key, test);
    }

}
