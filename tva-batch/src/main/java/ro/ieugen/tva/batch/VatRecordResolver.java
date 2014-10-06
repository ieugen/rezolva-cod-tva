package ro.ieugen.tva.batch;

import eu.europa.ec.taxud.vies.services.checkvat.CheckVatPortType;
import lombok.extern.slf4j.Slf4j;
import ro.ieugen.tva.batch.csv.VatRecord;

import javax.batch.api.chunk.ItemProcessor;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class VatRecordResolver implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        VatRecord record = (VatRecord) item;
        log.info("Resolving record {}", record);

//        makeSoapRequestAndUpdateRecord(record);

        return record;
    }

    private void makeSoapRequestAndUpdateRecord(VatRecord record) throws MalformedURLException {
        URL url = new URL("http://ec.europa.eu/taxation_customs/vies/services/checkVatService");
        QName qName = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat", "checkVatPortType");
        Service service = Service.create(url, qName);
        CheckVatPortType servicePort = service.getPort(CheckVatPortType.class);
        Holder<Boolean> valid = new Holder<>();
        Holder<XMLGregorianCalendar> requestDate = new Holder<>();
        Holder<String> name = new Holder<>();
        Holder<String> address = new Holder<>();
        servicePort.checkVat(new Holder<>("RO"), new Holder<>(record.getVatCode()), requestDate, valid, name, address);

        record.setRequestDate(requestDate.value.toGregorianCalendar().getTime());
        record.setValid(valid.value.toString());
        record.setName(name.value);
        record.setAddress(address.value);
    }
}
