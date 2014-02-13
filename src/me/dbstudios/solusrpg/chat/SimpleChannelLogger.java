package me.dbstudios.solusrpg.chat;

public class SimpleChannelLogger implements ChannelLogger {
	private static final Logger logger = Logger.getLogger(SimpleChannelLogger.class.getName());
	private final Channel channel;

	public ChatChannelLogger(Channel channel) {
		this.channel = channel;


	}

	public void logChannelJoin(RpgPlayer player) {

	}
}