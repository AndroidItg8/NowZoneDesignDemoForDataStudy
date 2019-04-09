package itg8.com.nowzonedesigndemo.common;

import java.util.List;

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


    @POST("Bleonemasters/add")
    Observable<ResponseBody> storePressure(@Body List<DataModelPressure> dataModelPressures);

//    User[username]:8087890075
//    User[first_name]:User name full
//    User[age]:43
//    User[gender]:M
//    User[weight]:78
//    User[password]:123456
//    User[user_group_id]:4
//    User[height]:173

    @POST("appregister")
    @FormUrlEncoded
    Observable<ResponseBody> storeProfile(@Field("User[username]") String mobile,
                                          @Field("User[first_name]") String username,
                                          @Field("User[age]") String age,
                                          @Field("User[gender]") String gender,
                                          @Field("User[weight]") String weight,
                                          @Field("User[password]") String password,
                                          @Field("User[user_group_id]") String userGroupId,
                                          @Field("User[height]") String height);
}
