package com.runzii.lib.widgets;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by runzii on 16-7-16.
 */
public class ObservableString extends BaseObservable implements Parcelable, Serializable {
    static final long serialVersionUID = 1L;
    private String mValue;

    public ObservableString(String mValue) {
        this.mValue = mValue;
    }

    public String get() {
        return mValue;
    }

    public void set(String value) {
        if (!value.equals(mValue)) {
            mValue = value;
            notifyChange();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
    }

    public static final Parcelable.Creator<ObservableString> CREATOR
            = new Parcelable.Creator<ObservableString>() {

        @Override
        public ObservableString createFromParcel(Parcel source) {
            return new ObservableString(source.readString());
        }

        @Override
        public ObservableString[] newArray(int size) {
            return new ObservableString[size];
        }
    };
}
