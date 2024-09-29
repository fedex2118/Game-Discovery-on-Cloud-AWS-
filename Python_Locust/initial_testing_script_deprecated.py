import requests

# Define the base URL of the internal ALB
ALB_BASE_URL = "http://GatewayLoadBalancer-1906045949.eu-west-3.elb.amazonaws.com"
INTERNAL_ALB_URL = "http://MicroservicesLoadBalancer-1679407097.eu-west-3.elb.amazonaws.com"

# Define the endpoints for each service
#AUTH_URL = f"{ALB_BASE_URL}/auth/api"
#GAME_URL = f"{ALB_BASE_URL}/game/api"
# PREFERENCES_URL = f"{ALB_BASE_URL}/preferences/api"
# REVIEWS_URL = f"{ALB_BASE_URL}/reviews/api"

GATEWAY_URL = f"{ALB_BASE_URL}/api/v1/auth/sign-up"
#AUTH_URL = f"{ALB_BASE_URL}/api/v1/auth/authenticate"

def send_request(url, payload):
    """
    Sends a POST request to the specified URL with the given payload.
    """
    try:
        response = requests.post(url, json=payload)
        #response.raise_for_status()
        return response.json()
    except requests.exceptions.HTTPError as http_err:
        print(f"HTTP error occurred: {http_err}")
    except Exception as err:
        print(f"Other error occurred: {err}")

# Example payload
for i in range(13, 200):
    payload = {
        "username": "us" + str(i),
        "email": "us" + str(i),
        "password": "us"+ str(i)
    }

    gateway_response = send_request(GATEWAY_URL, payload)
    print("gateway Response:", gateway_response)


# payload = {
#   "email": "string",
#   "password": "string"
# }

# Send requests to different services
# gateway_response = send_request(GATEWAY_URL, payload)
#auth_response = send_request(AUTH_URL, payload)
# game_response = send_request(GAME_URL, payload)
# preferences_response = send_request(PREFERENCES_URL, payload)
# reviews_response = send_request(REVIEWS_URL, payload)

# Print the responses
# print("gateway Response:", gateway_response)
#print("Auth Response:", auth_response)
# print("Game Response:", game_response)
# print("Preferences Response:", preferences_response)
# print("Reviews Response:", reviews_response)
