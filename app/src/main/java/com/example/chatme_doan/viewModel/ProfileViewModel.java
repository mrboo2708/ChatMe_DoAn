package com.example.chatme_doan.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatme_doan.repository.ProfileRepository;
import com.example.chatme_doan.UserModel;

public class ProfileViewModel extends ViewModel {

    ProfileRepository profileRepository = ProfileRepository.getInstance();

    public LiveData<UserModel> getUser() {
        return profileRepository.getUser();
    }

    public void editImage(String uri) {
        profileRepository.editImage(uri);
    }

    public void editStatus(String status) {
        profileRepository.editStatus(status);
    }

    public void edtUsername(String name) {

        profileRepository.editUsername(name);
    }

}
