# Modbus_URCap

## A tool modbus RTU URCap.
The URCap runs on port: 40408 on a daemon. Following script functions is added by this URCap:
 
*	**init_tool_modbus(address)** where port is string and address is an int.
*	**tool_modbus_write(register_address, data)** both parameter is an int.
*	**tool_modbus_read(register_address)** parameter is an int.

The RS485 settings is controlled with the built in function:

    set_tool_communication(enabled,baud_rate,parity,stop_bits,rx_idle_chars,tx_idle_chars)


And tool supply voltage is controlled with:

    set_tool_voltage(voltage)

## An example script program (Bojke BL 400N):
    init_tool_modbus(1)

    if get_tool_digital_out(1) == True:
        textmsg("Setting range")
        var1 = tool_modbus_read(0,2)
        var2 = tool_modbus_increment(var1,50)
        var3 = tool_modbus_increment(var1,-50)
        var4 = tool_modbus_write(12,var2)
        var5 = tool_modbus_write(13,var3)
    end