package com.otherService;

public class AgooService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
