package com.bdstar.taxi.net.bean;

public class Ads {

    private AdsData object;

    public AdsData getObjcet() {
        return object;
    }

    public void setObjcet(AdsData objcet) {
        this.object = objcet;
    }

    public static class AdsData{

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAds() {
            return name;
        }

        public void setAds(String ads) {
            this.name = ads;
        }
    }

}
