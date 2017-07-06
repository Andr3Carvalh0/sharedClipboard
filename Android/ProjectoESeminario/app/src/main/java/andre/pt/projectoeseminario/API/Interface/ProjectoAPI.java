package andre.pt.projectoeseminario.API.Interface;

import andre.pt.projectoeseminario.API.Remote.RetrofitClient;

public class ProjectoAPI {

    private static String main = "https://projecto1617.herokuapp.com/api/";

    public static IAPI getAPI() {
        return RetrofitClient.getClient(main).create(IAPI.class);
    }

}
