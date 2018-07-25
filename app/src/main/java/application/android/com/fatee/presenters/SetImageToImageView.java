package application.android.com.fatee.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import application.android.com.fatee.models.quickbloxholder.QBFileHolder;
import application.android.com.fatee.views.fragments.RoomFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetImageToImageView {
    public static Boolean status=false;
    public  static void loadImageToImageView(final Context context, String URL, final CircleImageView userImage, final int idUser, final RelativeLayout progressBar, final  RelativeLayout chatView)
    {
        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                QBFileHolder.getInstance().putQBFileUser(idUser, bitmap);
                    if (QBFileHolder.getInstance().sizeOfImages()==4) {
                        RoomFragment.adapter.notifyDataSetChanged();
                        chatView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(URL).noPlaceholder().into(userImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("Anh ID",""+idUser);
            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(context).load(URL).into(t);


    }
}
