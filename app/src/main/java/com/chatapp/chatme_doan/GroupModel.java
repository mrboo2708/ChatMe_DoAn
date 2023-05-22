package com.chatapp.chatme_doan;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chatapp.chatme_doan.model.GroupLastMessageModel;
import com.chatapp.chatme_doan.model.GroupMemberModel;

import java.util.List;

public class GroupModel implements Parcelable {

    @Nullable
    public String id, adminId, adminName, createdAt, image, name;
    @Nullable
    public List<GroupMemberModel> members;
    @Nullable
    public GroupLastMessageModel lastMessageModel;
    public boolean isAdmin;

    public GroupModel() {
    }

    protected GroupModel(@NonNull Parcel in) {
        id = in.readString();
        adminId = in.readString();
        adminName = in.readString();
        createdAt = in.readString();
        image = in.readString();
        name = in.readString();
        members = in.createTypedArrayList(GroupMemberModel.CREATOR);
        lastMessageModel = in.readParcelable(GroupLastMessageModel.class.getClassLoader());
        isAdmin = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adminId);
        dest.writeString(adminName);
        dest.writeString(createdAt);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeTypedList(members);
        dest.writeParcelable(lastMessageModel, flags);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupModel> CREATOR = new Creator<GroupModel>() {
        @Override
        public GroupModel createFromParcel(Parcel in) {
            return new GroupModel(in);
        }

        @Override
        public GroupModel[] newArray(int size) {
            return new GroupModel[size];
        }
    };
}
