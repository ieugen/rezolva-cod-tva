package ro.ieugen.tva;


import lombok.Data;
import lombok.Value;
import lombok.experimental.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Value
@Builder
@Slf4j
public class MyObject {

    private String prop1;
    private String prop2;
    private List<String> names;


    public String getProp1(){
        log.info("aaaaa");
        return "aaaaaaaa";
    }

}
