package org.android.agoo.accs;

import com.otherService.BaseService;

public class AgooService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

}
