package KGUcapstone.OutDecision.domain.notifications.service;

public interface NotificationService {
    boolean addNotifications(Long postId);
    boolean deleteNotifications(Long postId);
}