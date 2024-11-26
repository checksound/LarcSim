# An Hello World program for the Larc Simulator
             
li $1 1         # trap number for Print String
li $2 16        # "Hello World" is stored at location 16
li $3 20        # maxlength for the string; larger than actual length is OK
syscall         # call the Print String trap
             
li $1 0         # trap number for Halt
syscall         # halt
             
@16             # store the following, starting at location 16
"Hello World