# Modbus_URCap

## A tool modbus RTU URCap.
The URCap runs on port: 40408 on a daemon. Following script can be called when the URCap is installed. 
*	**init_tool_modbus(address)** where port is string and address is an int.
*	**tool_modbus_write(register_address, data)** both parameter is an int.
*	**tool_modbus_read(register_address)** parameter is an int.

The RS485 settings is controlled with the built in function
set\_tool\_communication(_enabled_,_baud\_rate_,_parity_,_stop\_bits_,_rx\_idle\_chars_,_tx\_idle\_chars_)

And tool supply voltage is controlled with 
set\_tool\_voltage(_voltage_)
