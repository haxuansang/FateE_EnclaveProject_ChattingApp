package application.android.com.fatee.models.services;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.api.APIChatting;
import application.android.com.fatee.models.api.APIClient;
import application.android.com.fatee.models.entities.QuickBloxResponse;
import application.android.com.fatee.models.services.interfaces.ChattingService;
import retrofit2.Call;
import retrofit2.Response;

public class ChattingServiceImpl implements ChattingService{
    @Override
    public void getQuickBloxIdFromServer(final Callback<QuickBloxResponse> callback) {
        APIChatting apiChatting = APIClient.getClient().create(APIChatting.class);
        Call<QuickBloxResponse> call = apiChatting.getQuickBloxFromServer();
        call.enqueue(new retrofit2.Callback<QuickBloxResponse>() {
            @Override
            public void onResponse(Call<QuickBloxResponse> call, Response<QuickBloxResponse> response) {
//                if (response.code() == 200) {
                    callback.onSuccess(new QuickBloxResponse("5b486a4fa28f9a13f725ebf3"));
//                }
            }

            @Override
            public void onFailure(Call<QuickBloxResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
