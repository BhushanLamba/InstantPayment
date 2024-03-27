package it.services.instantpayment.device;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "DeviceInfo")
public class DeviceInfo {

    public DeviceInfo() {
    }

//    @Attribute(name = "srno")
//    public String srno;

    @Attribute(name = "dpId", required = false)
    public String dpId;

    @Attribute(name = "rdsId", required = false)
    public String rdsId;

    @Attribute(name = "rdsVer", required = false)
    public String rdsVer;

    @Attribute(name = "dc", required = false)
    public String dc;

    @Attribute(name = "mi", required = false)
    public String mi;

    @Attribute(name = "mc", required = false)
    public String mc;

    @Element(name = "additional_info", required = false)
    public additional_info add_info;

}
