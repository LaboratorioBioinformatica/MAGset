package edu.fbs.magset.fixed_gff;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(onlyExplicitlyIncluded = true)
public class FixedGFFFile {

	private String path;
	private Map<String, List<String>> locusTagVersusNewIdsMap;
	public FixedGFFFile(String path, Map<String, List<String>> locusTagVersusNewIdsMap) {
		super();
		this.path = path;
		this.locusTagVersusNewIdsMap = locusTagVersusNewIdsMap;
	}

	public List<String> getNewIds(String locusTag){
		return locusTagVersusNewIdsMap.get(locusTag);
	}
	
}
