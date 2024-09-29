package com.gateway.gateway_api.preferences.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.preferences.data.classes.PreferenceResp;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPost;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPut;

public interface IPreferencesService {
	

	CollectionResponse<PreferenceResp> findById(Long id);

	CollectionResponse<PreferenceResp> create(Long id, PreferencesPost request);

	CollectionResponse<PreferenceResp> update(Long id, PreferencesPut request);

	CollectionResponse<PreferenceResp> deleteById(Long id);

}
