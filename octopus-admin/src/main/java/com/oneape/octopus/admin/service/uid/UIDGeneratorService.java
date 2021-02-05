package com.oneape.octopus.admin.service.uid;

import com.oneape.octopus.dto.Id;

public interface UIDGeneratorService {

    long getUid();

    Id parseUid(long uId);
}
