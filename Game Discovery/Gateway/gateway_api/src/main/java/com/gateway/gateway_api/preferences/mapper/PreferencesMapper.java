package com.gateway.gateway_api.preferences.mapper;

import org.mapstruct.Mapper;

import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPost;
import com.gateway.gateway_api.preferences.data.classes.PreferenceReqPut;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPost;
import com.gateway.gateway_api.preferences.request.dto.PreferencesPut;

@Mapper
public interface PreferencesMapper {

	PreferenceReqPost toPreferenceReqPost(PreferencesPost request);
	
	PreferenceReqPut toPreferenceReqPut(PreferencesPut request);

}
