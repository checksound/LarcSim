
li $4 0 # indice iterazione
li $5 1 # costante per incremento
li $6 5 # N° di iterazioni

li $9 100 # costante BASE stack
li $10 100 # puntatore a testa dello stack

# metti in ciclo
slt $7 $4 $6
beqz $7 10

li $1 1         # trap number for Print String
li $2 40        # String is stored at location 40
li $3 20        # maxlength for the string; larger than actual length is OK
syscall         # call the Print String trap

# ora leggi intero
li $1 4
syscall

sw $1 $10

add $10 $10 $5     # incrementa di uno il puntatore stack

add $4 $4 $5     # incrementa di uno l'indice
beqz $0 -12

# stampa il risultato
li $1 1         # trap number for Print String
li $2 60        # String is stored at location 60
li $3 20        # maxlength for the string; larger than actual length is OK
syscall

# stampa lo stack
sub $10 $10 $1    # sottrai di 1 

slt $7 $10 $9    # confronta puntatore base dello stack con il puntatore alla testa
bnez $7 8

# stampa numero puntato
li $1 2     # "print int" syscall
lw $2 $10   # carica il valore dall'indirizzo di memoria contenuto nerl registro 10
syscall 

# stampa nuova linea
li $1 1         # trap number for Print String
li $2 80        # String is stored at location 80
li $3 20        # maxlength for the string; larger than actual length is OK
syscall

beqz $0 -11

li $1 0         # trap number for Halt
syscall         # halt

             
@40             # store the following, starting at location 40
"Digita un numero\n

@60
"Output:\n

@80
"\n

@100 # spazio per stack

