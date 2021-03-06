FROM bioconda/bioconda-utils-build-env:conda-4.8.3


RUN mkdir /databases
RUN mkdir /programs

WORKDIR /programs
RUN git clone https://github.com/aleimba/bac-genomics-scripts.git
RUN git clone https://github.com/lmrodriguezr/enveomics.git
RUN git clone --branch v3.13.0 https://github.com/sanger-pathogens/Roary.git

WORKDIR /databases
RUN mkdir cog_files
WORKDIR /databases/cog_files/

RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cddid.tbl.gz 
RUN gunzip cddid.tbl.gz
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/./little_endian/Cog_LE.tar.gz
RUN tar xvfz Cog_LE.tar.gz
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/fun.txt 
RUN curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/whog 

RUN conda update -y --all

RUN conda install -y -c conda-forge ruby=2.6.6
RUN conda install -y -c conda-forge gxx_linux-64=7.5.0
RUN conda install -y -c conda-forge matplotlib=3.2.2 seaborn=0.10.1
RUN conda install -y -c conda-forge biopython=1.77

RUN conda install -y -c r r-base=3.6.1


RUN conda install -y -c bioconda perl-bioperl=1.7.2
RUN conda install -y -c bioconda roary=3.13.0
RUN conda install -y -c bioconda diamond=0.9.24 hmmer=3.1b2 prodigal=2.6.3
RUN conda install -y -c bioconda java-jdk=8.0.112
RUN conda install -y -c bioconda mummer=3.23
RUN conda install -y -c bioconda bowtie2=2.4.2
RUN conda install -y -c bioconda samtools=1.10
RUN conda install -y -c bioconda emboss=6.6.0 
RUN conda install -y -c bioconda spades=3.15.0

RUN gem install sqlite3-ruby
RUN pip install run-dbcan==2.0.6 

WORKDIR /databases
RUN mkdir dbcan
WORKDIR /databases/dbcan
RUN curl -O http://bcb.unl.edu/dbCAN2/download/Databases/CAZyDB.07312019.fa && diamond makedb --in CAZyDB.07312019.fa -d CAZy \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/dbCAN-HMMdb-V8.txt && mv dbCAN-HMMdb-V8.txt dbCAN.txt && hmmpress dbCAN.txt \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tcdb.fa && diamond makedb --in tcdb.fa -d tcdb \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tf-1.hmm && hmmpress tf-1.hmm \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/tf-2.hmm && hmmpress tf-2.hmm \
    && curl -O http://bcb.unl.edu/dbCAN2/download/Databases/stp.hmm && hmmpress stp.hmm

WORKDIR /programs
RUN curl -O https://phoenixnap.dl.sourceforge.net/project/bbmap/BBMap_38.86.tar.gz
RUN tar xvfz BBMap_38.86.tar.gz

COPY magset.sh /programs/magset.sh
COPY magset-export.jar /programs/magset-export.jar
COPY gb_to_faa.py /programs/gb_to_faa.py
COPY ani-matrix.bash /programs/enveomics/Examples/ani-matrix.bash
WORKDIR /programs
RUN chmod 755 /programs/magset.sh

CMD ["bash"]