package com.cxytiandi.foxmock.agent.model;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class ClassInfo {
    private final String className;
    private final byte[] classFileBuffer;
    private final ClassLoader loader;


    public ClassInfo(String className, byte[] classFileBuffer, ClassLoader loader) {
        this.className = formatClassName(className);
        this.classFileBuffer = classFileBuffer;
        this.loader = loader;
    }

    public String getClassName() {
        return className;
    }

    private CtClass ctClass;

    public CtClass getCtClass() throws IOException {
        if (ctClass != null) {
            return ctClass;
        }

        final ClassPool classPool = new ClassPool(true);
        if (loader == null) {
            classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
        } else {
            classPool.appendClassPath(new LoaderClassPath(loader));
        }

        final CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();

        this.ctClass = clazz;
        return clazz;
    }

    private boolean modified = false;

    public boolean isModified() {
        return modified;
    }

    public void setModified() {
        this.modified = true;
    }

    public ClassLoader getClassLoader() {
        return loader;
    }

    private static String formatClassName(String className) {
        return className.replace('/', '.');
    }

    public byte[] getClassFileBuffer() {
        return classFileBuffer;
    }
}
