package andre.pt.projectoeseminario.Controller.API;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Factory class to return an Retrofit object
 */
public class RetrofitClientFactory {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                                   .baseUrl(baseUrl)
                                   .addConverterFactory(ScalarsConverterFactory.create())
                                   .build();
        }

        return retrofit;
    }

}
