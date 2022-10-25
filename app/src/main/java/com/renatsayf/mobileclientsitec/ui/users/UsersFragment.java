package com.renatsayf.mobileclientsitec.ui.users;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.renatsayf.mobileclientsitec.R;
import com.renatsayf.mobileclientsitec.databinding.FragmentUsersBinding;
import com.renatsayf.mobileclientsitec.model.users.User;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import androidx.activity.OnBackPressedCallback;
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
    public static final String KEY_IMEI = "KEY_IMEI";
    public static final String KEY_UID = "KEY_UID";
    private FragmentUsersBinding binding;
    private UsersViewModel usersVM;
    private NavController navController;
    private String uid;
    private TelephonyManager telephonyManager;
    private String imei;

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
        usersVM = new ViewModelProvider(this).get(UsersViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        telephonyManager = (TelephonyManager) requireContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE);
        }
        else {
            imei = telephonyManager.getDeviceId();
            String uuid = UUID.randomUUID().toString();
            usersVM.auth(imei, uuid, "http");
        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            String imei = arguments.getString(KEY_IMEI, "");
            if (!imei.isEmpty()) {

                usersVM.getUsers(imei);
            }
        }

        usersVM.getState().observe(getViewLifecycleOwner(), state -> {

            if (state.isAuth && state.users == null) {
                usersVM.getUsers(imei);
            }
            else if (state.users != null && !state.users.isEmpty()) {
                setUsersToSpinner(state.users);
            }
            else if (!state.error.isEmpty()) {
                Snackbar.make(binding.getRoot(), state.error, Snackbar.LENGTH_LONG).show();
            }
        });

        binding.spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                UsersViewModel.State state = usersVM.getState().getValue();
                if (state != null) {
                    List<User> users = state.users;
                    if (users != null) {
                        User user = users.get(i);
                        uid = user.uid;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {}
        });

        binding.etPass.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                binding.btnLogin.setEnabled(charSequence.length() > 3);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {}
        });

        binding.btnLogin.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_IMEI, imei);
            bundle.putString(KEY_UID, uid);
            navController.navigate(R.id.action_usersFragment_to_resultFragment, bundle);
        });
    }

    private void setUsersToSpinner(List<User> users) {
        Stream<String> stringStream = users.stream().map(user -> user.user);
        List<String> userList = stringStream.collect(Collectors.toList());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userList);
        binding.spinnerUsers.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true)
        {
            @Override
            public void handleOnBackPressed()
            {
                requireActivity().finish();
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
                        String uuid = UUID.randomUUID().toString();
                        usersVM.auth(imei, uuid, "http");
                    } else {
                        Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}