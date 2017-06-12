package org.rome.android.ipp.binder;

import com.otherService.BaseService;

/**
 * 阿里系列的隐式唤醒服务
 */
public class IppService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
