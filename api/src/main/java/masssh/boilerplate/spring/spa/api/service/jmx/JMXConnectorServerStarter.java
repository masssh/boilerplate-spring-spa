package masssh.boilerplate.spring.spa.api.service.jmx;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

public class JMXConnectorServerStarter {
    private static final int port = 9111;

    public static void startJMXConnectorServer() throws IOException {
        LocateRegistry.createRegistry(port);
        final JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi");
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        JMXConnectorServerFactory.newJMXConnectorServer(url, null, mBeanServer).start();
    }
}
