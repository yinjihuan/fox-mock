package com.cxytiandi.foxmock.agent.express;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import ognl.*;

public class OgnlExpress implements Express {

    private static final MemberAccess MEMBER_ACCESS = new DefaultMemberAccess(true);
    private static final Logger logger = LoggerFactory.getLogger(OgnlExpress.class);
    private static final ObjectPropertyAccessor OBJECT_PROPERTY_ACCESSOR = new ObjectPropertyAccessor();

    private Object bindObject;
    private final OgnlContext context;

    public OgnlExpress() {
        OgnlRuntime.setPropertyAccessor(Object.class, OBJECT_PROPERTY_ACCESSOR);
        context = new OgnlContext();
        context.setMemberAccess(MEMBER_ACCESS);
    }

    @Override
    public Object get(String express) throws OgnlExpressException {
        try {
            return Ognl.getValue(express, context, bindObject);
        } catch (Exception e) {
            logger.error("Error during evaluating the expression:", e);
            throw new OgnlExpressException(express, e);
        }
    }

    @Override
    public boolean is(String express) throws OgnlExpressException {
        final Object ret = get(express);
        return ret instanceof Boolean && (Boolean) ret;
    }

    @Override
    public Express bind(Object object) {
        this.bindObject = object;
        return this;
    }

    @Override
    public Express bind(String name, Object value) {
        context.put(name, value);
        return this;
    }

    @Override
    public Express reset() {
        context.clear();
        context.setMemberAccess(MEMBER_ACCESS);
        return this;
    }
}
