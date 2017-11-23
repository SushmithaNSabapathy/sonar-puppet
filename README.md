[![Build Status](https://api.travis-ci.org/racodond/sonar-puppet-plugin.svg?branch=master)](https://travis-ci.org/racodond/sonar-puppet-plugin)
[![AppVeyor Build Status](https://ci.appveyor.com/api/projects/status/gl21787mqvxtwivm/branch/master?svg=true)](https://ci.appveyor.com/project/racodond/sonar-puppet-plugin/branch/master)
[![Quality Gate status](https://sonarcloud.io/api/badges/gate?key=com.racodond.sonarqube.plugin.puppet%3Apuppet)](https://sonarcloud.io/dashboard/index/com.racodond.sonarqube.plugin.puppet%3Apuppet)
[![Release](https://img.shields.io/github/release/racodond/sonar-puppet-plugin.svg)](https://github.com/racodond/sonar-puppet-plugin/releases/latest)


## SonarQube Puppet Analyzer
This [SonarQube](http://www.sonarqube.org) plugin analyzes [Puppet](https://puppet.com/) files and:

 * Computes metrics: lines of code, number of statements, etc.
 * Checks various guidelines to find out potential bugs, security issues and code smells through more than [60 checks](#available-rules)
 * Provides the ability to write your own checks


## Usage
1. [Download and install](http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) SonarQube
1. Install the Puppet plugin by a [direct download](https://github.com/racodond/sonar-puppet-plugin/releases). The  latest version is compatible with SonarQube 5.6+.
1. Install your [favorite scanner](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis) (SonarQube Scanner, Maven, Ant, etc.)
1. [Analyze your code](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis)


## Metrics

### Classes
Classes = Number of classes + Number of defines

### Functions
Functions = Number of resources (including default resource and resource override)

### Complexity
Complexity is increased by one for each: class, define, resource instance, resource default statement, resource override, if, elsif, unless, selector match, case match, and, or.


## Available Rules

* TODO