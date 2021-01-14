class AniResults {
    constructor(genomeName, results) {
        this.genomeName = genomeName;
        this.results = results;
    }
}

class SummaryResults {
    constructor(genomeName, genesQty, specificGenesQty, sharedGenesQty, coreGenesQty, discartedGenes, grisQty, genesInGrisQty, grisFoundByMAGCheckQty, genesInGRIsFoundByMAGCheckQty, cogQty, cazyQty) {
        this.genomeName = genomeName;
        this.genesQty = genesQty;
        this.specificGenesQty = specificGenesQty;
        this.sharedGenesQty = sharedGenesQty;
        this.coreGenesQty = coreGenesQty;
        this.discartedGenes = discartedGenes;
        this.grisQty = grisQty;
        this.genesInGrisQty = genesInGrisQty;
        this.grisFoundByMAGCheckQty = grisFoundByMAGCheckQty;
        this.genesInGRIsFoundByMAGCheckQty = genesInGRIsFoundByMAGCheckQty;
        this.cogQty = cogQty;
        this.cazyQty = cazyQty;
    }
}

class PangenomeGene {
    constructor(geneName, annotation, numberOfIsolates, genomeGeneNames) {
        this.geneName = geneName;
        this.annotation = annotation;
        this.numberOfIsolates = numberOfIsolates;
        this.genomeGeneNames = genomeGeneNames;
    }
}

class GRI {
    constructor(id, genomeName, sequenceName, size, start, end, genesQty, coveredPositions, coverage, meanDepth, magCheckAlert) {
        this.id = id;
        this.genomeName = genomeName;
        this.sequenceName = sequenceName;
        this.size = size;
        this.start = start;
        this.end = end;
        this.genesQty = genesQty;
        this.coveredPositions = coveredPositions;
        this.coverage = coverage;
        this.meanDepth = meanDepth;
        this.magCheckAlert = magCheckAlert;
    }
}

class GRIGene {
    constructor(griName, genomeName, type, locusTag, product, proteinId, min, max, strand, parent) {
        this.griName = griName;
        this.genomeName = genomeName;
        this.type = type;
        this.locusTag = locusTag;
        this.product = product;
        this.proteinId = proteinId;
        this.min = min;
        this.max = max;
        this.strand = strand;
        this.parent = parent;
    }
}

class CogGene {
    constructor(locusTag, cogId, cogDescription) {
        this.locusTag = locusTag;
        this.cogId = cogId;
        this.cogDescription = cogDescription;
    }
}

class CazyGene {
    constructor(locusTag, cazyCodes) {
        this.locusTag = locusTag;
        this.cazyCodes = cazyCodes;
    }
}

class MatrixGene {
    constructor(genomeName, locusTag, type, product, proteinId, min, max, strand, parent, pangenomeGeneName, core, shared, specific, rgiName, cogId, cogName, cazyCodes, magCheckAlert) {
        this.genomeName = genomeName;
        this.type = type;
        this.locusTag = locusTag;
        this.product = product;
        this.proteinId = proteinId;
        this.min = min;
        this.max = max;
        this.strand = strand;
        this.parent = parent;
        this.pangenomeGeneName = pangenomeGeneName;
        this.core = core;
        this.shared = shared;
        this.specific = specific;
        this.rgiName = rgiName;
        this.cogId = cogId;
        this.cogName = cogName;
        this.cazyCodes = cazyCodes;
        this.magCheckAlert = magCheckAlert;
    }
}

class Filter {
    constructor(filterType, filterField, filterComparatorType, filterContent) {
        this.filterType = filterType;
        this.filterField = filterField;
        this.filterComparatorType = filterComparatorType;
        this.filterContent = filterContent;
    }
}
