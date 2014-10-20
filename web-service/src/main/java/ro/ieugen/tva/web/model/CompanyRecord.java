package ro.ieugen.tva.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRecord {

    private String name;
    private String identification;
    private String vatRegistered;

}
