package application.android.com.fatee.views.fragments;


import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.List;

import application.android.com.fatee.R;
import application.android.com.fatee.models.quickbloxholder.QBFileHolder;
import application.android.com.fatee.presenters.ChattingGroupPresenter;
import application.android.com.fatee.views.MainActivity;
import application.android.com.fatee.views.adapters.ChatMessageAdapter;

public class RoomFragment extends Fragment implements QBChatDialogMessageListener {
    QBChatDialog qbChatDialog;
    RecyclerView lvChatting;
    ImageButton btnsendMessage;
    TextView contentMessage;
    View view;
    public static ChatMessageAdapter adapter;
    List<QBChatMessage> qbChatMessagesArray;
    public static RelativeLayout progressBar;
    public static RelativeLayout chatView;

    private static RoomFragment instance;

    public static RoomFragment getInstance() {
        if (instance == null)
            instance = new RoomFragment();
        return instance;
    }

    public RoomFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room, container, false);
        initView();
        qbChatMessagesArray = new ArrayList<QBChatMessage>();
        initChatDilalog();
        retrieveMessages();
        btnsendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(contentMessage.getText().toString());
                chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                chatMessage.setSaveToHistory(true);
                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
                    qbChatMessagesArray.add(chatMessage);
                }
                adapter.notifyDataSetChanged();
                contentMessage.setText("");
                contentMessage.setFocusable(true);
                scroolSmooth();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (QBFileHolder.getInstance().sizeOfImages() > 0) {
            progressBar.setVisibility(View.GONE);
            chatView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);

    }


    private void retrieveMessages() {
        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder();
        qbMessageGetBuilder.setLimit(500);
        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog, qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    for (QBChatMessage qbChatMessage : qbChatMessages) {
                        qbChatMessagesArray.add(qbChatMessage);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                    adapter = new ChatMessageAdapter(getActivity().getBaseContext(), qbChatMessagesArray);
                    lvChatting.setLayoutManager(layoutManager);
                    lvChatting.setAdapter(adapter);
                    scroolSmooth();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("Error", e.getMessage());
                }
            });
        } else
            Toast.makeText(getActivity().getBaseContext(), "You couldn't connect with Group Chat, Please check anyway!!!", Toast.LENGTH_SHORT).show();


    }

    private void scroolSmooth() {
        if (adapter.getItemCount() > 0)
            lvChatting.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void initChatDilalog() {
        qbChatDialog = ChattingGroupPresenter.currentQBChatDialog;
        qbChatDialog.initForChat(QBChatService.getInstance());
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });

        if (!qbChatDialog.getType().equals(QBDialogType.PRIVATE)) {
            DiscussionHistory discussionHistory = new DiscussionHistory();
            discussionHistory.setMaxStanzas(0);
            qbChatDialog.join(discussionHistory, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {

                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        }
        qbChatDialog.addMessageListener(this);
    }

    private void initView() {
        lvChatting = (RecyclerView) view.findViewById(R.id.list_chat_messages);
        btnsendMessage = (ImageButton) view.findViewById(R.id.sendMessage);
        contentMessage = (EditText) view.findViewById(R.id.content_message);
        progressBar = (RelativeLayout) view.findViewById(R.id.progress_download);
        chatView = (RelativeLayout) view.findViewById(R.id.relative_layout_chatting);


    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        qbChatMessagesArray.add(qbChatMessage);
        adapter.notifyDataSetChanged();
        scroolSmooth();
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
        Log.e("ErrorChatMessage", "" + e.getMessage());
    }

}