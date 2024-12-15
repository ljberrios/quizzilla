package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerAnswer;

import java.util.Arrays;

import static edu.uprb.quizzilla.util.Colors.*;

public class AnswerCommand implements Command {

    @Override
    public String getLabel() {
        return "answer";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session == null || !session.isAlive()) {
            print(RED + "Not connected to a server");
            return;
        }

        var answer = new StringBuilder();
        // reconstruct user input
        Arrays.stream(args).forEach(arg -> answer.append(arg.trim()).append(" "));
        // send answer
        session.sendPacket(new PacketPlayerAnswer(
                session.getID(), answer.toString().trim()));
    }
}
