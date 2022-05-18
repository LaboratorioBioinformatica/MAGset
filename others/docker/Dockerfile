FROM bioconda/bioconda-utils-build-env:conda-4.8.3


RUN mkdir /databases
RUN mkdir /programs

WORKDIR /programs
RUN git clone https://github.com/aleimba/bac-genomics-scripts.git
RUN git clone https://github.com/lmrodriguezr/enveomics.git
RUN git clone --branch v3.13.0 https://github.com/sanger-pathogens/Roary.git

RUN curl -O https://phoenixnap.dl.sourceforge.net/project/bbmap/BBMap_38.86.tar.gz
RUN tar xvfz BBMap_38.86.tar.gz

WORKDIR /databases
RUN mkdir cog_files
WORKDIR /databases/cog_files/

RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cddid.tbl.gz 
RUN gunzip cddid.tbl.gz
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/./little_endian/Cog_LE.tar.gz
RUN tar xvfz Cog_LE.tar.gz
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/fun.txt 
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/whog 


COPY environment.yml /programs/environment.yml
RUN conda env update -f /programs/environment.yml

RUN gem install sqlite3-ruby

WORKDIR /databases
RUN mkdir dbcan
WORKDIR /databases/dbcan
RUN curl -O http://bcb.unl.edu/dbCAN2/download/Databases/CAZyDB.07312019.fa && diamond makedb --in CAZyDB.07312019.fa -d CAZy \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/dbCAN-HMMdb-V8.txt && mv dbCAN-HMMdb-V8.txt dbCAN.txt && hmmpress dbCAN.txt \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tcdb.fa && diamond makedb --in tcdb.fa -d tcdb \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tf-1.hmm && hmmpress tf-1.hmm \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tf-2.hmm && hmmpress tf-2.hmm \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/stp.hmm && hmmpress stp.hmm

COPY gb_to_faa.py /programs/gb_to_faa.py
COPY gb_to_fasta.py /programs/gb_to_fasta.py
COPY ani-matrix.bash /programs/enveomics/Examples/ani-matrix.bash
COPY magset-export.jar /programs/magset-export.jar
COPY magset.sh /programs/magset.sh

RUN chmod 755 /programs/magset.sh

WORKDIR /programs
CMD ["bash"]