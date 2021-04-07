package com.example.eventhunter.authentication.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventhunter.authentication.AuthenticationService;
import com.example.eventhunter.databinding.LoginFragmentBinding;
import com.example.eventhunter.di.Injectable;
import com.example.eventhunter.di.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static com.example.eventhunter.MainActivity.AUTH_ACTIVITY_REQUEST_EXTRA;

public class LoginFragment extends Fragment {

    @Injectable()
    private AuthenticationService authenticationService;

    public LoginFragment() {
        ServiceLocator.getInstance().inject(this);
    }

    private LoginFragmentBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);

        binding.loginButton.setOnClickListener(view -> {
            String email = binding.editTextEmailAddress.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(view, "Some Fields Are Empty", Snackbar.LENGTH_SHORT)
                        .show();
                return;
            }

            authenticationService.login(email, password, errorMessage -> {
                if (errorMessage == null) {
                    Intent intent = new Intent();
                    intent.putExtra(AUTH_ACTIVITY_REQUEST_EXTRA, "login");
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                            .show();
                }
            });
        });
        binding.goToRegister.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(LoginFragmentDirections.navigateToRegister());
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}