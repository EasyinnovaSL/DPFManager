DPFManager
==========

DPF Manager: Digital Preservation Formats Manager (Image files)

DPF Manager is an open source modular TIFF conformance checker that is extremely easy to use, to integrate with existing and new projects, and to deploy in a multitude of different scenarios. It is designed to help archivists and digital content producers ensure that TIFF files are fit for long term preservation, and is able to automatically suggest improvements and correct preservation issues. The team developing it has decades of experience working with image formats and digital preservation, and has leveraged the support of 60+ memory institutions to draft a new ISO standard proposal (TIFF/A) specifically designed for long term preservation of still-images. An open source community will be created and grown through the project lifetime to ensure its continuous development and success. Additional commercial services will be offered to make DPF Manager self-sustainable and increase its adoption.

Licensing
---------
The DPF Manager is dual-licensed:

 - [GPLv3+](http://www.gnu.org/licenses/gpl-3.0.en.html "GNU General Public License, version 3")
 - [MPLv2+](https://www.mozilla.org/en-US/MPL/2.0/ "Mozilla Public License, version 2.0")

CI Status
---------
- [![Build Status](https://travis-ci.org/EasyinnovaSL/DPFManager.svg?branch=master)](https://travis-ci.org/EasyinnovaSL/DPFManager "DPFManager Travis-CI master branch build") Travis-CI: `master`

- [![Build Status](https://travis-ci.org/EasyinnovaSL/DPFManager.svg?branch=develop)](https://travis-ci.org/EasyinnovaSL/DPFManager "DPFManager Travis-CI develop build") Travis-CI: `develop`

Getting DPF Manager software
------------------------
#### Download release version
You can download an installer for the latest DPF Manager GUI release [from our download site](http://dpfmanager.org/#download).

#### Download latest development version
If you want to try the latest development version you can obtain it from our [development download site](http://dpfmanager.org/community.html).

#### Run DPF Manager
You can run the DPF Manager in two modes, GUI and CLI. To start the software in GUI mode just double-click the GUI executable. A manual for the GUI can be found in [our download site](http://dpfmanager.org/Downloads/User%20Manual.pdf).

For using the CLI in non-windows operating systems use the terminal and enter the following command which will explain the available parameters.

    dpf_manager -h

For Windows operating systems use the CLI executable named dpf-manager-console.exe instead. 

Building the DPF Manager from Source
----------------------------------------
### Pre-requisites
If you want to build the code from source you'll require:

 * Java 8, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
 * [Maven v3+](https://maven.apache.org/)

#### Downloading the latest release source
You can use [Git](https://git-scm.com/) to download the source code.
```
git clone https://github.com/EasyinnovaSL/DPFManager.git
```
or download the latest release from [GitHub] (https://github.com/EasyinnovaSL/DPFManager/releases).

#### Build local TIFF dependencies
The newest versions of three dependencies are not pushed to Maven Repository. This means that they have to be built locally.
The dependencies are available in the following repositories:
* [TIFF Implementation Checker](https://github.com/viaacode/TIFF-Implementation-Checker)
* [TIFF Policy Checker](https://github.com/viaacode/TIFF-Policy-Checker)
* [TIFF Library 4J](https://github.com/viaacode/Tiff-Library-4J)

Simply clone and run `mvn clean install` on all repositories.
Alternatively, if you don't want to build these dependencies locally, you can choose to downgrade each version of these dependencies in this repository's pom.xml.

#### Use Maven to compile the source
Move to the downloaded project directory and call Maven install:

    cd DPFManager
    mvn clean install

The executable and the installer will be generated under the directory target/jfx.

### Compiling with OpenJDK instead of OracleJDK
We recommend to compile the DPF Manager using the OracleJDK, since compiling the project with it, is straightforward.

However, if you want to use OpenJDK instead of OracleJDK, you will need to build with the [open version of OpenJDK that includes JavaFX](https://wiki.openjdk.java.net/display/OpenJFX/Building+OpenJFX) completely from source.