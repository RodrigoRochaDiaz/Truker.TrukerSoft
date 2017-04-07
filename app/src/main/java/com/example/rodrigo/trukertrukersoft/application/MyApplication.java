package com.example.rodrigo.trukertrukersoft.application;

import android.app.Application;
import com.example.rodrigo.trukertrukersoft.models.User;
import com.example.rodrigo.trukertrukersoft.models.Person;
import com.example.rodrigo.trukertrukersoft.models.Car;
import java.util.concurrent.atomic.AtomicInteger;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Rodrigo on 29/03/2017.
 */

public class MyApplication extends Application {
    public static AtomicInteger UserId = new AtomicInteger();
    //public static AtomicInteger PersonId = new AtomicInteger();
    public static AtomicInteger CarId = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setupRealmConfiguration();
        Realm realm = Realm.getDefaultInstance();
        UserId = getIdByTable(realm, User.class);
        //PersonId = getIdByTable(realm, Person.class);
        CarId = getIdByTable(realm, Car.class);
        realm.close();
    }

    private void setupRealmConfiguration(){
        RealmConfiguration configuration = new RealmConfiguration
                .Builder(getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> tClass){
        RealmResults<T> results = realm.where(tClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }


}
