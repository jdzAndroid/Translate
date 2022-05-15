package com.jdzAndroid;

import com.jdzAndroid.DocTransformation.xml2xls.Tranformation;
import com.jdzAndroid.DocTransformation.xml2xls.TransformationConfig;
import com.jdzAndroid.Translate.TranslateConfig;
import com.jdzAndroid.Translate.TranslateTask;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CenterTask implements Plugin<Project> {
    private TranslateConfig mTranslateTranslateConfig;
    private TransformationConfig mTransformationConfig;

    @Override
    public void apply(Project project) {
        mTranslateTranslateConfig = project.getExtensions().create("config", TranslateConfig.class);
        mTransformationConfig = project.getExtensions().create("xml2xls", TransformationConfig.class);
        project.getTasks().register("xml2xls", task -> new Tranformation(mTransformationConfig).startWork());
        project.getTasks().register("translate", task -> new TranslateTask(mTranslateTranslateConfig).startWork());
    }
}