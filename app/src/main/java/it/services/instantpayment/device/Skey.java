package it.services.instantpayment.device;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Skey {

    public Skey() {
    }

    @Attribute(name = "ci", required = false)
    public String ci;

    @Text(required = false)
    public String value;

}
