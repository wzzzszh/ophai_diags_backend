package com.itshixun.industy.fundusexamination.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesDTO {
    private HeatmapsDTO heatmaps;
    private VesselsDTO vessels;
    private DisksDTO disks;
    private OriginalDTO original;

}
