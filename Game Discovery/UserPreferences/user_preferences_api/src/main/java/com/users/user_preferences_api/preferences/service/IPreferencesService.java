package com.users.user_preferences_api.preferences.service;

import com.users.user_preferences_api.custom.response.CollectionResponse;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPost;
import com.users.user_preferences_api.preferences.dto.PreferenceReqPut;
import com.users.user_preferences_api.preferences.dto.PreferenceResp;

public interface IPreferencesService {
	

	CollectionResponse<PreferenceResp> findById(Long id);

	CollectionResponse<PreferenceResp> create(PreferenceReqPost preferenceReqPost);

	/**
	 *
	 * We treat PUT method here as partial update and not total update. 
	 * This means that only passed parameters will be replaced on the entity.
	 * There is an exception for the entities related to this table (FK).
	 * For these list in input we apply the following logic:
	 * 1) if no parameter is passed -> nothing is updated on db
	 * 2) if empty list is passed -> complete wipe on the foreign entity table
	 * 3) if list with values are passed -> only those values are saved on db the others are wiped
	 * @param id
	 * @param preferenceReqPut
	 * @return
	 * @author fedex2118
	 */
	CollectionResponse<PreferenceResp> update(Long id, PreferenceReqPut preferenceReqPut);

	CollectionResponse<PreferenceResp> deleteById(Long id);

	CollectionResponse<Long> findUserIdByOwnedGame(Long id);

}
