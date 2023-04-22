package com.trader.joes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.trader.joes.R;

public class AccountFragment extends Fragment {

    private Button mEditPhotoBtn;
    private Button mEditPersonalInfoBtn;
    private RelativeLayout mPersonalInfoLayout;
    private RelativeLayout mEditPersonalInfoLayout;
    private Button mSavePersonalInfoBtn;
    private Button mCancelPersonalInfoBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //initialize UI components
        mEditPhotoBtn = view.findViewById(R.id.edit_profile_photo);
        mEditPersonalInfoBtn = view.findViewById(R.id.edit_personal_info_btn);
        mPersonalInfoLayout = view.findViewById(R.id.personal_info_layout);
        mEditPersonalInfoLayout = view.findViewById(R.id.edit_personal_info_layout);
        mSavePersonalInfoBtn = view.findViewById(R.id.save_personal_info_btn);
        mCancelPersonalInfoBtn = view.findViewById(R.id.cancel_personal_info_btn);

        //add onclick listener
        mEditPhotoBtn.setOnClickListener(new AccountFragmentListener());
        mEditPersonalInfoBtn.setOnClickListener(new AccountFragmentListener());
        mSavePersonalInfoBtn.setOnClickListener(new AccountFragmentListener());
        mCancelPersonalInfoBtn.setOnClickListener(new AccountFragmentListener());

        return view;
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
        }

        private void savePersonalInfo() {
            //TODO: Save the info first

            //Then, revert to default view
            mEditPersonalInfoBtn.setVisibility(View.VISIBLE);
            mPersonalInfoLayout.setVisibility(View.VISIBLE);
            mEditPersonalInfoLayout.setVisibility(View.GONE);
        }

        private void cancelPersonalInfo() {
            mEditPersonalInfoBtn.setVisibility(View.VISIBLE);
            mPersonalInfoLayout.setVisibility(View.VISIBLE);
            mEditPersonalInfoLayout.setVisibility(View.GONE);
        }
    }
}