package pt.andre.projecto.Model.Database;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class ParentDatabase {

    abstract void push(String user, String data);
    abstract void pull(String user);
    abstract void authenticate(String user, String pass);
    abstract boolean createUser(String user, String pass);

    String wordHashing(String word){
        throw new NotImplementedException();
    }

    String wordDeHashing(String word){
        throw new NotImplementedException();
    }
}
