package application.android.com.fatee.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import application.android.com.fatee.R;
import application.android.com.fatee.models.quickbloxholder.QBFileHolder;
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
import application.android.com.fatee.presenters.SetImageToImageView;
import application.android.com.fatee.views.MainActivity;
import application.android.com.fatee.views.PrivateChat;
import application.android.com.fatee.views.fragments.RoomFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<QBChatMessage> mMessageList;

    Integer userID;
    int countOfUsers;
    public static int currentUserBuddy=0;


    public ChatMessageAdapter(Context context, List<QBChatMessage> messageList,int countOfUser) {
        mContext = context;
        mMessageList = messageList;
        userID = QBChatService.getInstance().getUser().getId();
        countOfUsers=countOfUser;
        if (countOfUser==0)
        {
            PrivateChat.chatView.setVisibility(View.VISIBLE);
            PrivateChat.progressBar.setVisibility(View.GONE);
        }


    }



    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {


        if (mMessageList.get(position).getSenderId().equals(userID)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_send_messages, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_receive_messages, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(mMessageList.get(position));
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(mMessageList.get(position));

        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        BubbleTextView bubbleTextView;

        SentMessageHolder(View itemView) {
            super(itemView);

            bubbleTextView = (BubbleTextView) itemView.findViewById(R.id.idmessend);
        }

        void bind(QBChatMessage message) {
            bubbleTextView.setText(message.getBody());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder implements   View.OnCreateContextMenuListener, PopupMenu.OnMenuItemClickListener {
        BubbleTextView bubbleTextView;
        CircleImageView userImage;
        TextView timeMessage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            bubbleTextView = (BubbleTextView) itemView.findViewById(R.id.idmesreceive);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_image);


        }

        void bind(final QBChatMessage message) {

            bubbleTextView.setText(message.getBody());
            if(countOfUsers>1)
            {

                userImage.setOnCreateContextMenuListener(this);

            }
            userImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    currentUserBuddy= message.getSenderId();
                    return false;
                }
            });

            if(QBFileHolder.getInstance().getFileUserById(message.getSenderId())==null)

            {
                QBContent.getFile( QBUserHolder.getInstance().getUserById(message.getSenderId()).getFileId()).performAsync(new QBEntityCallback<QBFile>() {
                    @Override
                    public void onSuccess(QBFile qbFile, Bundle bundle) {
                        if(countOfUsers>1)
                            SetImageToImageView.loadImageToImageView(mContext,qbFile.getPublicUrl(),userImage,message.getSenderId(), RoomFragment.progressBar,RoomFragment.chatView,countOfUsers);
                        else
                        {
                            Picasso.with(mContext).load(qbFile.getPublicUrl()).into(userImage);
                            Picasso.with(mContext).load(qbFile.getPublicUrl()).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    QBFileHolder.getInstance().putQBFileUser(message.getSenderId(),bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                            PrivateChat.progressBar.setVisibility(View.GONE);
                            PrivateChat.chatView.setVisibility(View.VISIBLE);
                        }

                    }
                    @Override
                    public void onError(QBResponseException e) {

                    }
                });

            }
            else
                userImage.setImageBitmap(QBFileHolder.getInstance().getFileUserById(message.getSenderId()));

        }



        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.getMenuInflater().inflate(R.menu.detail_user_group, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }



        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId())
            {
                case R.id.user_profile:
                    break;
                case R.id.user_make_buddy:
                    createPrivateChat();
                    break;

            }
            return false;
        }
    }

    private void createPrivateChat() {

        QBChatDialog qbChatDialogPrivate = DialogUtils.buildPrivateDialog(currentUserBuddy);
        QBRestChatService.createChatDialog(qbChatDialogPrivate).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                Intent intent = new Intent(mContext,PrivateChat.class);
                intent.putExtra("private_dialog",qbChatDialog);
                intent.putExtra("buddy_name",QBUserHolder.getInstance().getUserById(ChatDialogsAdapter.getUserIDBuddy(qbChatDialog)).getFullName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(mContext, "You already made friend each other", Toast.LENGTH_SHORT).show();

            }
        });
    }

}