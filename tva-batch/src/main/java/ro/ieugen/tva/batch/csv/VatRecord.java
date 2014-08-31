package ro.ieugen.tva.batch.csv;

import lombok.Data;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class VatRecord implements Serializable {

    String submittedName;

    String vatCode;

    Date requestDate;

    String valid;

    String name;

    String address;

    @Override
    public String toString() {
        return submittedName + ';' + vatCode + ';' + requestDate + ';' + valid + ';' +
                name + ';' + address;
    }
}
