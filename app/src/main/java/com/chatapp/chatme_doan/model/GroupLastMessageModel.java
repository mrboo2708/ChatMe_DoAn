package com.chatapp.chatme_doan.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GroupLastMessageModel implements Parcelable {
    @Nullable
    public String senderId, message, date;


    public GroupLastMessageModel() {
    }


    protected GroupLastMessageModel(@NonNull Parcel in) {
        senderId = in.readString();
        message = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(senderId);
        dest.writeString(message);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupLastMessageModel> CREATOR = new Creator<GroupLastMessageModel>() {
        @Override
        public GroupLastMessageModel createFromParcel(Parcel in) {
            return new GroupLastMessageModel(in);
        }

        @Override
        public GroupLastMessageModel[] newArray(int size) {
            return new GroupLastMessageModel[size];
        }
    };
}
