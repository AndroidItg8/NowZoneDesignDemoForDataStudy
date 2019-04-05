package itg8.com.nowzonedesigndemo.common;

import io.reactivex.Observable;
import itg8.com.nowzonedesigndemo.utility.model.breath.BreathingModel;
import itg8.com.nowzonedesigndemo.utility.model.step.StepsModel;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetroController {

    @POST
    Observable<ResponseBody> storeBreathing(@Url String url, @Body BreathingModel model);

    @GET
    Observable<BreathingModel> getBreathing(@Url String url);

    @POST
    Observable<ResponseBody> storeSteps(@Url String url, @Body StepsModel model);

    @GET
    Observable<StepsModel> getSteps(@Url String url);


    @GET
    Observable<ResponseBody> checkLogin(@Url String url, @Query("username") String username, @Query("password") String password);


}
