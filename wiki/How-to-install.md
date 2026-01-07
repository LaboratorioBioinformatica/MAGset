<h1>Requirements</h1>

* Docker (see https://docs.docker.com/install/ if it is not already installed in your system)
* 64-bit Linux system operation. We validated MAGset with the following distributions:
  * Ubuntu version 18.04 and 20.04
  * CentOS version 7.6 and 8.3
  * Fedora version 33
  * Debian version 9 and 10 (In version 10, there is a open issue about docker and Debian, please follow these steps [https://github.com/docker/for-linux/issues/58#issuecomment-735664418](https://github.com/docker/for-linux/issues/58#issuecomment-735664418) to use MAGset.)

* About 12GB of free space (mostly for docker image)


<h1>Installing </h1>

* Download the main script <br/>
`curl -OL https://github.com/LaboratorioBioinformatica/magset/releases/download/1.5.3/run-magset.sh`
<br/> or <br/>
`wget https://github.com/LaboratorioBioinformatica/magset/releases/download/1.5.3/run-magset.sh`
* Make the script executable <br/>
`chmod +x run-magset.sh`

That's it! Please test your installation following the [Quick start](Quick-start.md) tutorial.

<h1>Memory and execution time</h1>

The execution time and memory usage will vary  based on the data size, format file (GBFF or FASTA) and if the negative GRIs will be validated against the raw data (MAGcheck module).

Running the software with GBFF files will increase the memory/time considerably, because the pipeline with this type of file executes extra steps (pangenome and annotations).

In general, 8 GB of memory and 4 threads will be enough to execute comparisons with 4 bacterial genomes in a reasonable time.

The tables below show some examples of time/memory consumption, using Ubuntu 20.4 running in the cloud (digital ocean provider), Basic Plan (8 GB / 4 CPUs / 160 GB SSD Disk):
* **Data:**
  * Genomes compared: 4 genomes of approximately 3MB each
  * MAGcheck raw data: Illumina pair end,  50 GB  (MAGcheck data)

|   | FASTA without MAGcheck | FASTA with MAGcheck | GBK without MAGcheck | GBK with MAGcheck |
|---|---|---|---|---|
|Time (hh:mm)| 00:05 | 00:45 | 01:00 | 01:45 |
|Memory| 600MB | 600MB | 3.5 GB	| 3.5 GB |

* **Data:**
  * Genomes compared: 10 genomes of approximately 3MB each
  * No raw data

|   | FASTA without MAGcheck | GBK without MAGcheck |
|---|---|---|
|Time (hh:mm)| 00:35 | 04:00 |
|Memory| 850B | 4.5 GB |

<h1>Limitations</h1>
This software was built to run comparisons with no more than 10 genomes, always of the same species. Running comparisons with more than 10 genomes are not officially supported and can take a long time to execute or have memory issues.


