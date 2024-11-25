package edu.uprb.quizzilla.network;

import java.io.Serializable;

/**
 * Abstraction for POJOs which will be sent between client and server,
 * which we call packets. Implements {@link Serializable} to be able to
 * send them through object input/output streams.
 */
public interface Packet extends Serializable {}
