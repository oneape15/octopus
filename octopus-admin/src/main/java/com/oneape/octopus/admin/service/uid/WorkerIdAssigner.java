package com.oneape.octopus.admin.service.uid;

public interface WorkerIdAssigner {
    /**
     * Assign worker id for {@link DefaultUidGenerator}
     *
     * @return assigned worker id
     */
    long assignWorkerId();
}
