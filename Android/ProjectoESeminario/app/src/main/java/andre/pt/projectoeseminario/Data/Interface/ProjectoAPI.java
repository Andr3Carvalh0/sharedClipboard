package andre.pt.projectoeseminario.Data.Interface;

import andre.pt.projectoeseminario.Data.Remote.RetrofitClient;

public class ProjectoAPI {

    private static String main = "http://projecto-testes.herokuapp.com/api/";

    public static IAPI getAPI() {
        return RetrofitClient.getClient(main).create(IAPI.class);
    }

}
