package pt.andre.projecto.Service.REST;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Service.Interfaces.IGoogleService;

public class ParentService {

    @Autowired
    IGoogleService cache;

    public String handleAuthentication(String token){
        return cache.getUser(token);
    }

}
