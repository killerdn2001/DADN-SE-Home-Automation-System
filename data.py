from datetime import date
# from firebase import firebase
from datetime import datetime, date
import time
import random
import pyrebase
firebaseConfig = {
    "apiKey": "AIzaSyCxIoAXMA2Ae9rgkEMaxWv3VYIc0PFPcVE",
    "authDomain": "btl-dadn.firebaseapp.com",
    "databaseURL": "https://btl-dadn-default-rtdb.firebaseio.com",
    "projectId": "btl-dadn",
    "storageBucket": "btl-dadn.appspot.com",
    "messagingSenderId": "148990099639",
    "appId": "1:148990099639:web:6daa9f2f2eb5947f6c4d32",
    "measurementId": "G-TZPK45ENSB"
}

firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database()
# List table


def db_fan():
    return db.child('Fan')


def db_light():
    return db.child('Light')


def db_rec_fan():
    return db.child('Record Fan')


def db_rec_light():
    return db.child('Record Light')


def db_rec():
    return db.child('Record')


def db_location():
    return db.child('Location')


def db_user():
    return db.child('User')


def create():

    #Fan, Light
    fan = [{
        'id': 1,
        'level': 2,
        'id_location': 1
    }, {
        'id': 2,
        'level': 2,
        'id_location': 2
    }, {
        'id': 3,
        'level': 2,
        'id_location': 3
    }, {
        'id': 4,
        'level': 2,
        'id_location': 4
    }]
    for i in fan:
        db.child('Fan').push(i)
    light = [{
        'id': 5,
        'status': 'off',
        'id_location': 1
    }, {
        'id': 6,
        'status': 'off',
        'id_location': 2
    }, {
        'id': 7,
        'status': 'off',
        'id_location': 3
    }, {
        'id': 8,
        'status': 'off',
        'id_location': 4
    }]
    for i in light:
        db.child('Light').push(i)

    # Sensor
    sensor = [{
        'id': 1,
        'id_location': 1
    }, {
        'id': 2,
        'id_location': 2
    }, {
        'id': 3,
        'id_location': 3
    }, {
        'id': 4,
        'id_location': 4
    }]
    for i in sensor:
        db.child('Sensor').push(i)

    # Location
    location = [{
        'id': 1,
        'temp': 30,
        'light': 5
    }, {
        'id': 2,
        'temp': 30,
        'light': 5
    },
        {
        'id': 3,
        'temp': 30,
        'light': 5
    },
        {
        'id': 4,
        'temp': 30,
        'light': 5
    }, ]
    for i in location:
        db.child('Location').push(i)

# Add record for one room


def add_room(id_room):
    today = date.today().strftime("%d/%m/%Y") + " " + \
        datetime.now().strftime("%H:%M")
    try:
        id_rec = len(db_rec().get().val())+1
    except:
        id_rec = 1
    temp = random.randint(20, 40)
    bright = random.randint(1, 9)
    update_fan = False
    update_light = False

    # If temp excess threshhold
    if temp < 26 or temp > 36:
        fan_query = db_fan().order_by_child(
            'id_location').equal_to(id_room).get()
        for fan in fan_query.each():
            level = fan.val()['level']
            update_fan = True
            # Update status and add rec if necessary
            if temp < 26 and level > 0:
                level -= 1
            elif temp > 36 and level < 3:
                level += 1
            else:
                update_fan = False
            if update_fan:
                db_fan().child(fan.key()).update({'level': level})
                data = {
                    'id': id_rec,
                    'id_sensor': id_room,
                    'id_device': fan.val()['id'],
                    'type': "Fan",
                    'light': bright,
                    'temp': temp,
                    'time': today,
                    'email': '',
                    'auto': True,
                    'status': '',
                    'level': level}
                db.child("Record").push(data)
                id_rec += 1
    # If bright excess threshhold
    if bright < 3 or bright > 7:
        light_query = db_light().order_by_child(
            'id_location').equal_to(id_room).get()
        for light in light_query.each():
            status = light.val()['status']
            update_light = True
            # Update status and add rec if necessary
            if bright < 3 and status == 'off':
                status = 'on'
            elif bright > 7 and status != 'off':
                status = 'off'
            else:
                update_light = False
            if update_light:
                db_light().child(light.key()).update({'status': status})
                data = {
                    'id': id_rec,
                    'id_sensor': id_room,
                    'id_device': light.val()['id'],
                    'type': "Light",
                    'light': bright,
                    'temp': temp,
                    'time': today,
                    'email': '',
                    'auto': True,
                    'status': status,
                    'level': ''}
                db.child("Record").push(data)
    if not update_fan and not update_light:
        data = {
            'id': id_rec,
            'id_sensor': id_room,
            'id_device': '',
            'type': '',
            'light': bright,
            'temp': temp,
            'time': today,
            'email': '',
            'auto': True,
            'status': '',
            'level': ''}
        db.child("Record").push(data)


def add():
    while(True):
        for i in range(4):
            add_room(i+1)
        time.sleep(5)


# create()
# add()
# listen()
