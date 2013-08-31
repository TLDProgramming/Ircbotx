package com.github.thelonedevil.Ircbotx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

@SuppressWarnings("rawtypes")
public class MyBotMain extends ListenerAdapter implements Listener {

	@Override
	public void onMessage(MessageEvent event) {
		if (event.getMessage().startsWith("!stop")) {
			event.respond("I must go, my people need me!");
			shutdown();
		}
	}

	static PircBotX bot;
	static String name;
	static int last = 0;
	static File config = new File("config.txt");
	static String server;
	static int port;
	static String username;
	static String password;
	static String ircserver;
	static String channel;
	static Channel channel1;
	static String from;
	static boolean debug = false;

	public static void main(String[] args) throws Exception {
		loadConfig();
		// Now start our bot up.
		bot = new PircBotX();
		System.out.println("Bot Started");

		bot.setName(name);
		System.out.println("Name set: "+name);

		// Enable debugging output.
		bot.setVerbose(debug);
		System.out.println("Debug: "+debug);
		// Enable Listener
		bot.getListenerManager().addListener(new MyBotMain());
		System.out.println("Listener Enabled");

		// Connect to the IRC server.
		bot.connect(ircserver);
		System.out.println("Bot Connected");

		// Join the #pircbot channel.
		bot.joinChannel(channel);
		System.out.println("Bot joined Channel: "+ channel);
		channel1 = bot.getChannel(channel);

		Timer timer = new Timer();
		timer.schedule(new Task(), 1000, 60000);
	}

	static void loadConfig() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(config));

			String line;

			while ((line = br.readLine()) != null) {
				if (line.startsWith("Server:")) {
					server = line.split(":")[1].trim();
					//System.out.println("server");
				} else if (line.startsWith("Port:")) {
					port = Integer.parseInt(line.split(":")[1].trim());
					//System.out.println("port");
				} else if (line.startsWith("Username:")) {
					username = line.split(":")[1].trim();
					//System.out.println("user");
				} else if (line.startsWith("Password:")) {
					password = line.split(":")[1].trim();
					//System.out.println("pass");
				} else if (line.startsWith("Nick:")) {
					name = line.split(":")[1].trim();
					//System.out.println("nick");
				} else if (line.startsWith("IrcServer:")) {
					ircserver = line.split(":")[1].trim();
					//System.out.println("irc");
				} else if (line.startsWith("Channel:")) {
					channel = line.split(":")[1].trim();
					//System.out.println("chan");
				} else if (line.startsWith("Debug:")) {
					debug = line.split(":")[1].trim().equalsIgnoreCase("True");
					//System.out.println("debug");
				} else if (line.startsWith("From:")) {
					from = line.split(":")[1].trim();
					//System.out.println("from");
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	static HashMap<String, String> getMessages() throws IOException {
		EmailService email = new EmailService(server, port);
		ArrayList<String> message = email.connect(username, password);
		HashMap<String, String> messages = new HashMap<String, String>();
		boolean bool = false;
		boolean right = false;
		for (String s : message) {
			if (s.startsWith("From:")) {
				if (s.split("<")[1].split(">")[0].equalsIgnoreCase(from)) {
					right = true;
					// From: "Drone.io Build"<builds@drone.io>

				}

			} else if (right) {
				if (s.startsWith("Subject:")) {
					String subject = s.replace("Subject:", "");
					messages.put("Subject", subject);
					//System.out.println("1");
				} else if (s.startsWith("<p>Build")) {
					String build = s.split("=")[1].split(">")[0].replace("\"", "");
					messages.put("Build", build);
					//System.out.println("2");
				} else if (s.startsWith("<p>Author")) {
					String author = s.split(":")[1].replace("</p>", "");
					messages.put("Author", author);
					//System.out.println("3");
				} else if (s.startsWith("<p>Branch")) {
					String branch = s.split(":")[1].replace("</p>", "");
					messages.put("Branch", branch);
					//System.out.println("4");
				} else if (s.startsWith("<p>Message")) {
					bool = true;
				} else if (bool) {
					String message1 = s.replace("<p>", "").replace("</p>", "");
					messages.put("Message", message1);
					//System.out.println("5");
					bool = false;
				}
			}
		}

		/*
		 * <p>Author : Justin Wiblin</p>
		 * <p>Branch : master</p>
		 * <p>Message:</p>
		 * <p>added missing overrides annotations</p>
		 */
		if (!messages.isEmpty()) {
			return messages;
		} else {
			return null;
		}
	}

	public static void shutdown() {
		bot.partChannel(channel1, "Among creatures born into chaos, a majority will imagine an order, a minority will question the order, and the rest will be pronounced insane. ");
		bot.disconnect();
		System.exit(0);
	}
}
