package com.oneape.octopus.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Id implements Serializable {
    private long workId;
    private String timestamp;
    private long sequence;
}
