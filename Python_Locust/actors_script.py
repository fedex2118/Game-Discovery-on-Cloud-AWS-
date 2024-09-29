from locust import HttpUser, TaskSet, task, between
import random

ALB_BASE_URL = "http://GatewayLoadBalancer-1906045949.eu-west-3.elb.amazonaws.com"

# to run: locust -f gameCreationTest.py --host http://GatewayLoadBalancer-1906045949.eu-west-3.elb.amazonaws.com --web-port 8089
# then run the control_locust.py script

user_credentials = []

for i in range(1, 180):
    user_credentials += [{"email": "us" + str(i), "password": "us" + str(i)}]

# only 2 admins
admin_credentials = [
    {"email": "admin@email.com", "password": "admin"},   # 1 DEFAULT
    {"email": "adminF", "password": "adminF"}, # 2
    {"email": "adminF2", "password": "adminF2"}, # 3
    {"email": "adminF3", "password": "adminF3"}, # 4
    {"email": "adminF4", "password": "adminF4"}, # 5
    {"email": "adminF5", "password": "adminF5"}, # 6
    {"email": "adminF6", "password": "adminF6"}, # 7
    {"email": "adminF7", "password": "adminF7"}, # 8
    {"email": "adminF8", "password": "adminF8"}, # 9
    {"email": "adminF9", "password": "adminF9"}   # 10
]

class GameTasks(TaskSet):
    def on_start(self):
        """
        on_start is called when a Locust user starts before any task is scheduled.
        Here we perform the authentication to get the JWT token.
        """
        self.authenticate()

    def authenticate(self):
        payload = {
            "email": self.user.email,
            "password": self.user.password
        }
        response = self.client.post(f"{ALB_BASE_URL}/api/v1/auth/authenticate", json=payload)
        if response.status_code == 200:
            content = response.json().get('content', [])
            if content:
                self.token = content[0].get('access_token')
                self.headers = {"Authorization": f"Bearer {self.token}"}
        else:
            self.token = None
            self.headers = None

    @task
    def create_game(self):
        if not self.user.is_admin and self.headers:
            self.client.post(f"{ALB_BASE_URL}/api/v1/game", json={
                "name": "GGGGGGG",
                "releaseDate": "2024-05-29T16:49:08.522Z",
                "price": random.choice([1, 10, 20, 30, 40, 50, 60]),
                "isSingleplayer": random.choice([True, False]),
                "isMultiplayer": random.choice([True, False]),
                "isPvp": random.choice([True, False]),
                "isPve": random.choice([True, False]),
                "description": "string",
                "isTwoDimensional": random.choice([True, False]),
                "isThreeDimensional": random.choice([True, False]),
                "coverArtUrl": "string",
                "platformIds": [
                    1, 2, 4, random.choice([5, 3, 1])
                ],
                "genreIds": [
                    7, 9, 12, random.choice([5, 3, 17])
                ],
                "publisherIds": [
                    21, 10, 3, random.choice([5, 8, 17])
                ],
                "developerIds": [
                    16, 17, 18, 11, random.choice([2, 3, 1])
                ]
            }, headers=self.headers)

    @task
    def process_pending_games(self):
        if self.user.is_admin and self.headers:
            response = self.client.get(f"{ALB_BASE_URL}/api/v1/by-admin/games/with-pending-status", headers=self.headers)
            games = response.json().get("content", [])
            payload = []
            for game in games:
                game_id = game['id']
                status = random.choice([1, 2]) # 1: APPROVED | 2: REJECTED
                payload += [{"id": game_id, "status": status}]
            
            if len(payload) > 0:
                self.client.patch(f"{ALB_BASE_URL}/api/v1/by-admin/games/status", json=payload, headers=self.headers)

    @task
    def review_games(self):
        if self.headers:
            game_id = random.choice([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19])
            # retrieve user id
            response_user_id = self.client.get(f"{ALB_BASE_URL}/api/v1/user/user-id", headers=self.headers)
            user_id_content = response_user_id.json().get("content", [])
            user = user_id_content[0]
            user_id = user['id']
            # check if a review for that game exists
            response_review = self.client.get(f"{ALB_BASE_URL}/api/v1/review/game/" + str(game_id) + "/user/" + str(user_id), headers=self.headers)
            review_content = response_review.json().get("content", [])
            print("reviewFOUND: " + str(review_content) + str(user_id))
            print(len(review_content))
            if len(review_content) > 0:
                # send update for the review
                self.client.put(f"{ALB_BASE_URL}/api/v1/review/game/" + str(game_id) + "/user/" + str(user_id), json={
                    "vote": random.choice([1, 2 , 3, 4, 5, 6, 7, 8, 9, 10]),
                    "description": "string"
                }, headers=self.headers)
            else:
                # create review
                self.client.post(f"{ALB_BASE_URL}/api/v1/review/game", json={
                    "userId": user_id,
                    "gameId": game_id,
                    "vote": random.choice([1, 2 , 3, 4, 5, 6, 7, 8, 9, 10]),
                    "description": "string"
                }, headers=self.headers)
    
    @task
    def update_preference(self):
        if self.headers:
            # retrieve user id
            response_user_id = self.client.get(f"{ALB_BASE_URL}/api/v1/user/user-id", headers=self.headers)
            user_id_content = response_user_id.json().get("content", [])
            user = user_id_content[0]
            user_id = user['id']
            # check if preference exists for that user
            response_user_preference = self.client.get(f"{ALB_BASE_URL}/api/v1/preference/" + str(user_id), headers=self.headers)
            user_preference_content = response_user_preference.json().get("content", [])
            print("preferenceFOUND: " + str(user_preference_content) + str(user_id))
            print(len(user_preference_content))
            if len(user_preference_content) > 0:
                # update user preference
                response = self.client.put(f"{ALB_BASE_URL}/api/v1/preference/" + str(user_id), json={
                    "startingDate": "2019-05-29T19:45:01.930Z",
                    "startingPrice": random.choice([12, 18, 25, 41, 32, 5]),
                    "isSingleplayer": random.choice([True, False]),
                    "isMultiplayer": random.choice([True, False]),
                    "isPvp": random.choice([True, False]),
                    "isPve": random.choice([True, False]),
                    "isTwoDimensional": random.choice([True, False]),
                    "isThreeDimensional": random.choice([True, False]),
                    "startingAverageRating": random.choice([3, 4, 5, 6, 7, 8, 9]),
                    "startingReviewQuantity": random.choice([5, 10, 25, 40, 60, 80, 100]),
                    "skipGenres": random.choice([True, False]),
                    "skipPublishers": random.choice([True, False]),
                    "skipDevelopers": random.choice([True, False]),
                    "skipPlatforms": random.choice([True, False]),
                    "platformIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "genreIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "publisherIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "developerIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "libraryIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "wishlistIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ]
                }, headers=self.headers)
            else:
                # create user_preference_content
                response = self.client.post(f"{ALB_BASE_URL}/api/v1/preference/" + str(user_id), json={
                    "startingDate": "2019-05-29T19:45:01.930Z",
                    "startingPrice": random.choice([12, 18, 25, 41, 32, 5]),
                    "isSingleplayer": random.choice([True, False]),
                    "isMultiplayer": random.choice([True, False]),
                    "isPvp": random.choice([True, False]),
                    "isPve": random.choice([True, False]),
                    "isTwoDimensional": random.choice([True, False]),
                    "isThreeDimensional": random.choice([True, False]),
                    "startingAverageRating": random.choice([3, 4, 5, 6, 7, 8, 9]),
                    "startingReviewQuantity": random.choice([5, 10, 25, 40, 60, 80, 100]),
                    "skipGenres": random.choice([True, False]),
                    "skipPublishers": random.choice([True, False]),
                    "skipDevelopers": random.choice([True, False]),
                    "skipPlatforms": random.choice([True, False]),
                    "platformIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "genreIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "publisherIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "developerIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "libraryIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ],
                    "wishlistIds": [
                        random.choice([1, 4, 7, 17, 9, 5, 27, 2, 3, 18, 31, 19])
                    ]
                }, headers=self.headers)
                print(str(response) + str(user_id))

    @task
    def game_discovery(self):
        if self.headers:
            # game discovery
            self.client.get(f"{ALB_BASE_URL}/api/v1/discovery", headers=self.headers)

# class GameUser(HttpUser):
#     tasks = [GameTasks]
#     wait_time = between(1, 5)  # wait between 1 and 5 seconds between tasks

#     # Keep track of how many admin users have been created
#     admin_count = 0
#     user_count = 1

#     def __init__(self, *args, **kwargs):
#         super().__init__(*args, **kwargs)
#         if GameUser.admin_count < 10:
#             user = admin_credentials[GameUser.admin_count]
#             self.is_admin = True
#             GameUser.admin_count += 1
#         elif GameUser.user_count < 100:
#             user = user_credentials[GameUser.user_count]
#             self.is_admin = False
#             GameUser.user_count += 1

#         self.email = user["email"]
#         self.password = user["password"]

class AdminUser(HttpUser):
    tasks = [GameTasks]
    wait_time = between(1, 5)
    is_admin = True

    def on_start(self):
        user = admin_credentials[self.environment.runner.user_count % len(admin_credentials)]
        self.email = user["email"]
        self.password = user["password"]

class RegularUser(HttpUser):
    tasks = [GameTasks]
    wait_time = between(1, 5)
    is_admin = False
    
    def on_start(self):
        user = random.choice(user_credentials)
        self.email = user["email"]
        self.password = user["password"]

# if __name__ == "__main__":
    # import os
    # os.system("locust -f gameCreationTest.py")
