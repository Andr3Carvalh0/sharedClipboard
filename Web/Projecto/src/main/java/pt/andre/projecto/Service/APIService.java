package pt.andre.projecto.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Controllers.URIs.FirebaseServer;
import pt.andre.projecto.Model.Database.IDatabase;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;
import pt.andre.projecto.Service.Interfaces.IAPIService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class APIService implements IAPIService{

    @Autowired
    private IDatabase database;

    @Autowired
    private FirebaseServer firebaseServer;


    @Override
    public DatabaseResponse push(long token, String data) {
        return this.push(token, data, false);
    }

    private String storeFile(long token, MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                File outFile = new File("build/resources/main/static/content/" + token + "/");
                outFile.mkdirs();


                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("build/resources/main/static/content/" + token + "/" + file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                return System.getenv("SERVER") + "/content/" + token + "/" + file.getOriginalFilename();
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public DatabaseResponse push(long token, MultipartFile data) {
        String result = storeFile(token, data);

        if(result == null)
            return ResponseFormater.createResponse(ResponseFormater.EXCEPTION);

        return database.push(token, result, true);
    }

    private DatabaseResponse push(long token, String data, boolean isMIME) {
        DatabaseResponse push = database.push(token, data, isMIME);

        firebaseServer.notify(data, isMIME, database.getDevices(token));
        return push;
    }

    @Override
    public DatabaseResponse registerAndroidDevice(long token, String firebaseID) {
        return database.registerAndroidDevice(token, firebaseID);
    }

    @Override
    public DatabaseResponse pull(long token) {
        return database.pull(token);
    }

    @Override
    public DatabaseResponse createAccount(String account, String password) {
        return database.createAccount(account, password);
    }

    @Override
    public DatabaseResponse authenticate(String account, String password) {
        return database.authenticate(account, password);
    }
}
