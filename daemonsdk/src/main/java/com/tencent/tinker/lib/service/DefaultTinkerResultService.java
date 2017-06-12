package com.tencent.tinker.lib.service;

import com.otherService.BaseService;

public class DefaultTinkerResultService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
