package edu.uprb.quizzilla.network;

import java.io.Serializable;

/**
 * Abstraction for packets. Implements {@link Serializable} to
 * be able to send them through object input/output streams.
 */
public interface Packet extends Serializable {}
