
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Service.REST.ServerService;

import static junit.framework.Assert.assertEquals;

public class test {

    @Autowired
    ServerService service;

    @Test
    public void connectionTest() throws Exception {
        service.notifyRemovedDevice("103482874633874499718", "97F82594-4CCF-46A6-8D5B-7F8CF5CDF0F0");

    }
}