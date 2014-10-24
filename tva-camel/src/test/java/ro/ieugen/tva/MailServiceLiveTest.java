package ro.ieugen.tva;

import org.junit.Ignore;
import org.junit.Test;
import ro.ieugen.tva.api.model.CompanyRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Ignore
public class MailServiceLiveTest {

    private MailService sender = new MailService(createFaceEnv());

    public static Map<String, Object> createFaceEnv() {
        Map<String, Object> config = new HashMap<>();
        config.put(MailService.TVA_SMTP_HOST, "smtp.zoho.com");
        config.put(MailService.TVA_SMTP_USERNAME, "heroku.bot@ieugen.ro");
        config.put(MailService.TVA_SMTP_PASS, " replace with valid password");

        return config;
    }

    @Test
    public void testEmailSender() throws Exception {
        sender.sendEmail("stan.ieugen@gmail.com", fetchCompanyVatInformation());
    }

    private List<CompanyRecord> fetchCompanyVatInformation() {
        List<CompanyRecord> records = new ArrayList<>();
        records.add(new CompanyRecord("A SRL", "RO112233", "true"));
        records.add(new CompanyRecord("B SRL", "RO112244", "false"));
        records.add(new CompanyRecord("C SRL", "RO112255", "true"));
        return records;
    }
}