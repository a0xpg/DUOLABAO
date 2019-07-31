package com.hualong.duolabao.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Administrator on 2019-07-31.
 * <pre>
 *     ScoreInfo-积分信息
 * </pre>
 */
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain=true)
public class ScoreInfo {

    private Long amount;
    private Double totalScore;
    private Double score;
    private Boolean used;

    public ScoreInfo(Double totalScore) {
        this.totalScore = totalScore;
    }
}
