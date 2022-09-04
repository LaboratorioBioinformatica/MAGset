FROM mambaorg/micromamba:0.25.1


RUN mkdir /home/$MAMBA_USER/databases
RUN mkdir /home/$MAMBA_USER/programs

COPY --chown=$MAMBA_USER:$MAMBA_USER environment-base.lock /tmp/environment-base.lock
COPY --chown=$MAMBA_USER:$MAMBA_USER environment-bowtie2.lock /tmp/environment-bowtie2.lock

RUN micromamba install -y -n base -f /tmp/environment-base.lock && \
    micromamba clean --all --yes
RUN micromamba create  -y -n bowtie2 -f /tmp/environment-bowtie2.lock && \
    micromamba clean --all --yes
    
ARG MAMBA_DOCKERFILE_ACTIVATE=1 

RUN cd /home/$MAMBA_USER/programs && git clone https://github.com/aleimba/bac-genomics-scripts.git
RUN cd /home/$MAMBA_USER/programs && curl -L -O -k https://master.dl.sourceforge.net/project/bbmap/BBMap_38.86.tar.gz
RUN cd /home/$MAMBA_USER/programs && tar xvfz BBMap_38.86.tar.gz

RUN cd /home/$MAMBA_USER/databases && mkdir cog_files

RUN cd /home/$MAMBA_USER/databases/cog_files && curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/cddid.tbl.gz \ 
    && gunzip cddid.tbl.gz \
    && curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/mmdb/cdd/./little_endian/Cog_LE.tar.gz \
    && tar xvfz Cog_LE.tar.gz \
    && curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/fun.txt \ 
    && curl -O ftp://ftp.ncbi.nlm.nih.gov/pub/COG/COG/whog 

  
RUN cd /home/$MAMBA_USER/databases && mkdir dbcan
RUN cd /home/$MAMBA_USER/databases/dbcan && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/CAZyDB.07312019.fa && diamond makedb --in CAZyDB.07312019.fa -d CAZy \
    && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/dbCAN-HMMdb-V8.txt && mv dbCAN-HMMdb-V8.txt dbCAN.txt && hmmpress dbCAN.txt \
    && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/tcdb.fa && diamond makedb --in tcdb.fa -d tcdb \
    && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/tf-1.hmm && hmmpress tf-1.hmm \
    && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/tf-2.hmm && hmmpress tf-2.hmm \
    && curl -O -L http://bcb.unl.edu/dbCAN2/download/Databases/stp.hmm && hmmpress stp.hmm

COPY --chown=$MAMBA_USER:$MAMBA_USER gb_to_faa.py /home/$MAMBA_USER/programs/gb_to_faa.py
COPY --chown=$MAMBA_USER:$MAMBA_USER gb_to_fasta.py /home/$MAMBA_USER/programs/gb_to_fasta.py
COPY --chown=$MAMBA_USER:$MAMBA_USER magset-export.jar /home/$MAMBA_USER/programs/magset-export.jar
COPY --chown=$MAMBA_USER:$MAMBA_USER magset.sh /home/$MAMBA_USER/programs/magset.sh

RUN chmod 755 /home/$MAMBA_USER/programs/magset.sh

WORKDIR /home/$MAMBA_USER/programs
CMD bash /home/$MAMBA_USER/programs/magset.sh /home/$MAMBA_USER/conf.properties