package application.android.com.fatee.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import application.android.com.fatee.views.adapters.ChatMessageAdapter;

public class PrivateChat extends AppCompatActivity implements QBChatDialogMessageListener {
    QBChatDialog qbChatDialog;
    RecyclerView lvChatting;
    ImageButton btnsendMessage;
    TextView contentMessage;
    ChatMessageAdapter adapter;
    List<QBChatMessage> qbChatMessagesArray;
    String nameOfUser;
    public static RelativeLayout progressBar;
    public static RelativeLayout chatView;
    public static int countUsers=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
        qbChatMessagesArray=new ArrayList<QBChatMessage>();
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
                qbChatMessagesArray.add(chatMessage);
                adapter.notifyDataSetChanged();
                contentMessage.setText("");
                contentMessage.setFocusable(true);
                scroolSmooth();
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    private void initView() {
        lvChatting = (RecyclerView) findViewById(R.id.list_chat_messages);
        btnsendMessage = (ImageButton) findViewById(R.id.sendMessage);
        contentMessage =(EditText)findViewById(R.id.content_message);
        progressBar= (RelativeLayout)findViewById(R.id.progress_download);
        chatView= (RelativeLayout)findViewById(R.id.relative_layout_chatting);

    }
    private void retrieveMessages() {

        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder();
        qbMessageGetBuilder.setLimit(200);
        if(qbChatDialog!=null)
        {
            QBRestChatService.getDialogMessages(qbChatDialog,qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    for (QBChatMessage  qbChatMessage: qbChatMessages
                            ) {
                        qbChatMessagesArray.add(qbChatMessage);
                        if(qbChatMessage.getRecipientId()==QBChatService.getInstance().getUser().getId())
                            countUsers++;

                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PrivateChat.this);
                    if (countUsers==0)
                    adapter = new ChatMessageAdapter(PrivateChat.this,qbChatMessagesArray,0);
                      else
                        adapter = new ChatMessageAdapter(PrivateChat.this,qbChatMessagesArray,1);

                    lvChatting.setLayoutManager(layoutManager);
                    lvChatting.setAdapter(adapter);
                    scroolSmooth();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("Error",e.getMessage());
                }
            });
        }
        else
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();


    }
    private void initChatDilalog() {
        qbChatDialog=(QBChatDialog)getIntent().getSerializableExtra("private_dialog");
        nameOfUser= (String) getIntent().getSerializableExtra("buddy_name");
        getSupportActionBar().setTitle(nameOfUser);

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


        qbChatDialog.addMessageListener(this);
    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        qbChatMessagesArray.add(qbChatMessage);
        adapter.notifyDataSetChanged();
        scroolSmooth();
    }

    private void scroolSmooth() {
        if(adapter.getItemCount()>0)
            lvChatting.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.private_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.delete_setting_private:
                deleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDialog() {
            QBRestChatService.deleteDialog(qbChatDialog.getDialogId(),true).performAsync(new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Toast.makeText(PrivateChat.this, "Delete the dialogue successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });

    }
}
