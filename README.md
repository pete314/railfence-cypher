Running the project
=====================
The project is built for command line execution without any GUI.
The CypherBreakerRunner class is the entry point without any cli parameters.
On execution please follow the instruction on screen.

Implementation features
======================
The basic requirements for the project are followed and implemented. User has option to encrypt and decrypt messages.
There is two other features to perform file encryption and file decryption.

Technical features
================
The CypherBreaker uses a thread based decryption to perform the best score selection.
The same is used for the file based function, where all lines are handled as separate messages.
When performing a file decryption the file content are read and best score is chosen for each message and then the file is decrypted with highest score.

Known issues
================
Based on the cypher the file or message content containing spaces will result in a space less, chained string. Form this reason the original message can not be reproduced. 

UML
================
https://raw.githubusercontent.com/pete314/railfence-cypher/master/uml.png
