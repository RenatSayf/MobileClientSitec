package com.renatsayf.mobileclientsitec.ui.users;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.renatsayf.mobileclientsitec.R;
import com.renatsayf.mobileclientsitec.databinding.FragmentUsersBinding;
import com.renatsayf.mobileclientsitec.model.users.Users;
import com.renatsayf.mobileclientsitec.ui.login.LoginFragment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UsersFragment extends Fragment
{
    public static String KEY_IMEI = "KEY_IMEI";
    private FragmentUsersBinding binding;
    private UsersViewModel mViewModel;
    private String imei;
    private TelephonyManager telephonyManager;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        telephonyManager = (TelephonyManager) requireContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
        }
        else {
            imei = telephonyManager.getDeviceId();
            mViewModel.getUsers(imei);
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            int authCode = arguments.getInt(LoginFragment.KEY_AUTH_CODE, -1);
            if (authCode > 0) {
                UsersViewModel.State newState = new UsersViewModel.State();
                newState.isAuth = true;
                mViewModel.setState(newState);
            }
        }

        mViewModel.getState().observe(getViewLifecycleOwner(), observer -> {
            if (!observer.isAuth) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_IMEI, imei);
                navController.navigate(R.id.action_usersFragment_to_loginFragment, bundle);
            }
            else if (observer.users != null && !observer.users.users.listUsers.isEmpty()) {
                setUsersToSpinner(observer.users);
            }
            else if (!observer.error.isEmpty()) {
                Snackbar.make(binding.getRoot(), observer.error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Toast.makeText(requireContext(), "Permission granted.", Toast.LENGTH_SHORT).show();
                        imei = telephonyManager.getDeviceId();
                        mViewModel.getUsers(imei);
                        return;
                    } else {
                        Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void setUsersToSpinner(Users users) {
        Stream<String> stringStream = users.users.listUsers.stream().map(user -> user.user);
        List<String> userList = stringStream.collect(Collectors.toList());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userList);
        binding.spinnerUsers.setAdapter(arrayAdapter);
    }

}