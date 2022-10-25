package com.renatsayf.mobileclientsitec.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.snackbar.Snackbar;
import com.renatsayf.mobileclientsitec.databinding.FragmentResultBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;



@AndroidEntryPoint
public class ResultFragment extends Fragment
{
    private FragmentResultBinding binding;
    private UsersViewModel userVM;
    private String uid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        userVM = new ViewModelProvider(this).get(UsersViewModel.class);

        UsersAdapter usersAdapter = new UsersAdapter(UsersAdapter.diffCallback);
        binding.rvUsers.setAdapter(usersAdapter);

        Bundle arguments = getArguments();
        if (savedInstanceState == null && arguments != null) {
            uid = arguments.getString(UsersFragment.KEY_UID, "");
            if (!uid.isEmpty()) {
                userVM.auth(uid, uid, "http");
            }
        }

        userVM.getState().observe(getViewLifecycleOwner(), state -> {
            if (!state.isAuth && state.users != null && state.saved) {
                usersAdapter.submitList(state.users);
            }
            else if (state.isAuth && state.users == null) {
                binding.progress.setVisibility(View.VISIBLE);
                userVM.getUsers(uid);
            }
            else if (state.users != null && !state.users.isEmpty() && !state.saved) {
                usersAdapter.submitList(state.users);
                userVM.saveUsers(state.users);
                binding.progress.setVisibility(View.GONE);
            }
            else if (state.saved) {
                Snackbar.make(binding.getRoot(), "Список пользователей сохранен", Snackbar.LENGTH_LONG).show();
                UsersViewModel.State newState = new UsersViewModel.State();
                newState.users = state.users;
                newState.saved = true;
                newState.isAuth = false;
                userVM.setState(newState);
                binding.progress.setVisibility(View.GONE);
            }
            else if (!state.error.isEmpty()) {
                Snackbar.make(binding.getRoot(), state.error, Snackbar.LENGTH_LONG).show();
                binding.progress.setVisibility(View.GONE);
            }
        });
    }
}