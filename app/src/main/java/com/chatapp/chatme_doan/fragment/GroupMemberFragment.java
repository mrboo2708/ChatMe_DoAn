package com.chatapp.chatme_doan.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chatapp.chatme_doan.adapter.GroupContactAdapter;
import com.chatapp.chatme_doan.adapter.SelectedContactAdapter;
import com.chatapp.chatme_doan.constants.AllConstants;
import com.chatapp.chatme_doan.Interface.ContactItemInterface;
import com.chatapp.chatme_doan.databinding.FragmentGroupMemberBinding;
import com.chatapp.chatme_doan.model.GroupMemberModel;
import com.chatapp.chatme_doan.GroupModel;
import com.chatapp.chatme_doan.permissions.Permissions;
import com.chatapp.chatme_doan.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GroupMemberFragment extends Fragment implements ContactItemInterface {

    private FragmentGroupMemberBinding binding;
    private Permissions permissions;
    private DatabaseReference databaseReference;
    private GroupContactAdapter groupContactAdapter;
    private SelectedContactAdapter selectedContactAdapter;
    ArrayList<UserModel> appContacts, selectedContacts;
    String userPhoneNumber;
    String imageUri, groupName;
    Uri groupImage;
    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupMemberBinding.inflate(inflater, container, false);
        permissions = new Permissions();
        firebaseAuth = FirebaseAuth.getInstance();
        userPhoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();
        groupContactAdapter = new GroupContactAdapter(this);
        selectedContactAdapter = new SelectedContactAdapter(this, requireContext());

        selectedContacts = new ArrayList<>();


        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("checkBundle",bundle.getString("GroupName"));
            groupName = bundle.getString("GroupName");
            imageUri = bundle.getString("GroupImage");
            groupImage = Uri.parse(imageUri);
        }

        binding.facDone.setOnClickListener(done -> {
            if (selectedContacts.size() > 0) {
                createGroup();
            } else {
                Toast.makeText(requireContext(), "Select the contacts", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewContact.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.selectedRecyclerView.setLayoutManager(new
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.recyclerViewContact.setAdapter(groupContactAdapter);

        binding.selectedRecyclerView.setAdapter(selectedContactAdapter);

        selectedContactAdapter.setUserModels(selectedContacts);

        getUserContacts();
    }

    private void getUserContacts() {


        if (permissions.isContactOk(getContext())) {
            ArrayList<UserModel> userContacts = new ArrayList<>();
            String[] projection = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            ContentResolver cr = getContext().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
            if (cursor != null) {
                userContacts.clear();
                try {


                    while (cursor.moveToNext()) {

                        int nameNum = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        String name = cursor.getString(nameNum);
                        int numberNum = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String number = cursor.getString(numberNum);
                        Log.d("checkdata",name+number);


                        number = number.replaceAll("\\s", "");
                        String num = String.valueOf(number.charAt(0));

                        if (num.equals("0"))
                            number = number.replaceFirst("(?:0)+", "+1");

                        UserModel userModel = new UserModel();
                        userModel.setName(name);
                        userModel.setNumber(number);
                        userContacts.add(userModel);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            cursor.close();
            getAppContacts(userContacts);

        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, AllConstants.CONTACTS_REQUEST_CODE);
        }
    }

    private void getAppContacts(final ArrayList<UserModel> mobileContacts) {

        appContacts = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("number");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    appContacts.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String number = ds.child("number").getValue().toString();

                        for (UserModel userModel : mobileContacts) {

                            if (userModel.getNumber().equals(number) && !number.equals(userPhoneNumber)) {

                                String image = ds.child("image").getValue().toString();
                                String status = ds.child("status").getValue().toString();
                                String uID = ds.child("uID").getValue().toString();

                                String name = ds.child("name").getValue().toString();
                                UserModel registeredUser = new UserModel();

                                registeredUser.setName(name);
                                registeredUser.setStatus(status);
                                registeredUser.setImage(image);
                                registeredUser.setuID(uID);

                                appContacts.add(registeredUser);
                                break;
                            }
                        }
                    }
                    groupContactAdapter.setArrayList(appContacts);


                } else Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AllConstants.CONTACTS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserContacts();
            } else {
                Toast.makeText(requireContext(), "Contact Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onContactClick(UserModel userModel, int position, boolean isSelect) {

        if (isSelect) {
            selectedContacts.remove(userModel);
            selectedContactAdapter.setUserModels(selectedContacts);
        } else {
            if (!selectedContacts.contains(userModel)) {
                selectedContacts.add(userModel);
                selectedContactAdapter.setUserModels(selectedContacts);
            }
        }

    }

    private void createGroup() {

        Toast.makeText(requireContext(), "Creating Group", Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Group Detail");
        String groupId = databaseReference.push().getKey();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(groupId + AllConstants.GROUP_IMAGE).putFile(groupImage).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(requireContext(), "Working........", Toast.LENGTH_SHORT).show();
            Task<Uri> image = taskSnapshot.getStorage().getDownloadUrl();
            image.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String url = task.getResult().toString();
                    GroupModel groupModel = new GroupModel();
                    groupModel.adminId = firebaseAuth.getUid();
                    groupModel.adminName = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                            .getString("username", "");
                    groupModel.name = groupName;
                    groupModel.createdAt = String.valueOf(System.currentTimeMillis());
                    groupModel.image = url;
                    groupModel.id = groupId;

                    databaseReference.child(groupId).setValue(groupModel);
                    Toast.makeText(requireContext(), "Few seconds.....", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Group Detail").child(groupId).child("Members");
                    GroupMemberModel groupMemberModel = new GroupMemberModel();
                    groupMemberModel.id = firebaseAuth.getUid();
                    groupMemberModel.role = "admin";

                    reference.child(firebaseAuth.getUid()).setValue(groupMemberModel);

                    for (UserModel userModel : selectedContacts) {

                        GroupMemberModel memberModel = new GroupMemberModel();
                        memberModel.id = userModel.getuID();
                        memberModel.role = "member";
                        reference.child(userModel.getuID()).setValue(memberModel);
                    }

                    Toast.makeText(requireContext(), "Group Created", Toast.LENGTH_SHORT).show();
                    getActivity().finish();


                } else {
                    Toast.makeText(requireContext(), "Error : " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            });

        });
    }
}