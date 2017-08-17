package pt.andre.projecto.Controllers.Interfaces;

public interface INotify {
    boolean notify(String messageJSON, String... devices);

}
