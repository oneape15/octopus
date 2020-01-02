package com.oneape.octopus.service.uid;

public interface WorkerIdAssigner {
    /**
     * Assign worker id for {@link DefaultUidGenerator}
     *
     * @return assigned worker id
     */
    long assignWorkerId();
}
