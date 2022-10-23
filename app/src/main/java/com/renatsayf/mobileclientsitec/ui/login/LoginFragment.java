package com.renatsayf.mobileclientsitec.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renatsayf.mobileclientsitec.R;
import com.renatsayf.mobileclientsitec.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment
{
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

        binding.btnLogin.setOnClickListener( view1 -> {
            navController.navigate(R.id.action_loginFragment_to_usersFragment);
        });

    }
}