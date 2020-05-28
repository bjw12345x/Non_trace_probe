package com.wovido.probe_sdk;

import com.wovido.probe_sdk.Lifecycle.LifecycleOnCreateMethodVisitor;
import com.wovido.probe_sdk.Lifecycle.LifecycleOnDestroyMethodVisitor;
import com.wovido.probe_sdk.Onclick.OnclickVistor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PronbeClassVistor extends ClassVisitor implements Opcodes {


    private String mClassName;

    public PronbeClassVistor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access,name, signature, superName, interfaces);

        this.mClassName=name;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        System.out.println("ClassVisitor : mClassName ----> " + mClassName);

        System.out.println("ClassVisitor : method ----> " + name);
        if("onClick".equals(name)){
                return new OnclickVistor(mv);
            }

        //匹配FragmentActivity
        if ("android/support/v4/app/FragmentActivity".equals(this.mClassName)) {

            System.out.println("LifecycleClassVisitor : change mClassName ----> " + mClassName);

            System.out.println("LifecycleClassVisitor : change method ----> " + name);

            if ("onCreate".equals(name) ) {
                //处理onCreate
                return new LifecycleOnCreateMethodVisitor(mv);
            } else if ("onDestroy".equals(name)) {
                //处理onDestroy
                return new LifecycleOnDestroyMethodVisitor(mv);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {

        super.visitEnd();
    }
}
