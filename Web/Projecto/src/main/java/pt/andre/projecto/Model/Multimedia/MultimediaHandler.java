package pt.andre.projecto.Model.Multimedia;

import com.google.api.client.util.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import pt.andre.projecto.Model.Database.Utils.DatabaseResponseMultimedia;
import pt.andre.projecto.Model.Database.Utils.Interfaces.DatabaseResponse;
import pt.andre.projecto.Model.Database.Utils.ResponseFormater;

import java.io.*;

public class MultimediaHandler implements IMultimediaHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TAG = "Portugal: Multimedia Handler ";

    private final String PARENT_DIRECTORY = "files/";
    private final String FILE_LOCATION_API = "/api/MIME/";

    @Override
    public String store(String sub, byte[] file, String filename) {
        try {
            logger.info(TAG + "Attempting to create directories");

            String encodedSUB = Base64.encodeBase64String(sub.getBytes());

            // Create the directory structure if it isn't already created.
            File outFile = new File(PARENT_DIRECTORY + encodedSUB + "/");
            outFile.mkdirs();

            logger.info(TAG + "Directories created!");
            // Writes the file
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(PARENT_DIRECTORY + encodedSUB + "/" + filename)));
            stream.write(file);
            stream.close();
            logger.info(TAG + "File created!");
            return System.getenv("SERVER") + FILE_LOCATION_API + encodedSUB + "/" + filename;
        } catch (Exception e) {
            logger.error(TAG + e.getMessage());
            return null;
        }

    }

    @Override
    public DatabaseResponse pull(String encryptedSUB, String sub, String file) {
        assert encryptedSUB != null;
        if(!encryptedSUB.equals(Base64.encodeBase64String(sub.getBytes())))
            return ResponseFormater.createResponseMultimedia(ResponseFormater.NOT_PERMITTED);

        try {
            final byte[] ret = IOUtils.toByteArray(new FileInputStream(new File(PARENT_DIRECTORY + encryptedSUB + "/" + file)));
            return ResponseFormater.displayInformation(ret);

        } catch (IOException ex) {
            logger.error(TAG, "Cannot read file!");
        }

        return ResponseFormater.createResponseMultimedia(ResponseFormater.EXCEPTION);
    }
}
