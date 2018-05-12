package ru.windcorp.tge2.util.commands;

import java.util.ArrayList;
import java.util.Arrays;

import ru.windcorp.tge2.util.StringUtil;

public class CommandSystem implements Runnable {
	
	public static final char SEPARATOR = ' ';
	public static final CommandRegistry ROOT_COMMANDS = new CommandRegistry();
	public static final SubCommandRegistry GLOBAL_COMMANDS =
			new SubCommandRegistry(new String[] {"_global", "_globals", "_gl"}, "Global commands", "_global");
	public static final String GLOBAL_COMMAND_PREFIX = "_gl_";
	
	public static final CommandIO COMIO_LOG = new LogCommandIO(),
			COMIO_RAW = new RawCommandIO();
	
	public static CommandIO commandIO = COMIO_LOG;
	public static String commandFailureMsg = "Command %s in command call \"%s\" failed to execute: %s",
			commandNotFoundMsg = "Command \"%s\" not found",
			commandLoggedMsg = "-> %s";
	public static HelpCommand helpCommand = new CommandDefaultHelp();
	
	public static boolean logCommands = false;
	
	/**
	 * Change this to false to disable registration of default meta-commands.
	 */
	public static boolean registerDefaults = true;

	@Override
	public void run() {
		while (true) {
			runCommand(commandIO.readCommand());
		}
	}
	
	public static void runCommand(String commandCall) {
		if (logCommands) {
			commandIO.write(String.format(commandLoggedMsg, commandCall));
		}
		
		String[] parts = StringUtil.split(commandCall, SEPARATOR, 2);
		
		try {
			ROOT_COMMANDS.run(parts[0],
					(parts[1] == null)
							? new ArrayList<String>()
							: Arrays.asList(StringUtil.split(parts[1], SEPARATOR)));
		} catch (CommandFailedException e) {
			commandIO.writeError(String.format(commandFailureMsg,
					e.getCommand().getNames()[0],
					commandCall,
					e.getMessage()));
		}
	}
	
	private static void handleGlobals() {
		if (registerDefaults) {
			registerDefaultGlobals();
			registerDefaults = false;
		}
	}
	
	public static void start() {
		handleGlobals();
		new Thread(new CommandSystem(), "Command").start();
	}
	
	public static void startInThread() {
		handleGlobals();
		new CommandSystem().run();
	}
	
	public static void registerRoot(Command cmd) {
		ROOT_COMMANDS.register(cmd);
	}
	
	public static void registerGlobal(Command cmd) {
		GLOBAL_COMMANDS.getRegistry().register(cmd);
	}
	
	public static void registerDefaultGlobals() {
		registerGlobal(new CommandDefaultAlias());
		registerGlobal(new CommandDefaultCommandTree());
	}
	
	public static void showHelp(CommandRegistry registry) {
		helpCommand.showHelp(registry);
	}

}
