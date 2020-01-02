package com.oneape.octopus.service.uid;

import com.oneape.octopus.model.DTO.Id;

public interface UIDGeneratorService {

    long getUid();

    Id parseUid(long uId);
}
