package net.trenterprises.diamondcore.cross.command.custom.exception;

public class ExistentCommandException extends InvalidCommandException {
	
	private static final long serialVersionUID = -5232430425828433545L;

	public ExistentCommandException(String commandName) {
		super("command \"" + commandName + "\" already taken");
	}

}
