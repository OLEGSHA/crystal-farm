package ru.windcorp.tge2.util.commands;

import java.util.List;

import ru.windcorp.tge2.util.StringUtil;

public class CommandDefaultCommandTree extends Command {

	public CommandDefaultCommandTree() {
		super(new String[] {"cmdtree", "commandTree", CommandSystem.GLOBAL_COMMAND_PREFIX + "cmdtree"},
				"Lists command hierarchy", "cmdtree");
	}

	@Override
	protected void execute(List<String> args, String alias, CommandRegistry registry) {
		StringBuilder sb = new StringBuilder();
		printLevel(0, registry, sb);
	}
	
	private void printLevel(int level, CommandRegistry registry, StringBuilder sb) {
		for (Command c : registry.getCommands()) {
			if (c instanceof SubCommandRegistry) {
				for (int i = 0; i < level; ++i) sb.append("- ");
				sb.append("# ");
				sb.append(c.getNames()[0]);
				
				write(StringUtil.resetStringBuilder(sb));
				printLevel(level + 1, ((SubCommandRegistry) c).getRegistry(), sb);
			} else {
				for (int i = 0; i < level + 1; ++i) sb.append("- ");
				sb.append(c.getNames()[0]);
				
				write(StringUtil.resetStringBuilder(sb));
			}
		}
	}

}
