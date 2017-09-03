package andre.pt.projectoeseminario.Controller.API.Interface;

import andre.pt.projectoeseminario.Controller.API.RetrofitClientFactory;

public class ProjectoAPI {

    private static String main = "https://projecto1617.herokuapp.com/api/";

    public static IAPI getAPI() {
        return RetrofitClientFactory.getClient(main).create(IAPI.class);
    }

}
