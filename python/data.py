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

# List table


class Database:
    __instance = None

    @staticmethod
    def getDatabase():
        if Database.__instance == None:
            Database()
        return Database.__instance

    def __init__(self):
        if Database.__instance != None:
            raise Exception("This class is a singleton!")
        else:
            self.db = firebase.database()
            Database.__instance = self

    def db_fan(self):
        return self.db.child('Fan')

    def db_light(self):
        return self.db.child('Light')

    def db_sensor(self):
        return self.db.child('Sensor')

    def db_rec(self):
        return self.db.child('Record')

    def db_location(self):
        return self.db.child('Location')

    def db_user(self):
        return self.db.child('User')

    def create(self):

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
            self.db.child('Fan').push(i)
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
            self.db.child('Light').push(i)

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
            self.db.child('Sensor').push(i)

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
            self.db.child('Location').push(i)
