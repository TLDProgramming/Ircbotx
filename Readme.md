#IrcBot
This is a simple irc bot that can output the subject of an email message to an irc channel  
it uses [PircBotX] (http://code.google.com/p/pircbotx/ , "PircBotX")

##Usage
You need a config.txt file in the same directory as the ircbotx.jar file
The config.txt should look something like this;

```
# Lines starting with # are comments and will be ignored
# The order of these doesn't matter, this is juat the order i have it as
# The server has to be a pop3 server, and for best results have it set to not delete messages
Server: pop.example.com
# This is the pop3 server port
Port: 995
# The email address username, e.g. foo@bar.com would have the foo username
Username: foo
# The password to the email account
Password: bar
# The nick for the irc bot
Nick: Bot1 
# The irc server to join
IrcServer: irc.foobar.com
# The channel on the server for it to output messgaes to
Channel: #foobar
# Whether to set the bot to debug mode, outputs lots of stuff
Debug: false
# The email address that you want to see emails from, e.g. for drone.io notifications it would be build@drone.io
From: example@example.com

```

Then you just need to run the jar file with java, either from command line or from a batch/shell script