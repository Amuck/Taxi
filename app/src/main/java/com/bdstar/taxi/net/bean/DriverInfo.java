package com.bdstar.taxi.net.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * {
 *  *             "id": "1",
 *  *             "name": "1",
 *  *             "cardNo": "1",
 *  *             "photo": null,
 *  *             "phone": "1",
 *  *             "sex": "1",
 *  *             "nationality": "1",
 *  *             "bloodType": "1",
 *  *             "national": "1",
 *  *             "record": "1",
 *  *             "adress": "1",
 *  *             "idCardNo": "1",
 *  *             "idCardPhoto": "1",
 *  *             "birth": "1",
 *  *             "companyId": "1",
 *  *             "companyName": "1",
 *  *             "emergencyContact": "1",
 *  *             "emergencyAdress": "1"
 *  *         }
 */
public class DriverInfo {
    private String id;
    private String name;
    private String cardNo;
    private String facephotourl;
    private String contact;
    private String phone;
    private String sex;
    private String nationality;
    private String bloodType;
    private String national;
    private String record;
    private String adress;
    private String idCardNo;
    private String dateofbirth;
    private String companyId;
    private String companyName;
    private String emergencyContact;
    private String emergencyAdress;
    private String age;

    public String getAge(){
        return age;
        /*String dateString = dateofbirth.substring(0, 18);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        return (Calendar.getInstance()).get(Calendar.YEAR) - calendar.get(Calendar.YEAR);*/
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFacephotourl() {
        return facephotourl;
    }

    public void setFacephotourl(String facephotourl) {
        this.facephotourl = facephotourl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex.equals("2") ? "女" : "男";
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyAdress() {
        return emergencyAdress;
    }

    public void setEmergencyAdress(String emergencyAdress) {
        this.emergencyAdress = emergencyAdress;
    }
}
