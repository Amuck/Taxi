package com.bdstar.taxi.net.bean;

public class UpdateInfo {

    private UpdateData object;

    public UpdateData getObject() {
        return object;
    }

    public void setObject(UpdateData object) {
        this.object = object;
    }

    public static class UpdateData{
        private int version;
        private String url;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
