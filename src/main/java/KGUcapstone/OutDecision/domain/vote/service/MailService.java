package KGUcapstone.OutDecision.domain.vote.service;

import org.springframework.transaction.annotation.Transactional;

public interface MailService {
    @Transactional
    void sendNotification(String email, String title);
}
