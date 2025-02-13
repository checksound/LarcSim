
# il registro 1 contiene indirizzo della subroutine 
li $1 100

li $2 7

# il registro 9 dopo esecuzione jalr il valore del PC di ritorno
# il registro 1 contiene l'indirizzo della subroutine e viene caricato
# nel PC (Program Counter) 
jalr $9 $1

li $2 10
jalr $9 $1

li $2 3
jalr $9 $1

li $1 0         # trap number for Halt
syscall         # halt

# il registro 2 contiene il valore del lato
# il registro 10 contiene il risultato dell'operazione (valore dell'area)
@100
mul $10 $2 $2
jalr $0 $9