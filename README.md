# gradle翻译插件

该插件用于Android Studio中

[![Build Status][build-image]][build-url]
[![License: MIT][license-image]][license-url]
[![Code Coverage][coverage-image]][coverage-url]

[![Gradle plugin version][gradle-plugin-badge]][gradle-plugin-link]

## 开始

1，在你的modle的gradle文件中新增如下配置

apply plugin: 'com.jdzAndroid.Translate'

注意:config节点和android节点平级

config  
{    

    excelFilePath '......'//用于获取翻译的源excel文件路径
    
    languageCode 'en,de,es,fr,...'//需要翻译的国家语言代码
    
    column '1,2,3,4,...'//国家所对应的翻译在源excel文件中的列编号
    
    ignoreSplit '-'
    
    ignoreValue ',-，-%d-1'//在比较文本时候忽略的字符,默认-杠分开
    
    compareFilePath '......'//用于查找Key的xml文件
    
    outPath '......'//生成的翻译文件存储路径
    
    startLine 1//从源Excel第几行开始翻译
    
    replacedValue 'X'//需要被替换的内容
    
    newValue '%d'//被替换后的内容
 }

在项目gradle文件中添加如下依赖

dependencies 
{
     classpath 'com.jdz.translate:translate:1.0.0'
}
    

如果需要执行翻译任务只需make project即可


## Changelog
- `1.0.0` - initial version
