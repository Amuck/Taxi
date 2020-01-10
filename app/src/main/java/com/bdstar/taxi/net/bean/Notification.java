package com.bdstar.taxi.net.bean;

public class Notification {
    private NotificationData object;

    public NotificationData getObject() {
        return object;
    }

    public void setObject(NotificationData object) {
        this.object = object;
    }

    public class NotificationData{
        private String id;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
