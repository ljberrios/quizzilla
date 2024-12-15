package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketGameChat;

import java.util.Arrays;

public class ChatCommand implements Command {

    @Override
    public String getLabel() {
        return "chat";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session != null && session.isAlive()) {
            var builder = new StringBuilder();
            // reconstruct user input
            Arrays.stream(args).forEach(arg -> builder.append(arg.trim()).append(" "));
            session.sendPacket(new PacketGameChat(session.getID(), builder.toString()));
        }
    }
}
