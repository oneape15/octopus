package com.oneape.octopus.model.DTO.report;

import com.oneape.octopus.commons.value.OptStringUtils;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.commons.value.TitleValueDTO;
import com.oneape.octopus.model.DO.report.ReportParamDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 19:01.
 * Modify:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportParamDTO extends ReportParamDO {

    private List<TitleValueDTO> values;
    private List<String>        dependOnList;


    public ReportParamDTO(ReportParamDO paramDO) {
        BeanUtils.copyProperties(paramDO, this);

        // parse the depend on param
        if (StringUtils.isNotBlank(getDependOn())) {
            List<String> list = OptStringUtils.split(getDependOn(), ";");
            if (CollectionUtils.isNotEmpty(list)) {
                dependOnList = new ArrayList<>();
                dependOnList.addAll(list);
            }
        }
    }

    /**
     * parse Lov kv map
     */
    public Map<String, String> parseLovKvName() {
        Map<String, String> map = new HashMap<>();
        String kvname = getLovKvName();
        if (StringUtils.isNotBlank(kvname)) {
            String[] kvList = StringUtils.split(kvname, ";");
            for (String tmp : kvList) {
                String[] kv = StringUtils.split(tmp, "=");
                if (kv != null && kv.length == 2) {
                    map.put(StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
                }
            }
        }
        return map;
    }
}
