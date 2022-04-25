package com.cxytiandi.foxmock.agent.matcher;

import com.cxytiandi.foxmock.agent.model.ClassInfo;

public interface ClassMatcher {

    /**
     * 类信息匹配
     * @param classInfo
     * @return
     */
    boolean match(ClassInfo classInfo);

}