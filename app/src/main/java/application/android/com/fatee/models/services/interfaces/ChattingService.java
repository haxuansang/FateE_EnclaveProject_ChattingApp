package application.android.com.fatee.models.services.interfaces;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.QuickBloxResponse;

public interface ChattingService {
    void getQuickBloxIdFromServer(Callback<QuickBloxResponse> callback);
}
