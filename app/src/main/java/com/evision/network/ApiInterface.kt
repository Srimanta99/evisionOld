

import com.evision.CartManage.Pojo.InitDepositResponse
import com.google.gson.JsonObject

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


   // @Headers("Content-Type: application/json")
    /*@POST(NetworkUtility.LOG_IN)
    fun callLogInApi(@Field("user_email") email:String,
                     @Field("password") pass:String,
                     @Field("identification_id_token") identification_id_token:String,
                     @Field("device_model") device_model:String,
                     @Field("device_os") device_os:String): Call<LoginResponse>
*/



   // @Headers("Content-Type: application/json")
   // @POST(NetworkUtility.FAULTSTATUSMESSAGE_CHANGE)
   // fun callfaultstatusmessagechange(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultStatusMessageChange>

   // @Headers("Content-Type: application/json")
    @POST("/api/init-deposit")
    fun callApiinitdeposit(@Header("Content-Type") token:String,@Header("Authorization") site_id:String, @Body body: JsonObject): Call<InitDepositResponse>


}