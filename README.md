# Gradle translation plug-in

This plug-in is used in Android studio

## Start

1，Add the following configuration in the gradle file of your module

apply plugin: 'com.jdzAndroid.CenterTask'

be careful:The translateConfig node is at the same level as the Android node

translateConfig
{    

    excelFilePath '......'//Source excel file path for obtaining translation
    
    languageCode 'en,de,es,fr,...'//Country language code to be translated
    
    column '1,2,3,4,...'//The column number of the translation corresponding to the country in the source excel file
    
    ignoreSplit '-'
    
    ignoreValue ',-，-%d-1'//Characters ignored when comparing text, default - separated by bars
    
    compareFilePath '......'//The XML file used to find the key
    
    outPath '......'//Storage path of generated translation file
    
    startLine 1//Start the translation from the row of the source excel
    
    replacedValue 'X'//Content to be replaced
    
    newValue '%d'//Replaced content
 }

Add the following dependencies to the project gradle file

dependencies 
{
     classpath 'com.jdz.translate:translate:1.0.0'
}

If you need to perform translation tasks, just make project

## Changelog
- `1.0.0` - initial version