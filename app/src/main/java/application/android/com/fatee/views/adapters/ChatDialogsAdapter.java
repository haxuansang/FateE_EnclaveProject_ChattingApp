package application.android.com.fatee.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import application.android.com.fatee.R;
import application.android.com.fatee.models.quickbloxholder.QBFileHolder;
import application.android.com.fatee.models.quickbloxholder.QBUserHolder;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDialogsAdapter extends BaseAdapter {
    ArrayList<QBChatDialog> qbChatDialogs;
    Context context;
    int idUserBuddy;

    public ChatDialogsAdapter(ArrayList<QBChatDialog> qbChatDialogs, Context context) {
        this.qbChatDialogs = qbChatDialogs;
        this.context = context;

    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatDialogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private int getUserIDBuddy(QBChatDialog qbChatDialog)
    {
        int currentId=0;
        for (int i : qbChatDialog.getOccupants())
        {
            if (i!= QBChatService.getInstance().getUser().getId())
            {
                currentId=i;
            }
        }
        return  currentId;

    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chatdialog, viewGroup, false);
            TextView tvTitle, tvMessage;
            final CircleImageView buddyImage;
            tvTitle = (TextView) view.findViewById(R.id.inflateTitle);
            tvMessage = (TextView) view.findViewById(R.id.inflateMessage);
            buddyImage = (CircleImageView) view.findViewById(R.id.user_image_buddy);
            idUserBuddy=getUserIDBuddy(qbChatDialogs.get(i));
            tvTitle.setText(QBUserHolder.getInstance().getUserById(idUserBuddy).getFullName());
            tvMessage.setText(qbChatDialogs.get(i).getLastMessage());

            if (idUserBuddy != 0) {
                if (QBFileHolder.getInstance().getFileUserById(idUserBuddy) != null)
                    buddyImage.setImageBitmap(QBFileHolder.getInstance().getFileUserById(idUserBuddy));
                else {
                    if (QBUserHolder.getInstance().getUserById(idUserBuddy) != null) {
                        QBUser qbUser = QBUserHolder.getInstance().getUserById(idUserBuddy);
                        QBContent.getFile(qbUser.getFileId()).performAsync(new QBEntityCallback<QBFile>() {
                            @Override
                            public void onSuccess(QBFile qbFile, Bundle bundle) {
                                Picasso.with(context).load(qbFile.getPublicUrl()).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        QBFileHolder.getInstance().putQBFileUser(idUserBuddy, bitmap);

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                                Picasso.with(context).load(qbFile.getPublicUrl()).into(buddyImage);

                            }

                            @Override
                            public void onError(QBResponseException e) {

                            }
                        });

                    }
                }
            }
        }
        return view;
    }

}
