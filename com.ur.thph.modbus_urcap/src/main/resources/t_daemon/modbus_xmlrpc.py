#!/usr/bin/env python

import sys
import socket
import struct
import time
import logging as Logger
import os, sys
currentdir = os.path.dirname(os.path.realpath(__file__))
parentdir = os.path.dirname(currentdir)
sys.path.append(parentdir)
import minimalmodbus as modbus
import serial

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

isInitialized = False
LOCALHOST = "127.0.0.1"

instrument = None 
# value = ""
value = None

def isReachable():
  return True

def is_modbus_initialized():
  global isInitialized
  return isInitialized

def init_modbus_communication(slaveaddress):
  global instrument
  global isInitialized
  # instrument = modbus.Instrument('/dev/ttyUSB0',slaveaddress)
  instrument = modbus.Instrument('/dev/ttyTool',slaveaddress)
  instrument.serial.baudrate = 9600
  instrument.serial.bytesize = 8
  instrument.serial.parity = serial.PARITY_NONE
  instrument.serial.stopbits = 1
  instrument.serial.timeout = 1  # seconds
  isInitialized = True
  return True

def tool_modbus_write(register_address, data):
  if data is None:
    return "Modbus failed writing : Data is incorrect"

  data = float(data) * 1000 # scaling
  data = int(data)
  val1 = (data >> 16) & 0xFFFF  # Higher 16 bits
  val2 = data & 0xFFFF          # Lower 16 bits
  val = [val1,val2]
  try:	  
    global instrument
    # instrument.write_register(register_address,data,0)
    instrument.write_registers(register_address,val)
  except Exception:
    Logger.error("data: %s", data)
    Logger.error("val1: %s", val1)
    Logger.error("val2: %s", val2)
    Logger.error("Error in modbus write method", exc_info=True)
    return "Modbus failed writing"
  return "Succesfully executed!"

def tool_modbus_read(register_address, register_number):
  global value
  try:
    global instrument
    # value = int(instrument.read_register(register_address,0))
    val = instrument.read_registers(register_address,register_number,4)
    val_comb = (val[0] << 16) + val[1]
    # value = str(val_comb/1000) # + ';' + str(val[0] << 16) + ';' + str(val[1])
    value = val_comb / 1000 # scaling
  except Exception as e:
    Logger.error("Error in modbus read method", exc_info=True)
    # value = "Modbus falied reading"
    # value = str(e)
  return value

def tool_modbus_increment(data, inc):
  if data is not None:
    new_data = data + inc
    return new_data
  Logger.error("Error in modbus increment method", exc_info=True)
  return None


class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
    pass


# Connection related functions
server = MultithreadedSimpleXMLRPCServer((LOCALHOST, 40408))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
print "Listening on port 40408..."

server.register_function(isReachable,"isReachable")
server.register_function(init_modbus_communication,"init_modbus_communication")
server.register_function(tool_modbus_read,"tool_modbus_read")
server.register_function(tool_modbus_write,"tool_modbus_write")
server.register_function(tool_modbus_increment,"tool_modbus_increment")
server.register_function(is_modbus_initialized,"is_modbus_initialized")

server.serve_forever()

