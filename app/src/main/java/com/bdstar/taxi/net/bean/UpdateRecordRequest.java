package com.bdstar.taxi.net.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 {
 "id":"187a4d6d8b144fee9a6a59ead242aa8f20191222165645",（必要）
 "offAdress":"北极圈42区",（必要）
 "payManey":"200",（必要）
 "payType":"1", --------->支付方式 (1网上 0现金)
 "carHealth":"2",（必要）
 "driverEvaluation":"1",（必要）
 "serviceEvaluation":"1"（必要）
 "invoice":"0"不打印发票，“1”打印发票
 }
 */
public class UpdateRecordRequest implements Parcelable {
    public static final String PAY_TYPE_CASH = "3";
    public static final String PAY_TYPE_WECHAT = "2";
    public static final String PAY_TYPE_ALIPAY = "1";

    public static final String PINCHE_STATE_NOT = "0";
    public static final String PINCHE_STATE_YES = "1";
    public static final String FAPIAO_STATE_NOT = "0";
    public static final String FAPIAO_STATE_YES = "1";

    private String id;
    private String offAdress;
    private String payManey;
    private String payType;
    private String carHealth;
    //private String driverEvaluation;
    private String serviceEvaluation;
    private String pincheState;
    private String invoice = FAPIAO_STATE_NOT;

    public UpdateRecordRequest(){}

    public UpdateRecordRequest(String id){
        this.id = id;
    }

    protected UpdateRecordRequest(Parcel in) {
        id = in.readString();
        offAdress = in.readString();
        payManey = in.readString();
        payType = in.readString();
        carHealth = in.readString();
        serviceEvaluation = in.readString();
        pincheState = in.readString();
    }

    public static final Creator<UpdateRecordRequest> CREATOR = new Creator<UpdateRecordRequest>() {
        @Override
        public UpdateRecordRequest createFromParcel(Parcel in) {
            return new UpdateRecordRequest(in);
        }

        @Override
        public UpdateRecordRequest[] newArray(int size) {
            return new UpdateRecordRequest[size];
        }
    };

    public String getPincheState() {
        return pincheState;
    }

    public void setPincheState(String pincheState) {
        this.pincheState = pincheState;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarHealth() {
        return carHealth;
    }

    public void setCarHealth(String carHealth) {
        this.carHealth = carHealth;
    }

    public String getServiceEvaluation() {
        return serviceEvaluation;
    }

    public void setServiceEvaluation(String serviceEvaluation) {
        this.serviceEvaluation = serviceEvaluation;
    }

    public String getOffAdress() {
        return offAdress;
    }

    public void setOffAdress(String offAdress) {
        this.offAdress = offAdress;
    }

    public String getPayManey() {
        return payManey;
    }

    public void setPayManey(String payManey) {
        this.payManey = payManey;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(offAdress);
        dest.writeString(payManey);
        dest.writeString(payType);
        dest.writeString(carHealth);
        dest.writeString(serviceEvaluation);
        dest.writeString(pincheState);
    }
}
