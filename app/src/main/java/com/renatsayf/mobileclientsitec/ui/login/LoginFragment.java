package com.renatsayf.mobileclientsitec.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.renatsayf.mobileclientsitec.R;
import com.renatsayf.mobileclientsitec.databinding.FragmentLoginBinding;
import com.renatsayf.mobileclientsitec.ui.users.UsersFragment;

import java.util.Objects;
import java.util.UUID;


@AndroidEntryPoint
public class LoginFragment extends Fragment
{
    public static final String KEY_AUTH_CODE = "KEY_AUTH_CODE";
    private FragmentLoginBinding binding;
    private LoginViewModel mViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String imei = arguments.getString(UsersFragment.KEY_IMEI);

            binding.btnLogin.setOnClickListener(v -> {
                String uuid = UUID.randomUUID().toString();
                String pass = Objects.requireNonNull(binding.etPass.getText()).toString();
                mViewModel.auth(imei, uuid, pass);
            });
        }

        mViewModel.getState().observe(getViewLifecycleOwner(), observer -> {
            if (observer.code != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_AUTH_CODE, observer.code);
                navController.navigate(R.id.action_loginFragment_to_usersFragment, bundle);
            }
            else if (!observer.error.isEmpty()) {
                Snackbar.make(binding.getRoot(), observer.error, Snackbar.LENGTH_LONG).show();
            }
        });


    }
}