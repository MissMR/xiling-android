package com.dodomall.ddmall.module.community;

import android.os.Bundle;
import android.os.Parcelable;

import com.dodomall.ddmall.shared.Constants;

import org.parceler.Parcels;


/**
 * Created by bigbyto on 10/04/2017.
 * data handler factory
 */

public class DataFactory {
    public static <T> T create(Bundle state,Class<T> clazz) {
        T data = null;

        if (state != null) {
            Parcelable parcel = state.getParcelable(Constants.Extras.DATA_HANDLER);
            data = Parcels.unwrap(parcel);
        }

        if (data == null) {
            try {
                data = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static <T> void persistState(Bundle state,T parcel) {
        if (state == null) {
            return;
        }

        Parcelable p = Parcels.wrap(parcel);
        state.putParcelable(Constants.Extras.DATA_HANDLER,p);
    }
}
