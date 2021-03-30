# Modbus_URCap

## A tool modbus RTU URCap.
The URCap runs on port: 40408 on a daemon. Following script functions is added by this URCap:
 
*	**init_tool_modbus(address)** where port is string and address is an int.
*	**tool_modbus_write(register_address, data)** both parameter is an int.
*	**tool_modbus_read(register_address)** parameter is an int.

The RS485 settings is controlled with the built in function
```python
set_tool_communication(enabled,baud_rate,parity,stop_bits,rx_idle_chars,tx_idle_chars)
```

And tool supply voltage is controlled with
```python
set_tool_voltage(voltage)
```
