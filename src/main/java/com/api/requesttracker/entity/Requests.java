package com.api.requesttracker.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Requests")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID")
    private Long requestID;

    @Column(name = "Title", length = 100, nullable = false)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;
    public enum Priority {
        Low, Medium, High
    }
    public enum Status {
        Open, InProgress, Resolved, Closed
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "Priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    @Column(name = "RequesterID", nullable = false)
    private long requesterID;

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(long requesterID) {
        this.requesterID = requesterID;
    }

    public Long getAssignedToID() {
        return assignedToID;
    }

    public void setAssignedToID(Long assignedToID) {
        this.assignedToID = assignedToID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "AssignedToID")
    private Long assignedToID;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;


    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public User getRejectedByUser() {
        return rejectedByUser;
    }

    public void setRejectedByUser(User rejectedByUser) {
        this.rejectedByUser = rejectedByUser;
    }

    public String getImageType() {
        return imageType;
    }

    @Column(nullable = false)
    private boolean rejected;

    @ManyToOne
    @JoinColumn(name = "rejected_by_user_id")
    private User rejectedByUser;
    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String filetype;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType(String imageSubType) {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Long getImageSize() {
        return imageSize;
    }

    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String imageType;

    @Column(nullable = false)
    private Long imageSize;
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] file;

    @Column(nullable = false)
    private String videoLink;
    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageFile;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Column(nullable = false)
    private Long fileSize;

    public Requests() {
        // Default constructor
    }

    public Requests(String title, String description, Priority priority, Status status, long requesterID, Long assignedToID) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.requesterID = requesterID;
        this.assignedToID = assignedToID;
    }

}
