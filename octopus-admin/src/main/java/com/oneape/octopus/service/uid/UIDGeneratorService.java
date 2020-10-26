package com.oneape.octopus.service.uid;

import com.oneape.octopus.model.dto.Id;

public interface UIDGeneratorService {

    long getUid();

    Id parseUid(long uId);
}
