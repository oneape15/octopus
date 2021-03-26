package com.oneape.octopus.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created 2019-06-12 19:35.
 * Modify:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pair<L, R> implements Serializable {
    private L left;
    private R right;
}
