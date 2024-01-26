package io.Adrestus.Backend;

import io.Adrestus.Backend.Config.NetworkConfiguration;
import io.Adrestus.Backend.Config.ResourcesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
@EnableScheduling
public class App {
    public static void main(String[] args) throws Exception {
        /*Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        socket.close();
        Properties props = new Properties();
        props.put("jwt.secret" , "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        props.put("jwt.expirationDateInMs", "60000");
        props.put("jwt.refreshExpirationDateInMs", "60000");
        props.put("spring.datasource.url", "jdbc:mysql://localhost:3306/user?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=true");
        props.put("spring.datasource.username", "panos");
        props.put("spring.datasource.password", "password");
        props.put("spring.jpa.hibernate.ddl-auto" , " update");
        props.put("server.address", socket.getLocalAddress().getHostAddress());
        props.put("server.port", "8080");*/
        SpringApplication app = new SpringApplication(App.class);
        app.addListeners(new NetworkConfiguration());
        app.addListeners(new ResourcesConfiguration());
        app.run(args);
    }
}
