package com.emucoo.business_manager.widgets;

import java.util.List;

public interface MoreData<T> {
        //添加更多的数据
        void addMoreData(List<T> data, boolean hasMore);

        //将原来的数据clear，设置新的数据
        void setData(List<T> data, boolean hasMore);
    }
