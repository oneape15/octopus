package com.oneape.octopus.service.serve.impl;

import com.oneape.octopus.dto.serve.ServeParamDTO;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 14:57.
 * Modify:
 */
public class LovTask {

    private ServeParamDTO pdo;
    private Runnable      runnable;

    public LovTask(ServeParamDTO pdo, Runnable runnable) {
        this.pdo = pdo;
        this.runnable = runnable;
    }

    @Override
    public String toString() {
        return "LovTask{" +
                "pdo=" + pdo +
                ", runnable=" + runnable +
                '}';
    }
}
