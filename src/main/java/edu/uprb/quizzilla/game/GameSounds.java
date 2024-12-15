package edu.uprb.quizzilla.game;

import javax.sound.sampled.AudioSystem;

public enum GameSounds {

    POP("pop.wav"),
    HAT("hat.wav"),
    FW_TWINKLE("twinkle_far1.wav"),
    FW_LAUNCH("launch1.wav"),
    FW_BLAST("blast_far1.wav"),
    START("start.wav");

    private final String fileName;

    GameSounds(String fileName) {
        this.fileName = fileName;
    }

    public void playSound() {
        Thread.startVirtualThread(() -> {
            try {
                var in = getClass().getClassLoader()
                        .getResourceAsStream("sounds/" + fileName);
                if (in != null) {
                    var clip = AudioSystem.getClip();
                    var inputStream = AudioSystem.getAudioInputStream(in);
                    clip.open(inputStream);
                    clip.start();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
