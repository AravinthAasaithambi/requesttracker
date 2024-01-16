package com.api.requesttracker.serviceimpl;

import com.api.requesttracker.entity.Requests;
import com.api.requesttracker.entity.User;
import com.api.requesttracker.repository.RequestRepository;
import com.api.requesttracker.repository.UserRepository;
import com.api.requesttracker.services.EmailService;
import com.api.requesttracker.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MailServiceImpl extends EmailService {

    private final UserRepository userRepository;
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    @Autowired
    private EmailService emailService;

    public MailServiceImpl(UserRepository userRepository, RequestService requestService, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestService = requestService;
        this.requestRepository = requestRepository;
    }

    public void requestAssignEmail(Requests request){
        String emailContent =
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "    <title>Request Assignment Notification</title>" +
                        "</head>" +
                        "<body>" +
                        "    <p>Hello [[User's Name]],</p>" +
                        "    <p>We want to inform you that a request has been assigned to you. Below are the details:</p>" +
                        "    <ul>" +
                        "        <li><strong>Request ID:</strong> [[Request ID]]</li>" +
                        "        <li><strong>Description:</strong> [[Request Description]]</li>" +
                        "        <li><strong>Status:</strong> [[Request Status]]</li>" +
                        "        <li><strong>Priority:</strong> [[Request Priority]]</li>" +
                        "        <li><strong>Assigned By:</strong> [[Assigned By]]</li>" +
                        "        <li><strong>Created Date:</strong> [[Created Date]]</li>" +
                        "        <li><strong>Updated Date:</strong> [[Updated Date]]</li>" +
                        "    </ul>" +
                        "    <p>Thank you for your attention. Please feel free to reach out if you have any questions or concerns.</p>" +
                        "    <p>Best regards,\n" +
                        "    Request Tracker Application</p>" +
                        "</body>" +
                        "</html>";

        List<User> user = userRepository.findAll();
        Long requestId = request.getRequesterID();
        Long assignedTo = request.getAssignedToID();
        String assignedByName = user.stream().filter(a -> a.getId().equals(requestId)).findFirst().map(User::getUsername).orElse("Unknown Assigned By Name");
        String assignedtoName = user.stream().filter(a -> a.getId().equals(assignedTo)).findFirst().map(User::getUsername).orElse("No Users");
        String email= user.stream().filter(a -> a.getId().equals(assignedTo)).findFirst().map(User::getEmail).orElse("No Users");
        //String email = "aravinth.a@telliant.net";
        String replacedEmailContent = emailContent
                .replace("[[User's Name]]", assignedtoName)
                .replace("[[Request ID]]", request.getRequestID().toString())
                .replace("[[Request Description]]", request.getDescription())
                .replace("[[Request Status]]", request.getStatus().name())
                .replace("[[Request Priority]]", request.getPriority().name())
                .replace("[[Assigned By]]", assignedByName)
                .replace("[[Created Date]]", request.getCreatedAt().toString())
                .replace("[[Updated Date]]", request.getUpdatedAt().toString());
        try {
            emailService.sendEmail(email,"Request Assignment",replacedEmailContent);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void newRequestToAdmin(Requests request){
        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>New Request Notification</title>" +
                "</head>" +
                "<body>" +
                "    <p>Hello Admin,</p>" +
                "    <p>A new request has been created. Below are the details:</p>" +
                "    <ul>" +
                "        <li><strong>Request ID:</strong> [[Request ID]]</li>" +
                "        <li><strong>Description:</strong> [[Request Description]]</li>" +
                "        <li><strong>Status:</strong> [[Request Status]]</li>" +
                "        <li><strong>Priority:</strong> [[Request Priority]]</li>" +
                "        <li><strong>Requested By:</strong> [[Created By]]</li>" +
                "        <li><strong>Created Date:</strong> [[Created Date]]</li>" +
                "        <li><strong>Updated Date:</strong> [[Updated Date]]</li>" +
                "    </ul>" +
                "    <p>Thank you for your attention. Please take appropriate action.</p>" +
                "    <p>Best regards,\n" +
                "    Request Tracker Application</p>" +
                "</body>" +
                "</html>";
        List<User> user = userRepository.findAll();
        Long requestId = request.getRequesterID();
        Long assignedTo = request.getAssignedToID();
        String assignedByName = user.stream().filter(a -> a.getId().equals(requestId)).findFirst().map(User::getUsername).orElse("Unknown Assigned By Name");
        String assignedtoName = user.stream().filter(a -> a.getId().equals(assignedTo)).findFirst().map(User::getUsername).orElse("No Users");
        String email= user.stream().filter(a -> a.getId().equals(requestId)).findFirst().map(User::getEmail).orElse("No Users");
        //String email = "aravinth.a@telliant.net";
        String replacedEmailContent = emailContent
                .replace("[[Request ID]]", request.getRequestID().toString())
                .replace("[[Request Description]]", request.getDescription())
                .replace("[[Request Status]]", request.getStatus().name())
                .replace("[[Request Priority]]", request.getPriority().name())
                .replace("[[Created By]]", assignedByName)
                .replace("[[Created Date]]", request.getCreatedAt().toString())
                .replace("[[Updated Date]]", request.getUpdatedAt().toString());
        try {
            emailService.sendEmail(email,"New Request Created",replacedEmailContent);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public void cancelRequest(Long rejectedId,Requests request){
    String emailContent =
            "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "    <title>Request Cancellation Notification</title>" +
                    "</head>" +
                    "<body>" +
                    "    <p>Hello Admin,</p>" +
                    "    <p>The following request has been canceled by a [[User]]:</p>" +
                    "    <ul>" +
                    "        <li><strong>Request ID:</strong> [[Request ID]]</li>" +
                    "        <li><strong>Description:</strong> [[Request Description]]</li>" +
                    "        <li><strong>Status:</strong> Canceled</li>" +
                    "        <li><strong>Cancelled By:</strong> [[Requested By]]</li>" +
                    "        <li><strong>Cancellation Date:</strong> [[Cancellation Date]]</li>" +
                    "    </ul>" +
                    "    <p>Thank you for your attention. Please review and take necessary action.</p>" +
                    "    <p>Best regards,\n" +
                    "    Request Tracker Application</p>" +
                    "</body>" +
                    "</html>";
    List<User> user = userRepository.findAll();
    Long requestId = request.getRequesterID();
    Long cancelledId = rejectedId;
    Long assignedTo = request.getAssignedToID();
    String assignedByName = user.stream().filter(a -> a.getId().equals(cancelledId)).findFirst().map(User::getUsername).orElse("Unknown Assigned By Name");
    String assignedtoName = user.stream().filter(a -> a.getId().equals(assignedTo)).findFirst().map(User::getUsername).orElse("No Users");
    String email= user.stream().filter(a -> a.getId().equals(assignedTo)).findFirst().map(User::getEmail).orElse("No Users");
    //String email = "aravinth.a@telliant.net";
    String replacedEmailContent = emailContent
            .replace("[[User]]",assignedByName)
            .replace("[[Request ID]]", request.getRequestID().toString())
            .replace("[[Request Description]]", request.getDescription())
            .replace("[[Request Status]]", request.getStatus().name())
            .replace("[[Request Priority]]", request.getPriority().name())
            .replace("[[Requested By]]", assignedByName)
            .replace("[[Cancellation Date]]", request.getUpdatedAt().toString());
        try {
        emailService.sendEmail(email,"Cancelled Request",replacedEmailContent);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
    }
}

}
