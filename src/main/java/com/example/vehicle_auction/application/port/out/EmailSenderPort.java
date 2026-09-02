package com.example.vehicle_auction.application.port.out;

public interface EmailSenderPort {
    /**
     * Sends a simple text email.
     *
     * @param to      The recipient's email address
     * @param subject The subject line of the email
     * @param body    The plain text body of the email
     */
    void sendEmail(String to, String subject, String body);
}
