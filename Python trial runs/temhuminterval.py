#temperature and humidity readings program
#Created by Siim Salu x15003019 NCI
#CA 1 IOT Principles

#This program taken input from 5 separate sensors:
#1. dht sensor D4 - responsible of taking temperature and humidity readings
#2. rotary analog sensor A2 - responsible of setting interaval of when temp/humidity data is recorded and streamed to dweet.io minimum 1 sec, max 10 secs
#3. button D3 - when pressed and held keeps streaming/recording interval @ 1 sec, acts as a boost function
#4. led light D2 - when button pressed / boost active it is led up
#5. buzzer D6 - when record is boosted it buzzes for 0.25 seconds, if record is sent normally it buzzes for 0.5 seconds

#readings taken are stored in sqlite3 local db called sensordata.db, table readings
#5 columns are stored - temp, humidity, date time, interval and if button is pressed


import time
import datetime
import sqlite3
from grovepi import *
import dweepy

buzzer_pin = 6
led = 2										#assinging digital port 2 for led
frequency = 2;								#initial value of frequency of taking / processing / storing readings

def getDateTime():							#get current datetime
	return datetime.datetime.now()

def isPressed():							#getting if button is pressed value 0 or 1
	button = 3
	return digitalRead(button)

def getCurrentTime():						#getting current time in seconds
	return int(round(time.time()))

def getTemp():
	dht_sensor_port = 4
	[temp, hum] = dht(dht_sensor_port, 0)	#getting temperature value from dht sensor
	return temp
	
def getHumidity():							#getting humidity value from dht sensor
	dht_sensor_port = 4
	[temp, hum] = dht(dht_sensor_port, 0)
	return hum

def getFrequency():							#getting frequency value from rotary angle sensor, divived by 100 to get approximate second value
	potentiometer = 2	
	i = analogRead(potentiometer)/100
	if i < 1:
		i = 1
	else:
		i = i/2
	return i;

def getAllData():							#getting all data for dweet io
	data = {}
	data["temp"] = getTemp()
	data["humidity"] = getHumidity()
	data["interval"] = frequency*2
	data["datetime"] = str(getDateTime())
	data["isbtnpressed"] = isPressed()	
	return data
	
def insert(btnState):						#sqlite3 insert function
	conn = sqlite3.connect('/home/pi/Desktop/Project1/sensordata.db');
	cur = conn.cursor()
	cur.execute('''INSERT INTO readings(temp, humidity, date, interval, isbuttonpressed)
	VALUES(?,?,?,?,?)''',(getTemp(), getHumidity(), getDateTime(), getFrequency(), btnState,))
	conn.commit()
	conn.close()
	

def printData():							#sqlite3 get and print lines from db function
	conn = sqlite3.connect('/home/pi/Desktop/Project1/sensordata.db');
	cur = conn.cursor()
	cur.execute("SELECT * FROM readings")
	rows = cur.fetchall()
	for row in rows:
		print(row)
	conn.close()
		
starTime = getCurrentTime()

frequency = getFrequency()

print getAllData()							#on init - printing all for testing purpose


#two flows are possible
#1. if button is pressed, interval is set to 1 regardless of rotary sensor position and data is streamed after 1 second interval
#2. elseif button is not pressed, system is taking rotary sensor interval value for streaming/recording data 
while True:
	try:
		if isPressed():
			digitalWrite(led, 1)
			frequency = 1
			print getAllData()
			insert(1)
			dweepy.dweet_for('x15003019', getAllData())			#sending to dweet.io
			digitalWrite(buzzer_pin, 1)
			time.sleep(.25)
			digitalWrite(buzzer_pin, 0)
			frequency = 2
			time.sleep(2)
			digitalWrite(led,0)			
		elif starTime + frequency < getCurrentTime():			#executes when last broadcast time + interval time is smaller then current time 
			frequency = getFrequency()
			print getAllData()
			insert(0)
			dweepy.dweet_for('x15003019', getAllData())			#sending to dweet.io
			digitalWrite(buzzer_pin, 1)
			time.sleep(.5)
			digitalWrite(buzzer_pin, 0)			
			time.sleep(frequency)
			starTime = getCurrentTime()
			frequency = getFrequency()
			
	except KeyboardInterrupt:
		print "\n\nDatabase table can be found below"
		printData()
		digitalWrite(led,0)
		isPressed = 0
		break
	except (IOError, TypeError) as e:
		print "Error"
		

	
	
