package com.example.findmy.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.findmy.model.MarketListing;
import com.example.findmy.model.MarketListingRequest;
import com.example.findmy.model.POI;
import com.example.findmy.databinding.ProfilePoiBottomSheetBinding;
import com.example.findmy.model.User;
import com.example.findmy.network.FindMyService;
import com.example.findmy.network.FindMyServiceViewModel;
import com.example.findmy.ui.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPOIBottomSheet extends BottomSheetDialogFragment {
    private final View.OnClickListener submitListingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            User currentUser = ((HomeActivity) requireActivity()).currentUser;
            MarketListingRequest request = new MarketListingRequest(getListingPrice(), currentUser.getId(), myPOI.getId(), true, false);
            findMyService.createListing(request).enqueue(new Callback<MarketListing>() {
                @Override
                public void onResponse(Call<MarketListing> call, Response<MarketListing> response) {
                    if(!response.isSuccessful()) {
                        findMyService.showErrorToast(requireContext());
                        return;
                    }
                    dismiss();
                }

                @Override
                public void onFailure(Call<MarketListing> call, Throwable t) {
                    findMyService.showErrorToast(requireContext());
                }
            });
        }
    };

    private final View.OnClickListener unlistListingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: add calls to backend
        }
    };

    ProfilePoiBottomSheetBinding binding;

    private final POI myPOI;
    private FindMyService findMyService;
    private EditText inputListingPriceText;

    MyPOIBottomSheet(POI myPOI) {
        this.myPOI = myPOI;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        findMyService = new ViewModelProvider(requireActivity()).get(FindMyServiceViewModel .class).getFindMyService();

        binding = ProfilePoiBottomSheetBinding.inflate(inflater, container, false);

        setupPOIName(binding);
        setupSubmitListingButton(binding);
        setupUnlistButton(binding);
        setupListingPriceInput(binding);

        return binding.getRoot();
    }

    private void setupPOIName(ProfilePoiBottomSheetBinding binding) {
        TextView myPOINameText = binding.poiName;
        myPOINameText.setText(this.myPOI.getDescription());
    }

    private void setupSubmitListingButton(ProfilePoiBottomSheetBinding binding) {
        binding.listButton.setOnClickListener(submitListingListener);
    }

    private void setupUnlistButton(ProfilePoiBottomSheetBinding binding) {
        binding.unlistButton.setOnClickListener(unlistListingListener);
    }

    private void setupListingPriceInput(ProfilePoiBottomSheetBinding binding) {
        inputListingPriceText = binding.inputListingPrice;
    }

    private int getListingPrice() {
        String listingPrice = String.valueOf(inputListingPriceText.getText());
        return Integer.parseInt(listingPrice);
    }
}