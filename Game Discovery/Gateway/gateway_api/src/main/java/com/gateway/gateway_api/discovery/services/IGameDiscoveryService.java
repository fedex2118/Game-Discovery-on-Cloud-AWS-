package com.gateway.gateway_api.discovery.services;

import com.gateway.gateway_api.custom.response.CollectionResponse;
import com.gateway.gateway_api.games.data.classes.GameResp;

public interface IGameDiscoveryService {

	CollectionResponse<GameResp> discover(Double randomness);

}
