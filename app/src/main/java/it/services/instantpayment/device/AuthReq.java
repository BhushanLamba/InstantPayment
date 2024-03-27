package it.services.instantpayment.device;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "Auth")
@Namespace(reference = "http://www.uidai.gov.in/authentication/uid-auth-request/2.0")
public class AuthReq {

    public AuthReq() {
    }

    @Attribute(name = "uid", required = false)
    public String uid;

    @Attribute(name = "rc", required = false)
    public String rc;

    @Attribute(name = "tid", required = false)
    public String tid;

    @Attribute(name = "ac", required = false)
    public String ac;

    @Attribute(name = "sa", required = false)
    public String sa;

    @Attribute(name = "ver", required = false)
    public String ver;

    @Attribute(name = "txn", required = false)
    public String txn;

    @Attribute(name = "lk", required = false)
    public String lk;

    @Element(name = "Uses", required = false)
    public Uses uses;

    @Element(name = "Meta", required = false)
    public Meta meta;

    @Element(name = "Skey", required = false)
    public Skey skey;

    @Element(name = "Data", required = false)
    public Data data;

    @Element(name = "Hmac", required = false)
    public String Hmac;

}
