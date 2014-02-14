package me.dbstudios.solusrpg.chat;

import java.io.File;
import java.nio.StandardOpenOption;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.dbstudios.solusrpg.SolusRpg;
import me.dbstudios.solusrpg.config.Configuration;
import me.dbstudios.solusrpg.config.Directories;

public class SimpleChannelLogger implements ChannelLogger {
	private static String msgFormat = "[%3$tF %3$tH:%3$tM:%3$tS] %1$s (%0$s): %2$s";
	private static String recFormat = "[%6$tF %6$tH:%6$tM:%6$tS] %4$f.2M -- %1$s (%0$s) -> %3$s (%2$s): %5$s";
	private static String joinFormat = "[%2$tF %2$tH:%2$tM:%2$tS] %1$s (%0$s) has joined the channel.";
	private static String leaveFormat = "[%2$tF %2$tH:%2$tM:%2$tS] %1$s (%0$s) has left the channel.";

	private final Channel channel;
	private final File logFile;

	private List<String> buffer = new ArrayList<>();

	public ChatChannelLogger(Channel channel) {
		this(channel, Directories.DATA_LOGS + "chat" + File.separator + channel.getName() + ".log");
	}

	public ChatChannelLogger(Channel channel, String logPath) {
		this.channel = channel;
		this.logFile = new File(logPath);

		if (!logFile.exists())
			try {
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			} catch (Exception e) {
				SolusRpg.log(Level.WARNING, String.format("Could not initialize chat logger for %s", channel.getName()));

				if (Configuration.is("logging.verbose"))
					e.printStackTrace();

				throw new CreationException("Could not create log file");
			}
	}

	public SimpleChannelLogger logChannelJoin(RpgPlayer player) {
		buffer.add(String.format(joinFormat, player.getDisplayName(), player.getName(), System.currentTimeMillis()));
	}

	public SimpleChannelLogger logChannelLeave(RpgPlayer player) {
		buffer.add(String.format(leaveFormat, player.getDisplayName(), player.getName(), System.currentTimeMillis()));
	}

	public SimpleChannelLogger logMessageSent(RpgPlayer sender, String message) {
		buffer.add(String.format(msgFormat, sender.getDisplayName(), sender.getName(), message, System.currentTimeMillis()));
	}

	public SimpleChannelLogger logMessageRecieved(RpgPlayer sender, RpgPlayer receiver, String message) {
		buffer.add(String.format(recFormat, sender.getDisplayName(), sender.getName(), receiver.getDisplayName(), receiver.getName(), message, System.currentTimeMillis()));
	}

	public SimpleChannelLogger flush() {
		try {
			Path.write(logFile.toPath(), this.buffer, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		} catch (Exception e) {
			SolusRpg.log(Level.SEVERE, String.format("Could not write log buffer to file (%s); please make sure the file exists and is writeable", logFile.getPath()));
		}

		return this.clean();
	}

	public SimpleChannelLogger clean() {
		buffer.clear();

		return this;
	}
}