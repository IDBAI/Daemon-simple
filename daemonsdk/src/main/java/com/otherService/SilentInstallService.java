package com.otherService;

/**
 * 仿造 kingroot 的服务
 */
public class SilentInstallService  extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
