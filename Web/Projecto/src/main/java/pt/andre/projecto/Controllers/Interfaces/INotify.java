package pt.andre.projecto.Controllers.Interfaces;

public interface INotify {
    void notify(String sub, String messageJSON, String... devices);

}
