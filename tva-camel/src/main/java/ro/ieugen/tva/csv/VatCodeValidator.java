package ro.ieugen.tva.csv;

import lombok.NonNull;

public class VatCodeValidator {

    /**
     * Remove leading 'RO' if there; Remaining code is valid if only it contains only numbers and
     * has a length less than 10
     * <p/>
     * Better algorithm here: <a href="http://ro.wikipedia.org/wiki/Cod_de_Identificare_Fiscal%C4%83">CIF</a>
     *
     * @param vatCode
     * @return
     * @throws java.lang.IllegalArgumentException if code is invalid
     */
    public String validateAndClean(@NonNull String vatCode) {
        String uppercased = trimLeadingRo(vatCode);
        validateVatCodeHasOnlyNumbers(uppercased);
        validateVatCodeLength(uppercased);
        return uppercased;
    }

    private void validateVatCodeLength(String vatCode) {
        if (vatCode.length() > 10) {
            throw new IllegalArgumentException("VatCode exceeds maximum length 10");
        }
    }

    private String trimLeadingRo(String vatCode) {
        String uppercased = vatCode.trim().toUpperCase();
        if (uppercased.startsWith("RO")) {
            uppercased = uppercased.substring(2);
        }
        return uppercased;
    }

    private void validateVatCodeHasOnlyNumbers(String vatCode) {
        for (char c : vatCode.toCharArray()) {
            if (c < '0' || c > '9')
                throw new IllegalArgumentException("Vat code contains illegal characters " + c);
        }
    }
}
