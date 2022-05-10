import serial.tools.list_ports
import sys
from Adafruit_IO import MQTTClient

from datetime import datetime, date
from data import Database
import pyrebase
import time
import random

AIO_FEED_IDS = ["dadn.fan1", "dadn.led1", "dadn.light1",
                "dadn.temp1", "dadn.light1-button", "dadn.temp1-button"]
AIO_USERNAME = "dangtu2001"
AIO_KEY = "aio_mAWM05apROWTCFqP5OyyU20imVua"


def connected(client):
    print("Ket noi thanh cong ...")
    for feed in AIO_FEED_IDS:
        client.subscribe(feed)


def subscribe(client, userdata, mid, granted_qos):
    print("Subcribe thanh cong ...")


def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit(1)


def message(client, feed_id, payload):
    if isMicrobitConnected:
        if feed_id == "dadn.led1":
            if payload == "0":
                ser.write(("0#").encode())
            elif payload == "1":
                ser.write(("1#").encode())
        if feed_id == "dadn.fan1":
            if payload == "0":
                ser.write(("2#").encode())
            elif payload == "1":
                ser.write(("3#").encode())
            elif payload == "2":
                ser.write(("4#").encode())
            elif payload == "3":
                ser.write(("5#").encode())
        global temp, update_fan, fan_id, id_rec, level, id_room
        id_room = 1
        if feed_id == "dadn.temp1":
            fan_query = database.db_fan().order_by_child(
                'id_location').equal_to(id_room).get()
            for fan in fan_query.each():
                update_fan = True
                if not auto or auto == 'false':
                    update_fan = False
                level = fan.val()['level']
                fan_id = fan.val()['id']
                temp = int(payload)

                # If temp excess threshhold, update fan if necessary
                if temp <= 32 and level != 0:
                    level = 0
                elif temp > 32 and temp <= 35 and level != 1:
                    level = 1
                elif temp > 35 and temp <= 37 and level != 2:
                    level = 2
                elif temp > 37 and level != 3:
                    level = 3
                else:
                    update_fan = False
                if update_fan:
                    database.db_fan().child(fan.key()).update({'level': level})
        if feed_id == "dadn.light1":

            # Add record to database
            today = date.today().strftime("%d/%m/%Y") + " " + \
                datetime.now().strftime("%H:%M:%S")
            try:
                id_rec = len(database.db_rec().get().val())+1
            except:
                id_rec = 1
            light_query = database.db_light().order_by_child(
                'id_location').equal_to(id_room).get()
            for light in light_query.each():
                update_light = True
                if not auto or auto == 'false':
                    update_light = False
                status = light.val()['status']
                bright = int(payload)

                # If light excess threshhold, update light if necessary
                if bright <= 200 and status != 'on':
                    status = 'on'
                elif bright > 200 and status != 'off':
                    status = 'off'
                else:
                    update_light = False

                # If update fan, add record for fan
                if update_fan:
                    data_rec = {
                        'id': id_rec,
                        'id_sensor': id_room,
                        'id_device': fan_id,
                        'type': "Fan",
                        'light': bright,
                        'temp': temp,
                        'time': today,
                        'email': email,
                        'auto': True,
                        'status': '',
                        'level': level}
                    id_rec += 1
                    database.db_rec().push(data_rec)

                # If update light, add record for light
                if update_light:
                    database.db_light().child(
                        light.key()).update({'status': status})
                    data_rec = {
                        'id': id_rec,
                        'id_sensor': id_room,
                        'id_device': light.val()['id'],
                        'type': "Light",
                        'light': bright,
                        'temp': temp,
                        'time': today,
                        'email': email,
                        'auto': True,
                        'status': status,
                        'level': -1}
                    database.db_rec().push(data_rec)

                # If neither fan nor light update, add default record
                if not update_fan and not update_light:
                    data_rec = {
                        'id': id_rec,
                        'id_sensor': id_room,
                        'id_device': -1,
                        'type': "",
                        'light': bright,
                        'temp': temp,
                        'time': today,
                        'email': email,
                        'auto': True,
                        'status': '',
                        'level': -1}
                    database.db_rec().push(data_rec)

                # Update room information
                location_query = database.db_location().order_by_child(
                    'id').equal_to(id_room).get()
                for location in location_query.each():
                    database.db_location().child(location.key()).update(
                        {'temp': temp, 'light': bright})
        print("Nhan du lieu: " + feed_id + " " + payload)


client = MQTTClient(AIO_USERNAME, AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()

# kết nối cổng


def getPort():
    ports = serial.tools.list_ports.comports()
    N = len(ports)
    commPort = "None"
    for i in range(0, N):
        port = ports[i]
        strPort = str(port)
        if "USB Serial Device" in strPort:
            # if "USB Serial Device" in strPort:
            splitPort = strPort.split(" ")
            commPort = (splitPort[0])
    return commPort


isMicrobitConnected = False
if getPort() != "None":
    print("Microbit connected success.")
    ser = serial.Serial(port=getPort(), baudrate=115200)
    isMicrobitConnected = True


def processData(data):
    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")
    print(splitData)
    global temp1_button, light1_button

    if splitData[1] == "LIGHT" and splitData[0] == "1":
        client.publish("dadn.light1", splitData[2])

    if splitData[1] == "TEMP" and splitData[0] == "1":
        client.publish("dadn.temp1", splitData[2])


mess = ""


def readSerial():
    bytesToRead = ser.inWaiting()
    if (bytesToRead > 0):
        global mess
        mess = mess + ser.read(bytesToRead).decode("UTF-8")
        while ("#" in mess) and ("!" in mess):
            start = mess.find("!")
            end = mess.find("#")
            processData(mess[start: end + 1])
            if(end == len(mess)):
                mess = ""
            else:
                mess = mess[end+1:]

# Listen to record database for getting the recent record then change device state accordingly


def record_stream_handler(message):
    try:
        id_rec = database.db_rec().child(message["path"].split("/")[1]).get()
        auto_rec = " automatically" if id_rec.val()['auto'] else " manually"
        if id_rec.val()['type'] == "Fan":
            print("Fan set to "+str(id_rec.val()['level'])+auto_rec)
            client.publish("dadn.fan1", id_rec.val()['level'])
        elif id_rec.val()['type'] == "Light":
            print("Light set to "+str(id_rec.val()['status'])+auto_rec)
            client.publish("dadn.led1", 1 if id_rec.val()
                           ['status'] == "on" else 0)
    except:
        pass


auto = ''
email = ''
# Listen to user database for getting auto mode information


def user_stream_handler(message):
    global auto, email
    mess_split = message["path"].split("/")
    if len(mess_split) == 3:
        update_attribute = mess_split[2]
        id_user = mess_split[1]
        if update_attribute == 'last_login':
            auto = database.db_user().child(id_user).get().val()['auto']
            email = database.db_user().child(id_user).get().val()['email']
        elif update_attribute == 'auto':
            auto = message["data"]


def listen():
    rec_stream = database.db_rec().stream(record_stream_handler)
    auto_stream = database.db_user().stream(user_stream_handler)


database = Database.getDatabase()
listen()


while True:
    if isMicrobitConnected:
        readSerial()
    time.sleep(1)
