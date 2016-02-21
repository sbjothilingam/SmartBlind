Smart Blind an Android Application which communicates with the Raspberry pi to get the temperature, ambient and blind status of the room
and change the blind status according the rule specified in FuzzyLogic when temperature and ambient changes. Notification from the pi is received 
when there is change in temperature to a specified degree. User can edit, add and delete fuzzy logic rules. JSONrpc is used for communication between 
Android application and Raspberry pi.

Instruction:

1)Start Server
	-Navigate to "PervasiveCourse_Student" folder.
	-Type "ant build"
	-Type "ant JsonRpcServer"
	-Server will be started on port 8080.

2)Import the Project1 folder as an Android Application Project

3)Install and launch the application.