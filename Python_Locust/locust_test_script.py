import requests
import time

# to run in headless mode: locust -f actors_script.py --headless --host http://localhost:8084 --web-port 8089

# Base URL for Locust's web interface
locust_url = 'http://localhost:8089'

def start_test(user_count, spawn_rate):
    response = requests.post(f'{locust_url}/swarm', data={
        'user_count': user_count,
        'spawn_rate': spawn_rate
    })
    if response.status_code == 200:
        print(f'Successfully started test with {user_count} users.')
    else:
        print(f'Failed to start test: {response.status_code} {response.content}')

def change_users(user_count, spawn_rate):
    response = requests.post(f'{locust_url}/swarm', data={
        'user_count': user_count,
        'spawn_rate': spawn_rate
    })
    if response.status_code == 200:
        print(f'Successfully changed user count to {user_count}.')
    else:
        print(f'Failed to change user count: {response.status_code} {response.content}')

def stop_test():
    response = requests.get(f'{locust_url}/stop')
    if response.status_code == 200:
        print('Successfully stopped the test.')
    else:
        print(f'Failed to stop test: {response.status_code} {response.content}')

# Start with 30 users (10 of which are admins)
start_test(user_count=30, spawn_rate=1)

# Wait for 10 minutes
time.sleep(600)  # 600 seconds = 10 minutes

# Increase to 100 users (10 of which are admins)
change_users(user_count=100, spawn_rate=1)

# Wait for another 10 minutes
time.sleep(600)

# Decrease to 10 users (10 of which are admins)
change_users(user_count=10, spawn_rate=1)

# Wait for another 10 minutes
time.sleep(600)

# Stop the test
stop_test()
