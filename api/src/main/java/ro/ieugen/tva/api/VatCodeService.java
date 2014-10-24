package ro.ieugen.tva.api;

public interface VatCodeService {

    void resolveVatCodes(String csvVatCodeList, String email);
}
