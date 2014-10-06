package ro.ieugen.tva;

import org.junit.Test;

public class MyObjectTest {

    @Test
    public void testName() throws Exception {
        MyObject object = MyObject.builder()
                .prop1("111")
                .prop2("ssss")
                .build();

        object.getProp1();

        System.out.println(object.toString());

    }
}