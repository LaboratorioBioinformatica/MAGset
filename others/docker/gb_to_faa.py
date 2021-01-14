from Bio import SeqIO
import sys

file_name = str(sys.argv[1])

# stores all the CDS entries
all_entries = []
with open(file_name, 'r') as GBFile:
	GBcds = SeqIO.InsdcIO.GenBankCdsFeatureIterator(GBFile)
	for cds in GBcds:
		if cds.seq is not None:
			cds.id = cds.name
			cds.description = ''
			all_entries.append(cds)

SeqIO.write(all_entries, str(sys.argv[2]), 'fasta')