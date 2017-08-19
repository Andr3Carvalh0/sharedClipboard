package pt.andre.projecto;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import pt.andre.projecto.Controllers.Interfaces.IAPI;
import pt.andre.projecto.Controllers.URIs.API;
import pt.andre.projecto.Controllers.URIs.FirebaseService;
import pt.andre.projecto.Controllers.URIs.WebSocketService;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.MongoDB;
import pt.andre.projecto.Model.Multimedia.IMultimediaHandler;
import pt.andre.projecto.Model.Multimedia.MultimediaHandler;
import pt.andre.projecto.Service.APIService;
import pt.andre.projecto.Service.CacheService;
import pt.andre.projecto.Service.Interfaces.IAPIService;
import pt.andre.projecto.Service.Interfaces.ICacheService;
import pt.andre.projecto.Service.Interfaces.IServerService;
import pt.andre.projecto.Service.ServerService;
import pt.andre.projecto.WebSockets.WebSocketHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class, MongoAutoConfiguration.class})
@EnableCaching
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
    public FirebaseService getFirebaseService(){
        return new FirebaseService();
    }

    @Bean
    public WebSocketService getWebsocketService(){
        return new WebSocketService();
    }

    @Bean
    public ICacheService getCache(){
        return new CacheService();
    }

    @Bean
    public IMultimediaHandler getMultimediaHandler(){
        return new MultimediaHandler();
    }

    @Bean
    public IAPIService getAPIService(){
        return new APIService();
    }

    @Bean
    public IDatabase createDatabase(){
        return System.getenv("MONGO_USER") == null ? new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), "Projecto") : new MongoDB(System.getenv("MONGO_HOST"), System.getenv("MONGO_PORT"), System.getenv("MONGO_DATABASE"), System.getenv("MONGO_USER"), System.getenv("MONGO_PASSWORD"));
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}
