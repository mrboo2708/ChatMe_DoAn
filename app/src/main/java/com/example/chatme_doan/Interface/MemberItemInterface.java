package com.example.chatme_doan.Interface;

import androidx.annotation.NonNull;

import com.example.chatme_doan.UserModel;

public interface MemberItemInterface {

    void onMemberClick(@NonNull UserModel userModel, int position);
}
