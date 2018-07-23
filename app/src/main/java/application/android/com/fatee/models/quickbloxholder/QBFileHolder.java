package application.android.com.fatee.models.quickbloxholder;

import android.graphics.Bitmap;

import java.util.Hashtable;

public class QBFileHolder {
    private static QBFileHolder instance=null;
    public Hashtable<Integer,Bitmap> arrayImageUser;
    public static synchronized QBFileHolder getInstance()
    {
        if(instance==null)
            instance = new QBFileHolder();
        return instance;
    }
    private QBFileHolder()
    {
        arrayImageUser = new Hashtable<>();

    }
    public void putQBFileUser(int idUser,Bitmap bitmap)
    {
        arrayImageUser.put(idUser,bitmap);
    }

    public Bitmap getFileUserById(int i)
    {
       return arrayImageUser.get(i);
    }
    public int sizeOfImages()
    {
        return arrayImageUser.size();
    }


}
