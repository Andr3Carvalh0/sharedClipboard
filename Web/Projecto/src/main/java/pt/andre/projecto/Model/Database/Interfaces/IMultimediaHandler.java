package pt.andre.projecto.Model.Database.Interfaces;

import pt.andre.projecto.Output.Interfaces.DatabaseResponse;

public interface IMultimediaHandler {
    String store(String sub, byte[] file, String filename);
    DatabaseResponse pull(String encryptedSUB, String sub, String file);

}
