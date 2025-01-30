
li $4 0 # indice iterazione
li $5 1 # costante per incremento
li $6 5 # N° di iterazioni

li $10 0 # accumulatore

# metti in ciclo
slt $7 $4 $6
beqz $7 9
li $1 1         # trap number for Print String
li $2 40        # String is stored at location 40
li $3 20        # maxlength for the string; larger than actual length is OK
syscall         # call the Print String trap

# ora leggi intero e aggiungilo all'accumulatore
li $1 4
syscall

add $10 $10 $1

add $4 $4 $5
beqz $0 -11

# stampa il risultato
li $1 1         # trap number for Print String
li $2 60        # String is stored at location 60
li $3 20        # maxlength for the string; larger than actual length is OK
syscall

li $1 2        # "print int" syscall
add $2 $0 $10   # put the product into register 2, which holds the value to be printed
syscall
             
li $1 0         # trap number for Halt
syscall         # halt

             
@40             # store the following, starting at location 16
"Digita un numero\n

@60
"Output:\n


