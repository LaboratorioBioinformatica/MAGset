from Bio import SeqIO
import sys

originalRecords = SeqIO.parse(sys.argv[1], "genbank")
modifiedRecords = []
for record in originalRecords:
	record.id = record.name
	modifiedRecords.append(record)	

SeqIO.write(modifiedRecords, sys.argv[2], "fasta")
