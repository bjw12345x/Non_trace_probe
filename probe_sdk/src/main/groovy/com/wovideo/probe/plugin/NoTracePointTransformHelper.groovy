package com.wovideo.probe.plugin;



class NoTracePointTransformHelper {


    NoTracePointPluginParams extension
    /* SensorsAnalyticsSDKHookConfig sensorsAnalyticsHookConfig*/
    boolean disableSensorsAnalyticsMultiThread
    boolean disableSensorsAnalyticsIncremental
    boolean isHookOnMethodEnter
    HashSet<String> exclude = ['com.sensorsdata.analytics.android.sdk', 'android.support', 'androidx', 'com.qiyukf', 'android.arch', 'com.google.android']
    HashSet<String> include = ['butterknife.internal.DebouncingOnClickListener',
                               'com.jakewharton.rxbinding.view.ViewClickOnSubscribe',
                               'com.facebook.react.uimanager.NativeViewHierarchyManager']
    /** 将一些特例需要排除在外 */
    public static final HashSet<String> special = ['android.support.design.widget.TabLayout$ViewPagerOnTabSelectedListener',
                                                   'com.google.android.material.tabs.TabLayout$ViewPagerOnTabSelectedListener',
                                                   'android.support.v7.app.ActionBarDrawerToggle',
                                                   'androidx.appcompat.app.ActionBarDrawerToggle']


    NoTracePointTransformHelper(NoTracePointPluginParams extension) {
        this.extension = extension
    }


    void onTransform() {
        println("sensorsAnalytics {\n" + extension + "\n}")
        ArrayList<String> excludePackages = extension.exclude
        if (excludePackages != null) {
            exclude.addAll(excludePackages)
        }
        ArrayList<String> includePackages = extension.include
        if (includePackages != null) {
            include.addAll(includePackages)
        }
        createSensorsAnalyticsHookConfig()
    }

    private void createSensorsAnalyticsHookConfig() {
        //sensorsAnalyticsHookConfig = new SensorsAnalyticsSDKHookConfig()
        List<MetaProperty> metaProperties = SensorsAnalyticsSDKExtension.getMetaClass().properties
        for (it in metaProperties) {
            if (it.name == 'class') {
                continue
            }
            if (extension.sdk."${it.name}") {
                sensorsAnalyticsHookConfig."${it.name}"(it.name)
            }
        }
    }


    ClassNameAnalytics analytics(String className) {
        ClassNameAnalytics classNameAnalytics = new ClassNameAnalytics(className)
        if (classNameAnalytics.isSDKFile()) {
            def cellHashMap = sensorsAnalyticsHookConfig.methodCells
            cellHashMap.each {
                key, value ->
                    def methodCellList = value.get(className.replace('.', '/'))
                    if (methodCellList != null) {
                        classNameAnalytics.methodCells.addAll(methodCellList)
                    }
            }
            if (classNameAnalytics.methodCells.size() > 0 || classNameAnalytics.isSensorsDataAPI) {
                classNameAnalytics.isShouldModify = true
            }
        } else if (!classNameAnalytics.isAndroidGenerated()) {
            for (pkgName in special) {
                if (className.startsWith(pkgName)) {
                    classNameAnalytics.isShouldModify = true
                    return classNameAnalytics
                }
            }
            if (extension.useInclude) {
                for (pkgName in include) {
                    if (className.startsWith(pkgName)) {
                        classNameAnalytics.isShouldModify = true
                        break
                    }
                }
            } else {
                classNameAnalytics.isShouldModify = true
                if (!classNameAnalytics.isLeanback()) {
                    for (pkgName in exclude) {
                        if (className.startsWith(pkgName)) {
                            classNameAnalytics.isShouldModify = false
                            break
                        }
                    }
                }
            }
        }
        return classNameAnalytics
    }








}
