package application.android.com.fatee.models.quickbloxholder;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class QBUserHolder {
    private static QBUserHolder instance;
    public SparseArray<QBUser> qbUserSparseArray;



    public static synchronized QBUserHolder getInstance() {
        if (instance == null)
            instance = new QBUserHolder();
        return instance;

    }

    private QBUserHolder() {
        qbUserSparseArray = new SparseArray<>();

    }

    public List<Integer> getFileIDs()
    {
        List<Integer> fileIdsOfUsers = new ArrayList<>();
        for(int i=0;i<qbUserSparseArray.size();i++)
        {
            fileIdsOfUsers.add(qbUserSparseArray.keyAt(i));

        }
        return fileIdsOfUsers;
    }

    public void putUsers(List<QBUser> qbUsers) {
        for (QBUser qbUser : qbUsers)
            putUser(qbUser);

    }


    public void putUser(QBUser qbUser) {
        qbUserSparseArray.put(qbUser.getId(), qbUser);

    }


    public QBUser getUserById(int i) {
        return qbUserSparseArray.get(i);

    }

    public List<QBUser> getUsersById(List<Integer> ids) {
        List<QBUser> qbUserList = new ArrayList<>();
        for (Integer i : ids) {
            QBUser user = getUserById(i);
            if (user != null) {
                qbUserList.add(user);
            }

        }
        return qbUserList;
    }



}
