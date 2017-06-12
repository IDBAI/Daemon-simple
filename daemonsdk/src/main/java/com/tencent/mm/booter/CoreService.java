package com.tencent.mm.booter;

import com.otherService.BaseService;

public class CoreService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
