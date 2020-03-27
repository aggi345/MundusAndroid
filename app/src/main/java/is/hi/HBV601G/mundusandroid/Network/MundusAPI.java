package is.hi.HBV601G.mundusandroid.Network;

import android.graphics.Bitmap;
import android.util.Pair;

import java.io.ByteArrayInputStream;
import java.util.Set;

import is.hi.HBV601G.mundusandroid.Entities.Account;
import is.hi.HBV601G.mundusandroid.Entities.Child;
import is.hi.HBV601G.mundusandroid.Entities.ChildRewardPair;
import is.hi.HBV601G.mundusandroid.Entities.Parent;
import is.hi.HBV601G.mundusandroid.Entities.Quest;
import is.hi.HBV601G.mundusandroid.Entities.Reward;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @FormUrlEncoded
    @POST("getSmallChildById")
    Call<Child> getSmallChildById(@Field("childId") long childId);

    @GET("getChildren")
    Call<Set<Child>> getChildren();

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

    @GET("getChildRewardPair")
    Call<Set<ChildRewardPair>> getChildRewardPair();


    @GET("getAvailableQuestsOfParent")
    Call<Set<Quest>> getAvailableQuestsOfParent();

    @GET("getInProgressQuestsOfParent")
    Call<Set<Quest>> getInProgressQuestsOfParent();

    @GET("getInFinishedQuestsOfParent")
    Call<Set<Quest>> getInFinishedQuestsOfParent();

    @GET("getAllQuestsOfParent")
    Call<Set<Quest>> getAllQuestsOfParent();

    @GET("getAssignedQuestsOfChild")
    Call<Set<Quest>> getAssignedQuestsOfChild();

    @GET("getDoneQuestsOfChild")
    Call<Set<Quest>> getDoneQuestsOfChild();

    @FormUrlEncoded
    @POST("deletereward")
    Call<ResponseBody> deleteReward(@Field("id") long rewardId);

    @FormUrlEncoded
    @POST("purchaseReward")
    Call<Boolean> purchaseReward(@Field("id") long rewardId);

    @FormUrlEncoded
    @POST("grantReward")
    Call<Boolean> grantReward(@Field("rewardId") long rewardId, @Field("childId") long childId);

    @FormUrlEncoded
    @POST("deleteQuest")
    Call<ResponseBody> deleteQuest(@Field("questId") long questId);

    @FormUrlEncoded
    @POST("assignQuestParent")
    Call<ResponseBody> assignQuestParent(@Field("childId") long childId, @Field("questId") long questId);

    @FormUrlEncoded
    @POST("assignQuest")
    Call<ResponseBody> assignQuest(@Field("questId") long questId);

    @FormUrlEncoded
    @POST("unassignQuest")
    Call<ResponseBody> unassignQuest(@Field("questId") long questId);

    @FormUrlEncoded
    @POST("markQuestAsConfirmed")
    Call<ResponseBody> markQuestAsConfirmed(@Field("questId") long questId);

    @FormUrlEncoded
    @POST("markQuestAsNotDone")
    Call<ResponseBody> markQuestAsNotDone(@Field("questId") long questId);

    @FormUrlEncoded
    @POST("markQuestAsDone")
    Call<ResponseBody> markQuestAsDone(@Field("questId") long questId);


    //Login
    @GET("getLoginStatus")
    Call<Integer> getLoginStatus();



    //Profile
    @FormUrlEncoded
    @POST("addCoinsToChild")
    Call<ResponseBody> addCoinsToChild(@Field("childId") long childId, @Field("amount") long amount);

    @FormUrlEncoded
    @POST("addXpToChild")
    Call<ResponseBody> addXpToChild(@Field("childId") long childId, @Field("amount") long amount);

    @POST("updateBasicChildInfo")
    Call<ResponseBody> updateBasicChildInfo(@Body Child child);

    @POST("createChild")
    Call<ResponseBody> createChild(@Body Child child);

    @FormUrlEncoded
    @POST("removeChild")
    Call<ResponseBody> removeChild(@Field("childId") long childId);

    @GET("getBasicAccountInfo")
    Call<Account> getBasicAccountInfo();

    @POST("updateAccountInfo")
    Call<ResponseBody> updateAccountInfo(@Body Account account);

    @FormUrlEncoded
    @POST("changeAccountPassword")
    Call<ResponseBody> changeAccountPassword(@Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("changePinOnParent")
    Call<ResponseBody> changePinOnParent(@Field("currentPin") String currentPin, @Field("newPin") String newPin);

   //@FormUrlEncoded
    //@POST("uploadImage/{name}")
    //Call<ResponseBody> uploadImage(@Body ByteArrayInputStream image, @Path("name") String name);

    //@POST("uploadImage/{name}")
   // Call<ResponseBody> uploadImage(@Body byte[] byteImg, @Path("name") String name);

    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part filePart);//, @Part("name") RequestBody requestBody);








}
