============
Installation
============

.. contents::


Installing Gitools locally
-----------------------------

You can download the last version of Gitools from  `http://www.gitools.org/download <http://www.gitools.org/download>`__

Gitools depends on Java 7 or higher. Java must be installed in order to run Gitools. Once Java is set up, follow
the steps below.

* Download the latest Gitools version
* Uncompress the zip file into a folder (preferably in your Applications folder)
* Enter the Gitools-folder called where you find another folder called, ``bin``. *Depending on your operating system* you will have to click or double click one of the following files:
    * Windows: **gitools-2GB.bat**
    * Mac OS X: **gitools**
    * Linux: **gitools**


Setting up Java JDK
--------------------------------------

If your system doesn not have Java already installed, dwonload the latest version of
**Java 7 JDK**, from `www.oracle.com/technetwork/java/javase/downloads <www.oracle.com/technetwork/java/javase/downloads>`__.

Depending on your platform download the file for:

* Linux x64
* Mac OS X x64
* Windows x64

If your machine is not a 64-bit machine, download the x86 version

Depending on the platform the installation is different:

**Windows**

Install the .exe file

**OS X**

Install the .dmg file normally. Note that you need to install the JDK version of Java, otherwise Gitools
does not have access to the updated java.

**Linux**

* Extract the download Java file into a folder
* Open the file ``~/.bashrc`` in order to edit system variables:
    * Export the ``JAVA_HOME`` java installation folder as follows: ``export JAVA_HOME="/path/to/folder/jdk1.7.0_60``
    * Add the bin filder to your path as follows: ``export PATH = $JAVA_HOME/bin:$PATH``
    * Save the file and start gitools from a new terminal

Memory configuration
-----------------------

Gitools is a Java application, and by default it starts with the default memory requirements stablished by most of the Java applications (usually 256 Megabytes). But most probably you will need more than the default, you will realize this when you obtain an exception like this:

*java.lang.OutOfMemoryException*

There is a way to configure the memory limits for Gitools the same way that Java does, using the option *-Xmx*. But this configuration should be specified through an enviroment variable called **GITOOLS\_JAVA\_OPTS**. Let’s see some examples of how to do this with different operating systems:

**Windows**


- Right-click the **My Computer** icon on
- your desktop and select **Properties**.
- Click the Advanced tab. Click the
- Environment Variables button. Under System Variables, click New.
- Enter the variable name as **GITOOLS\_JAVA\_OPTS**.
- Enter the variable value as follows for 2 GB: **-Xmx2g**.
- Click OK.
- Click Apply Changes.

**Linux**

Imagine that you want to use 1024 megabytes of memory for Gitools, then edit the **~/.bashrc** file adding a line like this:

``export GITOOLS\_JAVA\_OPTS = ’-Xmx1024m’``

You can also specify 2 gigabytes like this:

``export GITOOLS\_JAVA\_OPTS = ’-Xmx2g’``

**Mac OS X**

Imagine that you want to use 1024 megabytes of memory for Gitools, then edit the **~/.profile** file adding a line like this:

``export GITOOLS\_JAVA\_OPTS =”-Xmx1024m”``

You can also specify 2 gigabytes like this:

``export GITOOLS\_JAVA\_OPTS =”-Xmx2g”``



Java Web Start
--------------

Java Web Start is a technology that allows running Java applications directly from the web. This method is very convenient to evaluate the tool, but when used for daily work and to use big amounts of data is better to install it locally.

In the  `main page of Gitools <http://www.gitools.org>`__  there is an orange button that says *Launch*. Simply click on it and the application will be downloaded and executed automatically.

It is very easy to use but sometimes the internet navigator doesn’t know how to communicate with Java and launch the application. You can use the following terminal command to launch it:

``javaws http://webstart.gitools.org/default/gitools.jnlp``




Adding Gitools to the command line  (Advanced)
-----------------------------------------------

**Mac OS X**

Imagine that you have uncompressed the zip file into */opt/gitools-1.5.0-bin*, then you can define the *PATH* variable in
the **~/.profile** file adding a line like this:

``export PATH =/opt/gitools-1.5.0-bin/bin: $PATH``

Start a new terminal and you should be able to run Gitools graphical interface typing:

``gitools``



**Linux**

Imagine that you have uncompressed the zip file into * /opt/gitools-1.5.0-bin*, then you can define the *PATH* variable
in the **~/.bashrc** file adding a line like this:

``export PATH =/opt/gitools-1.5.0-bin/bin: $PATH``

Start a new terminal and you should be able to run Gitools graphical interface typing:

``gitools``


**Windows**

- You need to have java installed. Go to this website to download and install;  `http://www.java.com/en/download/You <http://www.java.com/en/download/You>`__
- `http://www.java.com/en/download/You <http://www.java.com/en/download/You>`__  need to add path of java after set up; go here to do this:  `http://www.java.com/en/download/help/path.xml. <http://www.java.com/en/download/help/path.xml.>`__  This will tell your computer where to find the java program add these two paths:   **c:\\ java-directory\\ bin**  and **c:\\ java-directory\\ lib**  where c:\\ javadirectory will be the place where java is installed and  where the bin and lib directors are found in the java installation directory.
- You need to add the path of the Gitools bin directory also  in order to run the Gitools in the same path variable ( just like you added the other two paths) add ** c:\\ gitoolsdirectory\\ bin** and **c:\\ gitoolsdirectory\\ lib**  to the path variable. All path variables must be added to existing path (be careful not to delete anything which is already there) and separate each new path with a semicolon.  The new path can be added to the front of the existing path followed by a semicolon to separte other existing entries.
- To run the Gitools; just click on the gitools.bat file. You can make a shortcut for this file by righclick and say send to ‘desktop’ and it will creat a shortcut on your desktop for future use.




Source code
-----------

The code is located at `Github <http://www.github.com/gitools/gitools>`_ .

The last stable release is in the branch called ``master`` and the development code is in the branch ``develop``.

Download from github or checkout via the command ``git@github.com:gitools/gitools.git`` and compile it with Maven 3.

.. code-block:: bash

    # Compile and package
    mvn clean install assembly:assembly

    # Extract it and run
    cd target
    unzip gitools-2.0.0-bin.zip
    gitools-2.0.0/bin/gitools


