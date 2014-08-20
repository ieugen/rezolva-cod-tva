package ro.ieugen.tva;

import com.google.common.base.Splitter;
import org.apache.camel.Exchange;

public class SplitVatCodeList implements org.apache.camel.Expression {

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        String list = exchange.getIn().getBody(String.class);

        return type.cast(Splitter.on('\n').omitEmptyStrings().splitToList(list));
    }
}
