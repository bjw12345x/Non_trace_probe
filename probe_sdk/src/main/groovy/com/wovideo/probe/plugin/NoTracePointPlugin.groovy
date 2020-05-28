package com.wovideo.probe.plugin

import com.android.build.api.dsl.extension.AppExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.invocation.DefaultGradle
import org.gradle.internal.reflect.Instantiator

/**
 * @author harvie
 */
class NoTracePointPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {

        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator)
        def args = [ins] as Object[]
        NoTracePointPluginParams extension =  project.extensions.create(ClassModifyUtil.CONFIG_NAME,NoTracePointPluginParams,args)

        //设置配置参数
        boolean disableSensorsAnalyticsPlugin = false
        boolean disableSensorsAnalyticsMultiThreadBuild = false
        boolean disableSensorsAnalyticsIncrementalBuild = false
        boolean isHookOnMethodEnter = false


        registerTransform(project)




    }

    def static registerTransform(Project project){

        AppExtension appExtension = project.extensions.findByType(AppExtension.class)

        BaseExtension extension = project.extensions.getByType(BaseExtension)
        NoTracePointTransform transform = new NoTracePointTransform(project)
        extension.registerTransform(transform)
    }
}