package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import pt.andre.projecto.Service.Interfaces.ICacheService;

public class ParentService {

    @Autowired
    ICacheService cache;

    public String handleAuthentication(String token){
        return cache.getUser(token);
    }

}
