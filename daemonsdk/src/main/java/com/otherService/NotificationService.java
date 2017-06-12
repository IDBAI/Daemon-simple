package com.otherService;

/**
 * 支付宝推送服务
 */
public class NotificationService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
}
