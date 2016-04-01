veraPDF-model
=============
The veraPDF Validation model described using a Domain Specific Language developed in [XText](https://eclipse.org/Xtext/).

Licensing
---------
The veraPDF Validation model is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

CI Status
---------
- [![Build Status](https://travis-ci.org/veraPDF/veraPDF-model.svg?branch=master)](https://travis-ci.org/veraPDF/veraPDF-model "veraPDF-model Travis-CI master branch build") Travis-CI: `master`

- [![Build Status](https://travis-ci.org/veraPDF/veraPDF-model.svg?branch=integration)](https://travis-ci.org/veraPDF/veraPDF-model "veraPDF-model Travis-CI integration build") Travis-CI: `integration`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-model-0.8)](http://jenkins.opf-labs.org/job/veraPDF-model-0.8/) OPF Jenkins: `release-0.8`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-model-0.9)](http://jenkins.opf-labs.org/job/veraPDF-model-0.9/) OPF Jenkins: `integration`

Pre-requisites
--------------
In order to generate the model classes you'll need:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

If you want to edit and regenerate the model you'll need:

 * The [veraPDF-model-syntax project](https://github.com/veraPDF/veraPDF-model-syntax), the [README](https://github.com/veraPDF/veraPDF-model-syntax/blob/master/README.md) gives instructions on how to install and use XText support in Eclipse.

Generating the veraPDF Model
----------------------------
 1. Download the veraPDF-model repository, either:
 ```
 git clone https://github.com/veraPDF/veraPDF-model
 ```
 or download the [latest tar archive](https://github.com/veraPDF/veraPDF-model/archive/master.tar.gz "veraPDF-Model latest GitHub tar archive") or [zip equivalent](https://github.com/veraPDF/veraPDF-model/archive/master.zip "veraPDF-Model latest GitHub zip archive") from GitHub.

 2. Move to the downloaded project directory, e.g.
 ```
 cd veraPDF-model
 ```
 3. Build and install using Maven:
 ```
 mvn clean install
 ```
