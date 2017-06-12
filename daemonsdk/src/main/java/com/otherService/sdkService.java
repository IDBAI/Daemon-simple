package com.otherService;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-12 14:52.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class sdkService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
