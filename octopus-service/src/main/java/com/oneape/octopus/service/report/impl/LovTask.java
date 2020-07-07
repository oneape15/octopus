package com.oneape.octopus.service.report.impl;

import com.oneape.octopus.model.DO.report.ReportParamDO;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-07-07 14:57.
 * Modify:
 */
public class LovTask {

    private ReportParamDO pdo;
    private Runnable      runnable;

    public LovTask(ReportParamDO pdo, Runnable runnable) {
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
