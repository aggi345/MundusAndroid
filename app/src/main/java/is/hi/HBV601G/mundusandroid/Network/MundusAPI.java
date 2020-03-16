package is.hi.HBV601G.mundusandroid.Network;

import android.util.Pair;

import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MundusAPI {

    @FormUrlEncoded
    @POST("login")
    Call<Integer> login(@Field("email") String email, @Field("password") String password);

    //@Headers("Cookie: JSESSIONID = E002569DE8BD39E03A8B1B522C320DDA")
    @GET("login")
    Call<String> login2();

    @POST("signup")
    Call<ResponseBody> signup(@Body Account account);

    @GET("getparent")
    Call<Parent> getParent();

    @FormUrlEncoded
    @POST("pin-page-auth")
    Call<ResponseBody> PinAuth(@Field("id") long personId, @Field("pin") String pin);

    @GET("getPersonType")
    Call<Integer> getPersonType();

    @GET("getSmallChild")
    Call<Child> getSmallChild();

    @GET("getSmallParent")
    Call<Parent> getSmallParent();

    @GET("getSmallChildrenOfParent")
    Call<Set<Child>> getSmallChildrenOfParent();

    @POST("createQuest")
    Call<ResponseBody> createQuest(@Body Quest quest);

    @POST("createReward")
    Call<ResponseBody> createReward(@Body Reward reward);

    @GET("getRewardsOfParent")
    Call<Set<Reward>> getRewardsOfParent();

    @GET("getPurchasedRewardOfParent")
    Call<Set<Pair<Child, Reward>>> getPurchasedRewardOfParent();

    @GET("getRewardsOfChild")
    Call<Set<Reward>> getRewardsOfChild();

    @GET("getAvailableRewardsOfChild")
    Call<Set<Reward>> getAvailableRewardsOfChild();




    @GET("getAvailableQuestsOfParent")
    Call<Set<Quest>> getAvailableQuestsOfParent();

    @GET("getInProgressQuestsOfParent")
    Call<Set<Quest>> getInProgressQuestsOfParent();

    @GET("getInFinishedQuestsOfParent")
    Call<Set<Quest>> getInFinishedQuestsOfParent();

    @GET("getAlldQuestsOfParent")
    Call<Set<Quest>> getAlldQuestsOfParent();

    @GET("getAssignedQuestsOfChild")
    Call<Set<Quest>> getAssignedQuestsOfChild();

    @GET("getDoneQuestsOfChild")
    Call<Set<Quest>> getDoneQuestsOfChild();







}
