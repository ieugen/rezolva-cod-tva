package ro.ieugen.tva.csv;

import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = ";", quoting = true, generateHeaderColumns = true)
@Data
public class VatRecord {

    @DataField(pos = 1, name = "submittedName")
    String submittedName;

    @DataField(pos = 2, name = "vatCode")
    String vatCode;

    @DataField(pos = 3, name = "requestDate")
    Date requestDate;

    @DataField(pos = 4, name = "valid")
    String valid;

    @DataField(pos = 5, name = "name")
    String name;

    @DataField(pos = 6, name = "address")
    String address;

}
