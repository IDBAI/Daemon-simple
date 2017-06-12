package com.tencent.tinker.lib.service;

import com.otherService.BaseService;

public class TinkerPatchService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

    public class InnerService extends BaseService {
        @Override
        public String getServieName() {
            return getClass().getSimpleName();
        }
    }
}
