package com.trader.joes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.trader.joes.R;
import com.trader.joes.service.AuthService;
import com.trader.joes.service.UtilityService;

import java.util.function.Consumer;

public class AccountFragment extends Fragment {

    private RelativeLayout mainLayout;
    private Button mEditPhotoBtn;
    private Button mEditPersonalInfoBtn;
    private RelativeLayout mPersonalInfoLayout;
    private RelativeLayout mEditPersonalInfoLayout;
    private Button mSavePersonalInfoBtn;
    private Button mCancelPersonalInfoBtn;
    private TextView mDisplayName;
    private TextView mEmailAddress;
    private EditText mEditNameInput;
    private EditText mEditEmailInput;
    private AuthService authService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        authService = new AuthService();

        //initialize UI components
        mainLayout = view.findViewById(R.id.account_mgmt_layout);
        mEditPhotoBtn = view.findViewById(R.id.edit_profile_photo);
        mEditPersonalInfoBtn = view.findViewById(R.id.edit_personal_info_btn);
        mPersonalInfoLayout = view.findViewById(R.id.personal_info_layout);
        mEditPersonalInfoLayout = view.findViewById(R.id.edit_personal_info_layout);
        mSavePersonalInfoBtn = view.findViewById(R.id.save_personal_info_btn);
        mCancelPersonalInfoBtn = view.findViewById(R.id.cancel_personal_info_btn);
        mEditNameInput = view.findViewById(R.id.edit_name_value);
        mEditEmailInput = view.findViewById(R.id.edit_email_value);
        mDisplayName = view.findViewById(R.id.display_name_value);
        mEmailAddress = view.findViewById(R.id.display_email_value);

        //add onclick listener
        mEditPhotoBtn.setOnClickListener(new AccountFragmentListener());
        mEditPersonalInfoBtn.setOnClickListener(new AccountFragmentListener());
        mSavePersonalInfoBtn.setOnClickListener(new AccountFragmentListener());
        mCancelPersonalInfoBtn.setOnClickListener(new AccountFragmentListener());

        updateProfileDetails();
        return view;
    }

    private void updateProfileDetails() {
        FirebaseUser currentUser = authService.getCurrentUser();
        mDisplayName.setText(currentUser.getDisplayName());
        mEmailAddress.setText(currentUser.getEmail());
    }

    private class AccountFragmentListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.edit_profile_photo: editProfilePhoto();
                break;
                case R.id.edit_personal_info_btn: initializeEditPersonalInfo();
                break;
                case R.id.save_personal_info_btn: savePersonalInfo();
                break;
                case R.id.cancel_personal_info_btn: cancelPersonalInfo();
                break;
                default: break;
            }
        }

        private void editProfilePhoto() {

        }

        private void initializeEditPersonalInfo() {
            mEditPersonalInfoBtn.setVisibility(View.GONE);
            mPersonalInfoLayout.setVisibility(View.GONE);
            mEditPersonalInfoLayout.setVisibility(View.VISIBLE);

            //pre-populate edit fields
            FirebaseUser currentUser = authService.getCurrentUser();
            mEditNameInput.setText(currentUser.getDisplayName());
            mEditEmailInput.setText(currentUser.getEmail());
        }

        private void savePersonalInfo() {
            UtilityService.hideKeyboard(getActivity(), mainLayout);

            Consumer<Void> successCallback = new Consumer<Void>() {
                @Override
                public void accept(Void unused) {
                    updateProfileDetails();
                }
            };

            authService.updateUserProfile(
                    String.valueOf(mEditNameInput.getText()),
                    String.valueOf(mEditEmailInput.getText()),
                    successCallback
            );

            //Then, revert to default view
            mEditPersonalInfoBtn.setVisibility(View.VISIBLE);
            mPersonalInfoLayout.setVisibility(View.VISIBLE);
            mEditPersonalInfoLayout.setVisibility(View.GONE);
        }

        private void cancelPersonalInfo() {
            UtilityService.hideKeyboard(getActivity(), mainLayout);
            mEditPersonalInfoBtn.setVisibility(View.VISIBLE);
            mPersonalInfoLayout.setVisibility(View.VISIBLE);
            mEditPersonalInfoLayout.setVisibility(View.GONE);
        }
    }
}