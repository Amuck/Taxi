package com.bdstar.taxi.net.bean;

/**
 * {
 *     "success": true,
 *     "error": null,
 *     "data": {
 *         "code": 100,
 *         "message": "operation successful",
 *         "object": {
 *             "id": "1",
 *             "name": "1",
 *             "cardNo": "1",
 *             "photo": null,
 *             "phone": "1",
 *             "sex": "1",
 *             "nationality": "1",
 *             "bloodType": "1",
 *             "national": "1",
 *             "record": "1",
 *             "adress": "1",
 *             "idCardNo": "1",
 *             "idCardPhoto": "1",
 *             "birth": "1",
 *             "companyId": "1",
 *             "companyName": "1",
 *             "emergencyContact": "1",
 *             "emergencyAdress": "1"
 *         }
 *     }
 * }
 */
public class DriverInfoResponse {
    private boolean success;
    private String error;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private int code;
        private String message;
        private DriverFullInfo object;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DriverFullInfo getObject() {
            return object;
        }

        public void setObject(DriverFullInfo object) {
            this.object = object;
        }
    }
}
