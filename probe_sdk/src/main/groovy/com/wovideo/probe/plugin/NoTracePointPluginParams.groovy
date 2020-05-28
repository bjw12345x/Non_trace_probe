package com.wovideo.probe.plugin


import org.gradle.api.Action
import org.gradle.internal.reflect.Instantiator


class NoTracePointPluginParams {
    //插件名称，随意
    String pluginName = 'ProbePlugin'
    /**
     * 需要修改的目标包名，默认主app module
     */
    public HashSet<String> targetPackages
    /**
     * 是否输出扫描文件
     */
    public boolean outputModifyFile = false

    public boolean isPrintLog = true

    /**
     * 接收拦截事件的类名(含包名的全路径)
     */
    public String targetClassName

   /**
     *
     */
    public boolean debug = false
    public boolean disableJar = false
    public boolean useInclude = false
    public boolean lambdaEnabled = true

    public ArrayList<String> exclude = []
    public ArrayList<String> include = []


    public ProbePluginSDKExtension sdk


    NoTracePointPluginParams(Instantiator ins) {

        sdk = ins.newInstance(ProbePluginSDKExtension)
    }

    void sdk(Action<? super ProbePluginSDKExtension> action) {
        action.execute(sdk)
    }


    @Override
    String toString() {

        StringBuilder excludeBuilder = new StringBuilder()
        int length = exclude.size()
        for (int i = 0; i < length; i++) {
            excludeBuilder.append("'").append(exclude.get(i)).append("'")
            if (i != length - 1) {
                excludeBuilder.append(",")
            }
        }

        StringBuilder includeBuilder = new StringBuilder()
        length = include.size()
        for (int i = 0; i < length; i++) {
            includeBuilder.append("'").append(include.get(i)).append("'")
            if (i != length - 1) {
                includeBuilder.append(",")
            }
        }

        return "\tdebug=" + debug + "\n" +
                "\tdisableJar=" + disableJar + "\n" +
                "\tuseInclude=" + useInclude + "\n" +
                "\tlambdaEnabled=" + lambdaEnabled + "\n" +
                "\texclude=[" + excludeBuilder.toString() + "]" + "\n" +
                "\tinclude=[" + includeBuilder.toString() + "]" + "\n" +
                "\tsdk {\n" + sdk + "\n" +
                "\t}"
    }
}