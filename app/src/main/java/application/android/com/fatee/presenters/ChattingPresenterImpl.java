package application.android.com.fatee.presenters;

import application.android.com.fatee.models.Callback;
import application.android.com.fatee.models.entities.QuickBloxResponse;
import application.android.com.fatee.models.services.ChattingServiceImpl;
import application.android.com.fatee.models.services.interfaces.ChattingService;
import application.android.com.fatee.presenters.interfaces.ChattingPresenter;
import application.android.com.fatee.views.interfaces.ChattingView;

public class ChattingPresenterImpl implements ChattingPresenter {
    private ChattingService chattingService;
    private ChattingView chattingView;

    public ChattingPresenterImpl(ChattingView chattingView) {
        chattingService = new ChattingServiceImpl();
        this.chattingView = chattingView;
    }
    @Override
    public void getQuickBloxIdFromServer() {
        chattingService.getQuickBloxIdFromServer(new Callback<QuickBloxResponse>() {
            @Override
            public void onSuccess(QuickBloxResponse result) {
                chattingView.joinUserIntoRoom(result);
            }

            @Override
            public void onError(Throwable what) {

            }
        });
    }
}
