package pt.andre.projecto;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Service.APIService;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import pt.andre.projecto.Service.Interfaces.IServerService;
import pt.andre.projecto.Service.ServerService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public IServerService getServerService(){
        return new ServerService();
    }

    @Bean
    public IAPIService getAPIService(){
        return new APIService();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) //By default is singleton, but can be request(To be request we have to change the component scope or create a proxy on this bean) or prototype
    public IDatabase createDatabase(){
        return System.getenv("MONGO_USER") == null ? new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), "Projecto") : new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), System.getenv("MONGO_DATABASE"), System.getenv("MONGO_USER"), System.getenv("MONGO_PASSWORD"));
    }
}
