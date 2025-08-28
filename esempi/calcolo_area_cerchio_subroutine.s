
# costante 1000 (0x3E8 in esadecimale) nel registro 4
lui $4 3
li $5 0xE8 
lui $6 0xFF
sub $5 $5 $6

add $4 $4 $5  # caricata la costante nel registro 4

# costante 3141 (0xC45 in esadecimale) nel registro 6
lui $6 0xC
li $7 0x45     # siccome < 0x80 non fa l'estensione di segno

add $6 $6 $7   # caricata la costante nel registro 6

li $1 2    # chiamata di funzione con raggio = 2
li $8 50
jalr $9 $8
# nel registro 2 controlla il risultato approssimato del calcolo area

li $1 5    # chiamata di funzione con raggio = 5
li $8 50
jalr $9 $8
# nel registro 2 controlla il risultato approssimato del calcolo area

li $1 0         # trap number for Halt
syscall         # halt

# calcola area cerchio
# registro 1 contiene l'input, valore del lato
# registro 2 contiene l'output - valore intero dell'area
@50
add $2 $0 $1
mul $2 $2 $2    
mul $2 $2 $6
div $2 $2 $4
jalr $0 $9
