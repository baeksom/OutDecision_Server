package KGUcapstone.OutDecision.domain.vote.service;

public interface MailService {
    void sendNotification(String email, String title);
    void sendNotificationV2(String email, String title);
}
