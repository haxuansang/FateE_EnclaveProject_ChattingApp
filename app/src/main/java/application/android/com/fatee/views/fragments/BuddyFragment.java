package application.android.com.fatee.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;

import org.jivesoftware.smackx.carbons.packet.CarbonExtension;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
import application.android.com.fatee.views.MainActivity;
import application.android.com.fatee.views.PrivateChat;
import application.android.com.fatee.views.adapters.ChatDialogsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuddyFragment extends Fragment {
    View view;
    ListView lvChatting;
    int userIDBuddy;
    private static BuddyFragment instance;
    public BuddyFragment() {
        // Required empty public constructor
    }

    public static BuddyFragment getInstance() {
        if(instance == null)
            instance = new BuddyFragment();
        return instance;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view=inflater.inflate(R.layout.fragment_buddy, container, false);
        lvChatting=(ListView)view.findViewById(R.id.lvChatting);
        loadChatDialogs();
        lvChatting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QBChatDialog qbChatDialog = (QBChatDialog) lvChatting.getAdapter().getItem(i);
                Intent intent1 = new Intent(getActivity(), PrivateChat.class);
                intent1.putExtra("private_dialog",qbChatDialog);
                intent1.putExtra("buddy_name", QBUserHolder.getInstance().getUserById(ChatDialogsAdapter.getUserIDBuddy(qbChatDialog)).getFullName());
                startActivity(intent1);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChatDialogs();
    }

    private  void loadChatDialogs()
    {
        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        requestGetBuilder.setLimit(500);
        QBRestChatService.getChatDialogs(QBDialogType.PRIVATE,requestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                ChatDialogsAdapter chatDialogsAdapter = new ChatDialogsAdapter(qbChatDialogs,getActivity().getBaseContext());
                lvChatting.setAdapter(chatDialogsAdapter);
                chatDialogsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "onError: "+e.getMessage() );
            }
        });
    }

}
