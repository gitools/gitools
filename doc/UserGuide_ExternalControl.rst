

External Control of Gitools
-------------------------------------------------

Michael P Schroeder



Table of Contents
-------------------------------------------------

`Overview of commands <#N10049>`__  `#  <#N10049>`__

`How to send a command to Gitols <#N100AD>`__  `#  <#N100AD>`__





As of version 1.7.0 Gitools can be addressed from outside of the application. At the moment there is one command availabe.

Gitools is listening to the port **50151** by default. So it is possible to send the commands to this port. Alternatively it is possible to change the port by editing the **ui.xml** file in the Gitools configuration folder. (For Linux and Mac OS X users this should be at **/.gitools**).



Overview of commands
-------------------------------------------------

Command 
-------------------------------------------------

Description 
-------------------------------------------------

Options 
-------------------------------------------------

as of version 
-------------------------------------------------

**load **\ matrix-file 

 Tells Gitools to load a file.

**--rows** ( **-r**) File rows annotations

**--cols** ( **-c**) File cols annotations

 1.7.0

version
-------------------------------------------------

 Responds Gitools version

 

 1.7.1



How to send a command to Gitols
-------------------------------------------------

There is several ways to send the commands listed above to Gitools. We will make three examples here:

Command line
-------------------------------------------------

With a terminal application that lets you execute command line you can create a new Gitools instance with command to execute upon startup.

$ gitools load /home/user/matrix-file.tdm --cols /home/user/col-annotations.tsv --rows /home/user/row-annotations.tsv 

HTTP
-------------------------------------------------

By HTTP it is possible to send the command like this:

$ http://localhost:50151/load?file =/home/user/matrix-file.tdm&cols =/home/user/col-annotations.tsv&rows =/home/user/row-annotations.tsv 

Python
-------------------------------------------------

Python or any other programming environment can make use of the  `Telnet <http://en.wikipedia.org/wiki/Telnet>`__  internet protocol. See below to understand how python can connect to Gitools and send it a command.

import socket

gitools = socket .socket(socket .AF\_INET, socket .SOCK\_STREAM)

gitools .connect(( ’localhost’, 50151))

command = ”load /home/user/matrix-file.tdm --cols /home/user/col-annotations.tsv --rows /home/user/row-annotations.tsv”

gitools .send( command )

gitools .close()
