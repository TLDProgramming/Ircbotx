package com.github.thelonedevil.Ircbotx;

import java.io.IOException;
import java.util.*;

class Task extends TimerTask {

    // run is a abstract method that defines task performed at scheduled time.
    @Override
    public void run() {
	try {
	    HashMap<String, String> messages = MyBotMain.getMessages();
	    //System.out.println("message parsed");
	    if (messages != null) {
	    	//System.out.println("message not null");
		String subject = messages.get("Subject");
		String build = messages.get("Build");
		String author = "Author:" + messages.get("Author");
		String branch = "Branch:" + messages.get("Branch");
		String message = "Commit Message: " + messages.get("Message");
		MyBotMain.bot.sendMessage(MyBotMain.channel1, subject);
		//System.out.println("subject sent");

		MyBotMain.bot.sendMessage(MyBotMain.channel1, build);
		//System.out.println("build sent");
				
		MyBotMain.bot.sendMessage(MyBotMain.channel1, author);
		//System.out.println("author sent");
				
		MyBotMain.bot.sendMessage(MyBotMain.channel1, branch);
		//System.out.println("branch sent");
				
		MyBotMain.bot.sendMessage(MyBotMain.channel1, message);
		//System.out.println("message sent");
				
	    }
	} catch (IOException e) {
	    e.printStackTrace(System.out);
	}
    }
}
