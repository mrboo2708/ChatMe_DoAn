package com.chatapp.chatme_doan.Interface;

import androidx.annotation.NonNull;

import com.chatapp.chatme_doan.UserModel;

public interface MemberItemInterface {

    void onMemberClick(@NonNull UserModel userModel, int position);
}
