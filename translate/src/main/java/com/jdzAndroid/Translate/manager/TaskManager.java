package com.jdzAndroid.Translate.manager;

import com.jdzAndroid.Translate.checkkey.CheckKey;
import com.jdzAndroid.Translate.checkkey.CheckKeyConfig;
import com.jdzAndroid.Translate.filterKey.RemoveKey;
import com.jdzAndroid.Translate.filterKey.RemoveKeyConfig;
import com.jdzAndroid.Translate.findkey.FindKey;
import com.jdzAndroid.Translate.findkey.FindKeyConfig;
import com.jdzAndroid.Translate.replaceKey.ReplaceKey;
import com.jdzAndroid.Translate.replaceKey.ReplaceKeyConfig;
import com.jdzAndroid.Translate.translate.Translate;
import com.jdzAndroid.Translate.translate.TranslateConfig;
import com.jdzAndroid.Translate.translateByKey.TranslateByKey;
import com.jdzAndroid.Translate.translateByKey.TranslateByKeyConfig;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TaskManager implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        TranslateConfig translateConfig = project.getExtensions().create("config", TranslateConfig.class);
        FindKeyConfig findKeyConfig = project.getExtensions().create("findKey", FindKeyConfig.class);
        RemoveKeyConfig removeKeyConfig = project.getExtensions().create("removeKey", RemoveKeyConfig.class);
        ReplaceKeyConfig replaceKeyConfig = project.getExtensions().create("replaceKey", ReplaceKeyConfig.class);
        TranslateByKeyConfig translateByKeyConfig = project.getExtensions().create("translateByKey", TranslateByKeyConfig.class);
        CheckKeyConfig checkKeyConfig = project.getExtensions().create("checkKey", CheckKeyConfig.class);
        project.getTasks().register("translate", task -> new Translate(translateConfig).translate());
        project.getTasks().register("findKey", task -> new FindKey(findKeyConfig).findKey());
        project.getTasks().register("removeKey", task -> new RemoveKey(removeKeyConfig).removeKey());
        project.getTasks().register("replaceKey", task -> new ReplaceKey(replaceKeyConfig).replaceKey());
        project.getTasks().register("translateByKey", task -> new TranslateByKey(translateByKeyConfig).translate());
        project.getTasks().register("checkKey", task -> new CheckKey(checkKeyConfig).startCheck());
    }

//    generateMetadataFileForGreetingsPluginPluginMarkerMavenPublication - Generates the Gradle metadata file for publication 'greetingsPluginPluginMarkerMaven'.
//    generateMetadataFileForPluginMavenPublication - Generates the Gradle metadata file for publication 'pluginMaven'.
//    generatePomFileForGreetingsPluginPluginMarkerMavenPublication - Generates the Maven POM file for publication 'greetingsPluginPluginMarkerMaven'.
//    generatePomFileForPluginMavenPublication - Generates the Maven POM file for publication 'pluginMaven'.
//    publish - Publishes all publications produced by this project.
//            publishGreetingsPluginPluginMarkerMavenPublicationToLocalPluginRepositoryRepository - Publishes Maven publication 'greetingsPluginPluginMarkerMaven' to Maven repository 'localPluginRepository'.
//    publishGreetingsPluginPluginMarkerMavenPublicationToMavenLocal - Publishes Maven publication 'greetingsPluginPluginMarkerMaven' to the local Maven repository.
//            publishPluginMavenPublicationToLocalPluginRepositoryRepository - Publishes Maven publication 'pluginMaven' to Maven repository 'localPluginRepository'.
//    publishPluginMavenPublicationToMavenLocal - Publishes Maven publication 'pluginMaven' to the local Maven repository.
//            publishToMavenLocal - Publishes all Maven publications produced by this project to the local Maven cac
}