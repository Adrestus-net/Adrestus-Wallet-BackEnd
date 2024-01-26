package io.Adrestus.Backend.Config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Component
public class NetworkConfiguration implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private String host;


    public NetworkConfiguration() throws IOException {
    }

    @SneakyThrows
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        setHost(socket.getLocalAddress().getHostAddress());
        socket.close();
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> map = new HashMap<>();
        map.put("server.address", this.host);
        propertySources.addFirst(new MapPropertySource("newmap", map));
    }

    public String getHost() {
        return host;
    }

    @Value("${server.address}")
    public void setHost(String host) {
        this.host = host;
    }
}
